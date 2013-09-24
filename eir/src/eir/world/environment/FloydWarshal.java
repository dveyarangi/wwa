package eir.world.environment;

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
					dists[i][j] = Float.MAX_VALUE;
					lastdists[i][j] = Float.MAX_VALUE;
					preds[i][j] = null;
					lastpreds[i][j] = null;
				}
				else
				{
					dists[i][j] = 0;
					preds[i][j] = nodes.get(i);
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
			dists[i][j] = e.getLength();
			
			lastpreds[i][j] = e.getNode2();
		}
		
		for( int k=0 ; k<n ; k++ )
		{
			for( int i=0 ; i<n ; i++ )
			{
				for( int j=0 ; j<n ; j++ )
				{
					float contendor = lastdists[i][k] + lastdists[k][j];
					
					if( lastdists[i][j]>contendor )
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
		this.dists = lastdists;
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
