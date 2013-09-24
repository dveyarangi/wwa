package eir.world.environment;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;


/**
 * represents a route across a graph <br>
 * @author Ni
 *
 */
public class Route implements Poolable
{
	public static final Pool<Route> routesPool =
			new Pool<Route>()
			{
				@Override
				protected Route newObject()
				{
					return new Route();
				}
			};
	
	private FloydWarshalRoutes navMesh;
	private NavNode from;
	private NavNode to;
	private boolean hasNext;
	private boolean first;

	public Route()
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
	public void set( FloydWarshalRoutes navMesh, NavNode from, NavNode to )
	{
		this.navMesh = navMesh;
		this.from = from;
		this.to = to;
		hasNext = ( navMesh.routes[from.id][to.id]==null ) ? false : true;
		first = true;
	}
	
	public boolean hasNext()
	{
		return hasNext;
	}
	
	public NavNode next()
	{
		if( first )
		{
			first = false;
			return from;
		}
		from = navMesh.routes[from.id][to.id];
		hasNext = ( navMesh.routes[from.id][to.id]==null || from==to ) ? false : true;
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
}
