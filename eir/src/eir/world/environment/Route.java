package eir.world.environment;


/**
 * represents a route across a graph <br>
 * @author Ni
 *
 */
public class Route
{
	private final NavMesh navMesh;
	private NavNode from;
	private NavNode to;
	private boolean hasNext;
	private boolean first = true;
	
	public Route( NavMesh navMesh, NavNode from, NavNode to )
	{
		this.navMesh = navMesh;
		this.from = from;
		this.to = to;
		hasNext = ( navMesh.routes[from.index][to.index]==null ) ? false : true;
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
		from = navMesh.routes[from.index][to.index];
		hasNext = ( navMesh.routes[from.index][to.index]==null || from==to ) ? false : true;
		return from;
	}
}
