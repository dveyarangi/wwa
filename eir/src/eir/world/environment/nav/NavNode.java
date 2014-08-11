/**
 *
 */
package eir.world.environment.nav;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.Environment;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;

/**
 * That aint public on purpose, all modifications should go through
 * {@link NavMesh}
 *
 * @author dveyarangi
 *
 */
public class NavNode implements ISpatialObject
{
	private final Vector2			point;
	// private final Vector2 rawPoint;

	/**
	 * index of this node in the in the nav mesh that contains it (unique in
	 * mesh)
	 */
	final int				idx;

	/**
	 * List of all connected nodes
	 */
	private Set<NavNode>			neighbours;

	private final NavNodeDescriptor	descriptor;

	private AABB					pickingArea;

	private int						spatialId;

	/**
	 *
	 * @param point
	 *            location of the node
	 * @param rawPoint
	 *            location of this node in real space
	 * @param idx
	 *            index for this node
	 * @param aIdx
	 *            index of the asteroid containing this node
	 */
	NavNode( final NavNodeDescriptor descriptor, final Vector2 point, final int idx )
	{
		this.descriptor = descriptor;
		this.idx = idx;
		this.point = point;
		this.neighbours = new HashSet<NavNode>();

		this.pickingArea = AABB.createSquare( point, 1f );
	}

	public Vector2 getPoint() { return point; }

	public int getIndex() { return idx; }

	public void init()
	{
		spatialId = Environment.createObjectId();
	}

	/**
	 * @param nb
	 */
	void addNeighbour( final NavNode node )
	{
		neighbours.add( node );
	}

	/**
	 * @return
	 */
	public Set<NavNode> getNeighbors()
	{
		return neighbours;
	}

	@Override
	public String toString()
	{
		return new StringBuilder().append( "navnode [" ).append( idx ).append( " (" )
				.append( getPoint() ).append( ")]" ).toString();
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
	public void removeNeighbour( final NavNode node )
	{
		neighbours.remove( node );
	}

	public NavNodeDescriptor getDescriptor()
	{
		return descriptor;
	}

	@Override
	public boolean isAlive() { return true; }


}
