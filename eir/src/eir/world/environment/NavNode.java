/**
 * 
 */
package eir.world.environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

/**
 * That aint public on purpose, all modifications should go through {@link NavMesh}
 * @author dveyarangi
 *
 */
public class NavNode
{
	private Vector2 point;
	
	/**
	 * List of all connected nodes
	 */
	private Set <NavNode> neighbours;
	
	/**
	 * TODO: map target node to next node on the shortest path to it
	 */
	private Map <NavNode, NavNode> routes;
	
	NavNode(Vector2 point)
	{
		this.point = point;
		this.neighbours = new HashSet <NavNode> ();
		this.routes = new HashMap <NavNode, NavNode> ();
	}
	
	public Vector2 getPoint() { return point; }

	/**
	 * @param nb
	 */
	void addNeighbour(NavNode node)
	{
		neighbours.add( node );
	}
	
}
