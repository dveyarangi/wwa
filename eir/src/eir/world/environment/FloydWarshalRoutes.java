package eir.world.environment;


/**
 * calculates all shortest routes for a given nav mesh
 * @author Ni
 *
 */
public class FloydWarshalRoutes
{
	private final NavMesh navMesh;
	protected NavNode[][] routes;
	
	public FloydWarshalRoutes( NavMesh navMesh )
	{
		this.navMesh = navMesh;
	}
	
	public void calculate()
	{
		int n = navMesh.nodes.size();
		
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
					preds[i][j] = navMesh.nodes.get(i);
					lastdists[i][j] = 0;
					lastpreds[i][j] = navMesh.nodes.get(i);
				}
			}
		}
		
		for( NavNode cur : navMesh.nodes )
		{
			for( NavNode neighbour : cur.getNeighbors() )
			{
				int i = cur.index;
				int j = neighbour.index;
				
				lastdists[i][j] = cur.getPoint().dst(neighbour.getPoint());
				dists[i][j] = cur.getPoint().dst(neighbour.getPoint());
				
				lastpreds[i][j] = neighbour;
			}
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
		
		routes = lastpreds;
	}
	
	/**
	 * find the shortest route between node from and to
	 * @return ordered list starting at a and ending at b using shortest route / null if no route
	 */
	public Route getShortestRoute( NavNode from, NavNode to )
	{
		Route r = Route.routesPool.obtain();
		r.set(this, from, to);
		return r;
	}
}
