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
		
		float[][] ccwdists = new float[n][n];
		float[][] cwdists = new float[n][n];
		float[][] tmpdists = null;
		
		NavNode[][] ccwpreds = new NavNode[n][n];
		NavNode[][] cwpreds = new NavNode[n][n];
		
		NavNode[][] tmppreds = null;
		
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{	
				if( i!=j )
				{
					cwdists[i][j] = Float.POSITIVE_INFINITY;
				}
				else
				{
					cwdists[i][j] = 0;
					cwpreds[i][j] = nodes.get(i);
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
			
			cwdists[i][j] = e.getLength();
			cwpreds[i][j] = e.getNode2();
		}
		
//		circle "clock wise"
		for( int[] c : indexRange )
		{
			for( int i=c[0]+2 ; i<c[1] ; i++ )
			{
				for( int j=i-2 ; j>=0 ; j-- )
				{
					cwpreds[j][i] = cwpreds[j][i-1];
					cwdists[j][i] = cwdists[j][i-1] + cwdists[j+1][i];
				}
			}
		}
		
		//TODO do this thing
//		circle "counter clock wise"
//		for( int[] c : indexRange )
//		{
//			for( int i=c[0]+2 ; i<c[1] ; i++ )
//			{
//				for( int j=i-2 ; j>=0 ; j-- )
//				{
//					cwpreds[j][i] = cwpreds[j][i-1];
//					cwdists[j][i] = cwdists[j][i-1] + cwdists[j+1][i];
//				}
//			}
//		}
		
//		System.out.println("-------------------------------------");
//		for( int i=0 ; i<33 ; i++ )
//		{
//			for( int j=0 ; j<33 ; j++ )
//			{
//				if( lastpreds[i][j]==null)
//					System.out.print("null\t");
//				else
//					System.out.print(lastpreds[i][j].idx+"\t");
//			}
//			System.out.println();
//		}
//		for( int i=0 ; i<33 ; i++ )
//		{
//			for( int j=0 ; j<33 ; j++ )
//			{
//				if( lastdists[i][j]==Float.POSITIVE_INFINITY)
//					System.out.print("inf\t");
//				else
//					System.out.printf("%3.0f\t", lastdists[i][j]);
//			}
//			System.out.println();
//		}
		
//		System.exit(0);
		this.routes = cwpreds;
		this.dists = cwdists;
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
