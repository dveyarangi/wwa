package eir.world.environment;

import com.badlogic.gdx.utils.Pool;


/**
 * represents a route across a graph <br>
 * @author Ni
 *
 */
public class FloydWarshalRoute extends Route
{
	public static final Pool<FloydWarshalRoute> routesPool =
			new Pool<FloydWarshalRoute>()
			{
				@Override
				protected FloydWarshalRoute newObject()
				{
					return new FloydWarshalRoute();
				}
			};
	
	private FloydWarshal navMesh;
	private NavNode from;
	private NavNode to;
	private boolean hasNext;
	private boolean first;

	public FloydWarshalRoute()
	{
		this.navMesh = null;
		this.from = null;
		this.to = null;
		hasNext = false;
		first = true;
	}
	
	
	/**
	 * prepare for acquire!
	 * @param navMesh
	 * @param from
	 * @param to
	 */
	public void set( FloydWarshal navMesh, NavNode from, NavNode to )
	{
		this.navMesh = navMesh;
		this.from = from;
		this.to = to;
		
		hasNext = false;
		
		for( int[] cur : navMesh.indexRange )
		{
			if( from.idx<=cur[0] && to.idx<=cur[1] )
			{
				hasNext = true;
				break;
			}
		}
		
//		hasNext = ( navMesh.routes[from.idx][to.idx]==null ) ? false : true;
		first = true;
	}
	
	@Override
	public boolean hasNext()
	{
		return hasNext;
	}
	
	@Override
	public NavNode next()
	{
		if( first )
		{
			first = false;
			return from;
		}
		from = navMesh.routes[from.idx][to.idx];
		hasNext = ( navMesh.routes[from.idx][to.idx]==null || from==to ) ? false : true;
		return from;
	}
	
	@Override
	public void reset()
	{
		this.navMesh = null;
		this.from = null;
		this.to = null;
		hasNext = false;
		first = false;
	}


	@Override
	public void recycle()
	{
		routesPool.free(this);
	}
}
