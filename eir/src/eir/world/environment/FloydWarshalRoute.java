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
	private NavNode tmpto;
	private NavNode to;
	private int[] range;
	private int dir;
	private boolean hasNext;

	public FloydWarshalRoute()
	{
		this.navMesh = null;
		this.from = null;
		this.to = null;
		this.tmpto = null;
		this.dir = 0;
		this.hasNext = false;
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
		this.tmpto = this.to = to;
		hasNext = false;
		
		if( from.aIdx == tmpto.aIdx )
		{
			hasNext = true;
		}
		else if ( navMesh.routes[from.aIdx][to.aIdx]!=null )
		{
			tmpto = navMesh.routes[from.aIdx][to.aIdx];
			hasNext = true;
		}
		
		range = navMesh.indexRange.get(from.aIdx);
	}
	
	@Override
	public boolean hasNext()
	{
		return hasNext;
	}
	
	@Override
	public NavNode next()
	{
		if( from==to )
		{
			hasNext = false;
			return to;
		}
		
		else if ( from==tmpto )
		{
			NavNode lastfrom = from;
			from = navMesh.routes[from.fwIdx][to.aIdx];
			range = navMesh.indexRange.get(from.aIdx);
			tmpto = (navMesh.routes[from.aIdx][to.aIdx]==null) ? to : navMesh.routes[from.aIdx][to.aIdx];
			return lastfrom;
		}
		
		else if( from.aIdx==tmpto.aIdx )
		{
			dir = (navMesh.cwDistance(from, tmpto) < navMesh.ccwDistance(from, tmpto)) ? 1 : -1;
			NavNode lastfrom = from;
			
			// stupid modulo for negatives is negative so i added this
			int mod = from.idx+dir-range[0];
			int rangesize = range[1]-range[0]+1;
			mod = (mod<0) ? mod + rangesize : mod;
			
			from = navMesh.getNode(mod%rangesize + range[0]);
			
			return lastfrom;
		}
		
		return null;
	}
	
	@Override
	public void reset()
	{
		this.navMesh = null;
		this.from = null;
		this.to = null;
		this.tmpto = null;
		this.hasNext = false;
		this.dir = 1;
	}


	@Override
	public void recycle()
	{
		routesPool.free(this);
	}
}
