package eir.world.environment.spatial;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents an axis-aligned bounding box.
 * 
 */
public class AABB
{
	/**
	 * center point
	 */
	private Vector2 ref = Vector2.Zero.cpy();
	
	/**
	 * Size
	 */
	private Vector2 dim = Vector2.Zero.cpy();

	
	/**
	 * marker for {@link SpatialHashMap} queries.
	 */
	private int passId;
	
//	private static InvokationMapper amap = new InvokationMapper();
	
	/**
	 * Pool of AABB objects
	 */
	private static Queue <AABB> pool = new LinkedList <AABB> ();
	
	private static AABB getAABB()
	{
		if(pool.isEmpty())
			return new AABB();
		else
			return pool.poll();
	}
	
	/**
	 * Returns specified AABB to the pool.
	 */
	public static void free(AABB aabb) { pool.add( aabb ); }
	
	/**
	 * Returns this AABB to the pool.
	 */
	public void free() { AABB.free( this ); }

	public static AABB createSquare(float x, float y, float r)
	{
		AABB aabb = getAABB();
		return aabb.update( x, y, r, r );
	}
	public static AABB createSquare(Vector2 center, float r)
	{
		AABB aabb = getAABB();
		return aabb.update(center.x, center.y, r, r);
	}
	
	public static AABB createFromEdges(float x1, float y1, float x2, float y2)
	{
		float rx = (x2 - x1) / 2.f;  
		float ry = (y2 - y1) / 2.f;
		
		AABB aabb = getAABB();
		return aabb.update(x1+rx, y1+ry, rx, ry);
	}
	
	public static AABB createFromCenter(float cx, float cy, float rx, float ry)
	{
		AABB aabb = getAABB();
		return aabb.update(cx, cy, rx, ry);
	}
	
	public static AABB createPoint(float x, float y)
	{
		AABB aabb = getAABB();
		return aabb.update(x, y, 0, 0);
	}

	
	protected AABB()
	{
//		amap.record();
		this.ref = Vector2.Zero.cpy();
	}
	
	/**
	 * C'tor
	 * @param x box center x
	 * @param y box center y
	 * @param r half box width
	 * @param a box orientation (degrees)
	 */
	
	public AABB update(float x, float y, float rx, float ry)
	{
		this.ref.set( x, y );
		this.dim.set( rx, ry );

		return this;
	}
	
	public final Vector2 getAnchor() { return ref; }
	
	public final Vector2 getDimensions() { return dim; }

	final public void translate(float dx, float dy) {
		ref.add(dx, dy);
	}

	public void move(float x, float y) {
		ref.set( x, y );
	}

	public void fitTo(float radius)
	{
		if(radius < 0)
			throw new IllegalArgumentException("AABB radius must be positive.");
		
		if(dim.x > dim.y) 
		{
			dim.x = radius;
			dim.y *= dim.y/radius;  
		}
		else
		{
			dim.x *= dim.x/radius;  
			dim.y = radius;

		}
	}
	
	public final boolean overlaps(float minx, float miny, float maxx, float maxy)
	{
		return ( (maxx >= ref.x-dim.x && maxx <= ref.x+dim.x) ||
			     (minx >= ref.x-dim.x && minx <= ref.x+dim.x) ||
			     (minx >= ref.x-dim.x && maxx <= ref.x+dim.x) ||
			     (minx <= ref.x-dim.x && maxx >= ref.x+dim.x)    
			  ) && ( 
			     (maxy >= ref.y-dim.y && maxy <= ref.y+dim.y) ||
			     (miny >= ref.y-dim.y && miny <= ref.y+dim.y) ||
			     (miny >= ref.y-dim.y && maxy <= ref.y+dim.y) ||
			     (miny <= ref.y-dim.y && maxy >= ref.y+dim.y)    
			   );

	}

	public boolean overlaps(AABB area)
	{
		return overlaps( area.getMinX(), area.getMinY(), area.getMaxX(), area.getMaxY() );
	}


	public boolean equals(Object o)
	{
		return o == this;
	}
	
	@Override
	public String toString()
	{
		return "AABB [loc:" + ref.x + ":" + ref.y + "; r:(" + dim.x +"," +dim.y +");]"; 
	}


	public final float getMinX() { return ref.x - dim.x; }
	public final float getMaxX() { return ref.x + dim.x; }
	public final float getMinY() { return ref.y - dim.y; }
	public final float getMaxY() { return ref.y + dim.y; }

	public final float getRX() {return dim.x; }
	public final float getRY() {return dim.y; }
	public final float getCenterX()
	{
		return ref.x;
	}
	public final float getCenterY()
	{
		return ref.y;
	}


	public final int getPassId() { return passId; }

	public final void setPassId(int id) {	this.passId = id; }

	public boolean contains(float x, float y)
	{
		return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY();
	}

	/**
	 * @param aabb
	 */
	public AABB copyFrom(AABB aabb)
	{
		this.ref.set( aabb.ref );
		this.dim.set( aabb.dim );
		return this;
	}
	/**
	 * @param aabb
	 */
	public static AABB copy(AABB aabb)
	{
		return new AABB().copyFrom( aabb );
	}

	/**
	 * @param ox
	 * @param oy
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean crosses(float ox, float oy, float dx, float dy)
	{
		throw new UnsupportedOperationException();
	}


}
