package eir.world.environment;

import java.util.ArrayList;

import eir.world.environment.NavEdge.Type;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntIntHashMap;


/**
 * calculates all shortest routes for a given nav mesh<br>
 * not sure we can call it floyd warshal anymore.. its only partially so.
 * @author Ni
 *
 */
public class FloydWarshal extends NavMesh
{
	/**
	 * fw shortest routes
	 */
	protected NavNode[][] routes;
	
	/**
	 * nodes tagged as web anchors
	 */
	protected ArrayList<NavNode> webNodes;
	
	/**
	 * translates node index to floyd warshal matrix index
	 */
	protected TIntIntHashMap fwIdx;
	
	/**
	 *  distances inside the same asteroid
	 */
	protected float[] localdists;
	
	public FloydWarshal()
	{
		webNodes = new ArrayList<NavNode>();
	}
	
	/**
	 * initialize in asteroid routes and initial floyd warshal 
	 */
	public void init()
	{
		int n = nodes.size();
		
		// for navigation between nodes inside the same asteroid
		localdists = new float[n];
		
		TIntObjectIterator<NavEdge> it = edges.iterator();
		
		// add all "land" edges
		while( it.hasNext() )
		{
			it.advance();
			
			if( it.value().type!=Type.LAND )
				continue;
			
			NavEdge e = it.value();
			int a = e.getNode1().idx;
			int b = e.getNode2().idx;
			
			if( a>b )
				localdists[a] = e.getLength();
			else
				localdists[b] = e.getLength();
		}
		
		// generate local dists
		for( int[] c : indexRange )
		{
			localdists[c[1]] = localdists[c[0]];
			
			for( int i=c[0]+2 ; i<=c[1] ; i++ )
			{
				localdists[i] += localdists[i-1];
			}
		}
		
		reFloydWarshal();
	}
	
	/**
	 * recalculate floyd warshal routes
	 */
	public void reFloydWarshal()
	{
		int n = webNodes.size() + indexRange.size();
		
		float[][] dists = new float[n][n];
		float[][] lastdists = new float[n][n];
		float[][] tmpdists = null;
		
		NavNode[][] preds = new NavNode[n][n];
		NavNode[][] lastpreds = new NavNode[n][n];
		NavNode[][] tmppreds = null;
		
		TIntIntHashMap fwIdx = new TIntIntHashMap();
		
		// init dists
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{
				if( i!=j )
					lastdists[i][j] = Float.POSITIVE_INFINITY;
				else
					lastdists[i][j] = 0;
			}
		}
		
		// - put web nodes
		// - put distance between center nodes and web nodes 
		// 		(= sum of all edges in asteroid to prevent fw from choosing this virtual node as path between two real nodes)
		for( int i=0 ; i<webNodes.size() ; i++ )
		{
			NavNode node = webNodes.get(i);
			int[] range = indexRange.get(node.aIdx);
			
			int nodefwIdx = indexRange.size() + i;
			fwIdx.put( node.idx, nodefwIdx );
			lastpreds[nodefwIdx][node.aIdx] = lastpreds[node.aIdx][nodefwIdx] = lastpreds[node.aIdx][nodefwIdx] = node;
			lastdists[nodefwIdx][node.aIdx] = lastdists[node.aIdx][nodefwIdx] = localdists[range[0]] + localdists[range[1]];
		}
		
		
		TIntObjectIterator<NavEdge> it = edges.iterator();
		
		// add web nodes and distance between two ends of each web
		while( it.hasNext() )
		{
			it.advance();
			NavEdge e = it.value();
			
			if( e.type!=NavEdge.Type.WEB )
				continue;
			
			int i = fwIdx.get(e.getNode1().idx);
			int j = fwIdx.get(e.getNode2().idx);
			
			lastdists[i][j] = e.getLength();
			lastpreds[i][j] = e.getNode2();
		}
		
		// add distance between all nodes in the same asteroid
		for( NavNode from : webNodes )
		{
			for( NavNode to : webNodes )
			{
				if( from.aIdx != to.aIdx || from==to )
					continue;
				
				int from_fwIdx = fwIdx.get(from.idx);
				int to_fwIdx = fwIdx.get(to.idx);
				
				lastdists[from_fwIdx][to_fwIdx] = Math.min(cwDistance(from, to), ccwDistance(from, to));
				lastpreds[from_fwIdx][to_fwIdx] = to;
			}
		}
		
		// floyd warshal
		for( int k=0 ; k<n ; k++ )
		{
			for( int i=0 ; i<n ; i++ )
			{
				for( int j=0 ; j<n ; j++ )
				{
					float contendor = lastdists[i][k] + lastdists[k][j];
					
					if( lastdists[i][j]>contendor && contendor!=0 )
					{
						dists[i][j] = contendor;
						preds[i][j] = lastpreds[i][k];
					}
					else
					{
						dists[i][j] = lastdists[i][j];
						preds[i][j] = lastpreds[i][j];
					}
				}
			}
			
			// swap buffers
			tmppreds = lastpreds;
			lastpreds = preds;
			preds = tmppreds;
			
			tmpdists = lastdists;
			lastdists = dists;
			dists = tmpdists;
		}
		
		this.routes = lastpreds;
		this.fwIdx = fwIdx;
	}
	
	
	@Override
	public void linkNodes(NavNode na, NavNode nb, Type type)
	{
		super.linkNodes(na, nb, type);
		
		if( type==Type.WEB )
		{
			webNodes.add(na);
			webNodes.add(nb);
		}
	}
	
	
	/**
	 * find the shortest route between node from and to
	 * @return Route starting at a and ending at b using shortest route
	 */
	@Override
	public Route getShortestRoute( NavNode from, NavNode to )
	{
		FloydWarshalRoute r = FloydWarshalRoute.routesPool.obtain();
		r.set(this, from, to);
		return r;
	}
	
	/**
	 * returns the distance between <b>from</b> and <b>to</b> in positive index direction ("clockwise")
	 * @param from
	 * @param to
	 * @return clockwise distance between <b>from</b> and <b>to</b> if they are on the same asteroid, infinity otherwise
	 */
	public float cwDistance( NavNode from, NavNode to )
	{
		if( from.aIdx != to.aIdx )
			return Float.POSITIVE_INFINITY;
		
		if( from.idx < to.idx )
			return (from.idx==indexRange.get(from.aIdx)[0]) ? localdists[to.idx] : localdists[to.idx] - localdists[from.idx];
		else
			return localdists[indexRange.get(from.aIdx)[1]] - localdists[from.idx] + localdists[to.idx];
	}
	
	/**
	 * returns the distance between <b>from</b> and <b>to</b> in negative index direction ("counter clockwise")
	 * @param from
	 * @param to
	 * @return counter clockwise distance between <b>from</b> and <b>to</b> if they are on the same asteroid, infinity otherwise
	 */
	public float ccwDistance( NavNode from, NavNode to )
	{
		if( from.aIdx != to.aIdx )
			return Float.POSITIVE_INFINITY;
		
		if( from.idx < to.idx )
			return localdists[indexRange.get(from.aIdx)[1]] - localdists[to.idx] + localdists[from.idx];
		else
			return (to.idx==indexRange.get(from.aIdx)[0]) ? localdists[from.idx] : localdists[from.idx] - localdists[to.idx];
	}
}
