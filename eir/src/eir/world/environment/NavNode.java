/**
 * 
 */
package eir.world.environment;

import java.util.HashSet;
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
	 * index of this node in navmesh
	 */
	public final int index;
	
	/**
	 * List of all connected nodes
	 */
	private Set <NavNode> neighbours;
	
	NavNode(Vector2 point, int index)
	{
		this.index = index;
		this.point = point;
		this.neighbours = new HashSet <NavNode> ();
	}
	
	public Vector2 getPoint() { return point; }

	/**
	 * @param nb
	 */
	void addNeighbour(NavNode node)
	{
		neighbours.add( node );
	}

	/**
	 * @return
	 */
	public Set <NavNode> getNeighbors()
	{
		return neighbours;
	}
}
