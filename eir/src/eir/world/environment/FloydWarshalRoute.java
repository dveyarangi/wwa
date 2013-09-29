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
	private int[] range;
	private int dir;
	private boolean hasNext;

	public FloydWarshalRoute()
	{
		this.navMesh = null;
		this.from = null;
		this.to = null;
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
		this.to = to;
		
		hasNext = false;
		
		for( int[] cur : navMesh.indexRange )
		{
			if( from.idx>=cur[0] && from.idx<=cur[1] && to.idx>=cur[0] && to.idx<=cur[1] )
			{
				range = cur;
				hasNext = true;
				
				float cwlen=0, ccwlen=0;
				
				if( from.idx < to.idx )
				{	
					cwlen  = (from.idx==cur[0]) ? navMesh.localdists[to.idx] : navMesh.localdists[to.idx] - navMesh.localdists[from.idx];
					ccwlen = navMesh.localdists[cur[1]] - navMesh.localdists[to.idx] + navMesh.localdists[from.idx];
				}
				else
				{
					// TODO think this part over
					ccwlen = (from.idx==cur[0]) ? navMesh.localdists[to.idx] : navMesh.localdists[to.idx] - navMesh.localdists[from.idx];
					cwlen  = navMesh.localdists[cur[1]] - navMesh.localdists[to.idx] + navMesh.localdists[from.idx];
				}
				
				dir = (cwlen<ccwlen) ? 1 : -1;
				
				break;
			}
		}
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
		NavNode lastfrom = from;
		
		// stupid modulo for negatives is negative so i added this
		int mod = from.idx+dir-range[0];
		int rangesize = range[1]-range[0]+1;
		mod = (mod<0) ? mod + rangesize : mod;
		
		from = navMesh.getNode(mod%rangesize +range[0]);
		
		return lastfrom;
	}
	
	@Override
	public void reset()
	{
		this.navMesh = null;
		this.from = null;
		this.to = null;
		this.hasNext = false;
		this.dir = 1;
	}


	@Override
	public void recycle()
	{
		routesPool.free(this);
	}
}
