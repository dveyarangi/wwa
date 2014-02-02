/**
 * 
 */
package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public class NavEdge
{
	public enum Type { WEB, LAND, AIR }
	
	public final Type type;
	
	/**
	 * indexes of navnodes connected by this edge
	 */
	private final NavNode node1, node2;
	
	/**
	 * normal
	 */
	private final Vector2 normal;
	
	/**
	 * normalized direction vector
	 */
	private final Vector2 direction;
	
	/**
	 * edge slope
	 */
	private final float slope;
	
	/**
	 * lengths of polygon edges
	 */
	private final float length;

	protected NavEdge(NavNode node1, NavNode node2, Type type)
	{
		this.type = type;
		this.node1 = node1;
		this.node2 = node2;
		
		Vector2 a = node1.getPoint();
		Vector2 b = node2.getPoint();
			
		direction = new Vector2( b.x-a.x, b.y-a.y ).nor();
		
		// TODO: checkout about vertical ones
		slope = direction.y / direction.x;
		
		normal = b.tmp().sub( a ).rotate( 90 ).cpy();
		
		length = (float)Math.hypot( b.x-a.x, b.y-a.y );

	}

	/**
	 * @return the idx1
	 */
	public NavNode getNode1()
	{
		return node1;
	}

	/**
	 * @return the idx2
	 */
	public NavNode getNode2()
	{
		return node2;
	}

	/**
	 * @return the normal
	 */
	public Vector2 getNormal()
	{
		return normal;
	}


	/**
	 * @return the direction
	 */
	public Vector2 getDirection()
	{
		return direction;
	}


	/**
	 * @return the slope
	 */
	public float getSlope()
	{
		return slope;
	}


	/**
	 * @return the length
	 */
	public float getLength()
	{
		return length;
	}

	/**
	 * @return
	 */
	public Type getType()
	{
		return type;
	}
	
	
}
