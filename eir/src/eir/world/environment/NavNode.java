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
	 * index of this node in the in the nav mesh that contains it (unique in mesh)
	 */
	public final int idx;
	
	/**
	 * List of all connected nodes
	 */
	private Set <NavNode> neighbours;
	
	NavNode(Vector2 point, Vector2 rawPoint, int idx)
	{
		this.idx = idx;
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
			.append("navnode [").append(idx)
			.append(" (").append(getPoint()).append(")]")
			.toString();
	}
}
