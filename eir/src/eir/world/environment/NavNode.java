/**
 * 
 */
package eir.world.environment;

import java.util.HashSet;
import java.util.Set;

import yarangi.math.IVector2D;
import yarangi.math.Vector2D;

import com.badlogic.gdx.math.Vector2;

/**
 * That aint public on purpose, all modifications should go through {@link NavMesh}
 * @author dveyarangi
 *
 */
public class NavNode implements IVector2D
{
	private Vector2 point;
	private Vector2 rawPoint;
	
	/**
	 * index of this node in navmesh
	 */
	public final int index;
	
	/**
	 * List of all connected nodes
	 */
	private Set <NavNode> neighbours;
	
	NavNode(Vector2 point, Vector2 rawPoint, int index)
	{
		this.index = index;
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
	
	/* (non-Javadoc)
	 * @see yarangi.math.IVector2D#abs()
	 */
	@Override
	public double abs()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see yarangi.math.IVector2D#crossZComponent(yarangi.math.IVector2D)
	 */
	@Override
	public double crossZComponent(IVector2D arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see yarangi.math.IVector2D#crossZComponent(double, double)
	 */
	@Override
	public double crossZComponent(double arg0, double arg1)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2D left()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2D minus(IVector2D arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double x() {	return point.x; }

	@Override
	public double y() {	return point.y; }
	
	public String toString()
	{
		return new StringBuilder()
			.append("navnode [").append(index)
			.append(" (").append(getPoint()).append(")]")
			.toString();
	}
}
