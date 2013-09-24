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
	private Vector2 rawPoint;
	
	/**
	 * index of this node in navmesh
	 */
	public final int id;
	
	/**
	 * List of all connected nodes
	 */
	private Set <NavNode> neighbours;
	
	NavNode(Vector2 point, Vector2 rawPoint, int index)
	{
		this.id = index;
		this.point = point;
		this.rawPoint = rawPoint;
		this.neighbours = new HashSet <NavNode> ();
	}
	
	public Vector2 getPoint() { return point; }
	public Vector2 getRawPoint() { return rawPoint; }

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
	
	public String toString()
	{
		return new StringBuilder()
			.append("navnode [").append(id)
			.append(" (").append(getPoint()).append(")]")
			.toString();
	}
}
