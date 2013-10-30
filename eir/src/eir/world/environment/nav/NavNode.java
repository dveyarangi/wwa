/**
 * 
 */
package eir.world.environment.nav;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

import eir.world.Level;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;

/**
 * That aint public on purpose, all modifications should go through {@link NavMesh}
 * @author dveyarangi
 *
 */
public class NavNode implements ISpatialObject
{
	private final Vector2 point;
	private final Vector2 rawPoint;
	
	/**
	 * index of this node in the in the nav mesh that contains it (unique in mesh)
	 */
	public final int idx;
	
	/**
	 * index of the asteroid containing this nav node 
	 */
	public final int aIdx;
	
	/**
	 * List of all connected nodes
	 */
	private Set <NavNode> neighbours;

	private final NavNodeDescriptor descriptor;
	
	private AABB pickingArea;
	
	private int spatialId;
	
	/**
	 * 
	 * @param point location of the node
	 * @param rawPoint location of this node in real space
	 * @param idx index for this node
	 * @param aIdx index of the asteroid containing this node
	 */	
	NavNode(NavNodeDescriptor descriptor ,Vector2 point, Vector2 rawPoint, int idx, int aIdx)
	{
		this.descriptor = descriptor;
		this.idx = idx;
		this.point = point;
		this.rawPoint = rawPoint;
		this.aIdx = aIdx;
		this.neighbours = new HashSet <NavNode> ();
		
		this.pickingArea = AABB.createSquare( point, 0.1f );
	}
	
	public Vector2 getPoint() { return point; }
	public Vector2 getRawPoint() { return rawPoint; }
	
	public void init(Level level)
	{
		spatialId = Level.createObjectId();
	}

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

	@Override
	public AABB getArea()
	{
		return pickingArea;
	}

	@Override
	public int getId()
	{
		return spatialId;
	}

	/**
	 * @param nb
	 */
	public void removeNeighbour(NavNode node)
	{
		neighbours.remove( node );
	}
	
	public NavNodeDescriptor getDescriptor() { return descriptor; }
}
