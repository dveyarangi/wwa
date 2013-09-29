/**
 * 
 */
package eir.world.environment;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

import eir.world.Asteroid;
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
	
	private Asteroid asteroid;
	
	private int asteroidIdx;
	
	private AABB pickingArea;
	
	private int spatialId;
	
	NavNode(Asteroid asteroid, int asteroidIdx ,Vector2 point, Vector2 rawPoint, int idx)
	{
		this.asteroid = asteroid;
		this.asteroidIdx = asteroidIdx;
		this.idx = idx;
		this.point = point;
		this.rawPoint = rawPoint;
		this.neighbours = new HashSet <NavNode> ();
		
		this.pickingArea = AABB.createSquare( point, 0.1f );
	}
	
	public Vector2 getPoint() { return point; }
	public Vector2 getRawPoint() { return rawPoint; }
	
	public void init(Level level)
	{
		spatialId = level.createObjectId();
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
	
	public Asteroid getAsteroid() { return asteroid; }
	public int getAsteroidIdx() { return asteroidIdx; }
}
