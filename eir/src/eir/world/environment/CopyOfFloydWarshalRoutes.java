package eir.world.environment;


/**
 * calculates all shortest routes for a given nav mesh
 * @author Ni
 *
 */
public class CopyOfFloydWarshalRoutes extends NavMesh
{
//	protected NavNode[][] routes;
	
	protected int[][] next;
	
	protected float[][] dist;
	
	public CopyOfFloydWarshalRoutes()
	{
	}
	
	public void init()
	{
		calculate();
	}
	
	public void calculate()
	{
		int n = nodes.size();
		
		dist = new float[n][n];
		next = new int [n][n];			
		initArrays(dist, next, n);


		for( int k=0 ; k<n ; k++ )
		{
			
			for( int i=0 ; i<n ; i++ )
			{
				for( int j=0 ; j<n ; j++ )
				{
		            if(dist[i][k] + dist[k][j] < dist[i][j])
		            {
		               dist[i][j] = dist[i][k] + dist[k][j];
		               next[i][j] = k;
		            }
				}
			}
			
		}
		
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{
				if(i == j)
					continue;
				if(!hasPath(i, j))
					continue;
				
				int prevNextIdx = j,nextIdx = next[i][j];
				
				while(!isNeighbor(i, nextIdx) && prevNextIdx != nextIdx)
				{
					prevNextIdx = nextIdx;
					nextIdx = next[i][nextIdx];
					next[i][j] = nextIdx;
				}
				
				
			}
		}
	}
	
	private void initArrays(float [][] dist, int [][] next, int n)
	{
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{	
				if( i!=j )
				{
					dist[i][j] = Float.POSITIVE_INFINITY;
					next[i][j] = -1;
				}
				else
				{
					dist[i][j] = 0;
					next[i][j] = i;
				}
			}
		}	
		
		for(NavNode node : nodes)
		{
			for(NavNode neigh : node.getNeighbors())
			{
				dist[node.id][neigh.id] = dist[node.id][neigh.id] = node.getPoint().dst(neigh.getPoint());
				next[node.id][neigh.id] = neigh.id;
				next[neigh.id][node.id] = node.id;
			}
		}
		
	}
	
	public boolean hasPath(int fromIdx, int toIdx)
	{
		return !Float.isInfinite( dist[fromIdx][toIdx] );
	}
	
	public boolean isNeighbor(int idx, int jdx)
	{
		NavNode node = getNode(idx);
		for(NavNode neigh : node.getNeighbors())
		{
			if(neigh.id == jdx)
				return true;
		}
		
		return false;
	}

	
	/**
	 * find the shortest route between node from and to
	 * @return ordered list starting at a and ending at b using shortest route / null if no route
	 */
	public Route getShortestRoute( NavNode from, NavNode to )
	{
		Route r = Route.routesPool.obtain();
//		r.set(this, from, to);
		return r;
	}

	public int getNextNode(int currIdx, int targetIdx) {
		return next[currIdx][targetIdx];
	}
}
