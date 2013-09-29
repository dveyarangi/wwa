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
	
	protected float[][] dists;
	protected NavNode[] cwpreds;
	protected NavNode[] ccwpreds;
	
	
	public FloydWarshal()
	{
	}
	
	public void init()
	{
		calculate();
	}
	
	public void calculate()
	{
		int n = nodes.size();
		
		float[] dists = new float[n];
		
		NavNode[] cwpreds = new NavNode[n];
		NavNode[] ccwpreds = new NavNode[n];
		
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
				dists[a] = e.getLength();
			else
				dists[b] = e.getLength();
		}
		
		for( int[] c : indexRange )
		{
			dists[c[1]] = dists[c[0]];
			dists[c[0]] = 0;
			
			for( int i=c[0]+1 ; i<=c[1] ; i++ )
			{
				dists[i] += dists[i-1];
			}
		}
		
		System.exit(0);
//		this.dists = cwdists;
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
	public float distance( NavNode From, NavNode to )
	{
		return dists[From.idx][to.idx];
	}
}
