package eir.world.environment;

import eir.world.environment.NavEdge.Type;
import gnu.trove.iterator.TIntObjectIterator;


/**
 * calculates all shortest routes for a given nav mesh
 * @author Ni
 *
 */
public class FloydWarshal extends NavMesh
{
	protected NavNode[][] routes;
	
	// distantces inside the same asteroid
	protected float[] localdists;
	
	public FloydWarshal()
	{
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
		
		while( it.hasNext() )
		{
			it.advance();
			
			if( it.value().type==Type.WEB )
				continue;
			
			NavEdge e = it.value();
			int a = e.getNode1().idx;
			int b = e.getNode2().idx;
			
			if( a>b )
				localdists[a] = e.getLength();
			else
				localdists[b] = e.getLength();
		}
		
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
		int n = nodes.size();
		
		float[][] dists = new float[n][n];
		float[][] lastdists = new float[n][n];
		float[][] tmpdists = null;
		
		NavNode[][] preds = new NavNode[n][n];
		NavNode[][] lastpreds = new NavNode[n][n];
		
		NavNode[][] tmppreds = null;
		
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{	
				if( i!=j )
				{
					lastdists[i][j] = Float.POSITIVE_INFINITY;
				}
				else
				{
					lastdists[i][j] = 0;
					lastpreds[i][j] = nodes.get(i);
				}
			}
		}	
		
		TIntObjectIterator<NavEdge> it = edges.iterator();
		
		while( it.hasNext() )
		{
			it.advance();
			NavEdge e = it.value();
			int i = e.getNode1().idx;
			int j = e.getNode2().idx;
			
			lastdists[i][j] = e.getLength();
			lastpreds[i][j] = e.getNode2();
		}
		
		for( int[] c : indexRange )
		{
			for( int k=c[0] ; k<c[1] ; k++ )
			{
				for( int i=c[0] ; i<c[1] ; i++ )
				{
					for( int j=c[0] ; j<c[1] ; j++ )
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
		}
		
		this.routes = lastpreds;
	}
	
	/**
	 * find the shortest route between node from and to
	 * @return Route starting at a and ending at b using shortest route
	 */
	@Override
	public FloydWarshalRoute getShortestRoute( NavNode from, NavNode to )
	{
		FloydWarshalRoute r = FloydWarshalRoute.routesPool.obtain();
		r.set(this, from, to);
		return r;
	}
	
	/**
	 * returns the shortest distance between from and to
	 * @param From
	 * @param to
	 * @return
	 */
//	public float distance( NavNode From, NavNode to )
//	{
//		return dists[From.idx][to.idx];
//	}
}
