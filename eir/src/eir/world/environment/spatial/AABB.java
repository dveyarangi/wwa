package eir.world.environment.spatial;

import java.util.LinkedList;
import java.util.Queue;

import yarangi.java.InvokationMapper;

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
	 * half-width of the box
	 */
	private float rx;
	/**
	 * half-height of the box
	 */
	private float ry;
	
	/**
	 * max of rx and ry
	 */
	private float rmax;
	
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
	
	protected AABB update(float x, float y, float rx, float ry)
	{
		this.ref.set( x, y );
		rmax = Math.max(rx, ry);
		this.rx = rx;
		this.ry = ry;

		return this;
	}
	
	public final Vector2 getAnchor() { return ref; }

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
		
		if(rx > ry) 
		{
			rx = radius;
			ry *= rx/radius;  
		}
		else
		{
			rx *= rx/radius;  
			ry = radius;

		}
		this.rmax = radius;
	}
	
	public final boolean overlaps(float minx, float miny, float maxx, float maxy)
	{
		return ( (maxx >= ref.x-rx && maxx <= ref.x+rx) ||
			     (minx >= ref.x-rx && minx <= ref.x+rx) ||
			     (minx >= ref.x-rx && maxx <= ref.x+rx) ||
			     (minx <= ref.x-rx && maxx >= ref.x+rx)    
			  ) && ( 
			     (maxy >= ref.y-ry && maxy <= ref.y+ry) ||
			     (miny >= ref.y-ry && miny <= ref.y+ry) ||
			     (miny >= ref.y-ry && maxy <= ref.y+ry) ||
			     (miny <= ref.y-ry && maxy >= ref.y+ry)    
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
		return "AABB [loc:" + ref.x + ":" + ref.y + "; r:(" + rx +"," +ry +");]"; 
	}


	public final float getMinX() { return ref.x - rx; }
	public final float getMaxX() { return ref.x + rx; }
	public final float getMinY() { return ref.y - ry; }
	public final float getMaxY() { return ref.y + ry; }

	public final float getRX() {return rx; }
	public final float getRY() {return ry; }
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
		this.rx = aabb.rx;
		this.ry = aabb.ry;
		this.rmax = aabb.rmax;
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
