package eir.world.environment.spatial;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashSet;
import java.util.Set;

import yarangi.math.FastMath;

/**
 * Straightforward implementation of spatial hash map.
 * 
 * Note: cannot be used in multi-threaded environment, due to passId optimization (and lack of any type of synchronization).
 * @param <O>
 */
public class SpatialHashMap <O extends ISpatialObject>
{
	/**
	 * buckets array
	 * see {@link #hash(int, int)} for indices here
	 * TODO: hashmap is slow!!!
	 */
	protected Set <O> [] map;

	/**
	 * This is cache of object location;
	 * For each indexed object, we keep copy of it's AABB, so when object asks for AABB 
	 * update, the hashmap can fix buckets accordingly.
	 */
	private TIntObjectHashMap<AABB> aabbs = new TIntObjectHashMap<AABB> ();
	
	/**
	 * number of buckets
	 */
	private int size;
	
	/**
	 * dimensions of area this hashmap represents
	 */
	private float width, height;

	/**
	 * size of single hash cell
	 */
	private float cellSize; 
	
	/** 
	 * 1/cellSize, to speed up some calculations
	 */
	private float invCellsize;
	
	/** 
	 * cellSize/2
	 */
	private float halfCellSize;
	/**
	 * hash cells amounts 
	 */
	private int halfGridWidth, halfGridHeight;
	
	/**
	 * Used by query methods to mark tested objects and avoid result duplication;
	 * thusly permits only single threaded usage. 
	 */
	private int passId;
	
	public SpatialHashMap(String name, float cellSize, float width, float height)
	{
		this(name, (int)(width*height/(cellSize*cellSize)), cellSize, width, height);
	}
	/**
	 * 
	 * @param size amount of buckets
	 * @param cellSize bucket spatial size
	 * @param width covered area width
	 * @param height covered area height
	 */
	@SuppressWarnings("unchecked")
	public SpatialHashMap(String name, int size, float cellSize, float width, float height)
	{
		this.size = size;
		
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.invCellsize = 1.f / this.cellSize;
		this.halfGridWidth = (int)(width/2f/cellSize);
		this.halfGridHeight = (int)(height/2f/cellSize);
		this.halfCellSize = cellSize/2.f;
		map = new Set[size];
		for(int idx = 0; idx < size; idx ++)
			map[idx] = new HashSet <O> ();
	}
	
	/**
	 * TODO: Optimizing constructor
	 * @param width
	 * @param height
	 * @param averageAmount
	 */
	public SpatialHashMap(String name, int width, int height, int averageAmount)
	{
		throw new IllegalStateException("Not implemented yet.");
	}
	

	/**
	 * @return width of the area, covered by this map
	 */
	public final float getHeight() { return height; }

	/**
	 * @return height of the area, covered by this map
	 */
	public final float getWidth() { return width; }

	/**
	 * @return size (height and width) of a single cell
	 */
	public final float getCellSize() { return cellSize; }
	
	/**
	 * Retrieves content of the bucket that holds the contents of (x,y) cell.
	 * Result may contain data from other cells as well.
	 * @param x
	 * @param y
	 * @return
	 */
	public final Set <O> getBucket(int x, int y)
	{
		return map[hash(x, y)];
	}
	
	/**
	 * @return buckets number
	 */
	public final int getBucketCount() { return size; }

	/**
	 * Calculates spatial hash value.
	 * TODO: closure? %)
	 * @param x cell x cell coordinate (can range from -halfWidth to halfWidth)
	 * @param y cell y cell coordinate (can range from -halfHeight to halfHeight)
	 * @return
	 */
	protected final int hash(int x, int y)
	{
		return ((x+halfGridWidth)*6184547 + (y+halfGridHeight)* 2221069) % size;
	}

	/**
	 * Adds spatial object to the indexer.
	 * @param object
	 */
	public void add(O object) 
	{
		AABB transition = aabbs.get( object.getId() );
		if(transition != null)
			throw new IllegalArgumentException("Object " + object + " is already registered.");
		
		transition = AABB.copy( object.getArea() );
	
		iterateOverAABB( transition, false, object );
		
		aabbs.put( object.getId(), transition );
	}

	/**
	 * Removes spatial object from the indexer
	 * @param object
	 * @return
	 */
	public O remove(O object) 
	{
		
		AABB transition = aabbs.get( object.getId() );
		if(transition == null)
			throw new IllegalArgumentException("Object " + object + " is not registered.");
		
		iterateOverAABB( transition, true, object );
		
		aabbs.remove( object.getId() );
		
		AABB.free( transition );
		
		return object;
	}
	
/*	public void addLine(float ox, float oy, float dx, float dy)
	{
		int currGridx = toGridIndex(ox);
		int currGridy = toGridIndex(oy);
		float tMaxX, tMaxY;
		float tDeltaX, tDeltaY;
		int stepX, stepY;
		if(dx > 0)
		{
			tMaxX = ((currGridx*cellSize + halfCellSize) - ox) / dx;
			tDeltaX = cellSize / dx;
			stepX = 1;
		}					
		else
		if(dx < 0)
		{
			tMaxX = ((currGridx*cellSize - halfCellSize) - ox) / dx;
			tDeltaX = -cellSize / dx;
			stepX = -1;
		}
		else { tMaxX = Float.MAX_VALUE; tDeltaX = 0; stepX = 0;}
		
		if(dy > 0)
		{
			tMaxY = ((currGridy*cellSize + halfCellSize) - oy) / dy;
			tDeltaY = cellSize / dy;
			stepY = 1;
		}
		else
		if(dy < 0)
		{
			tMaxY = ((currGridy*cellSize - halfCellSize) - oy) / dy;
			tDeltaY = -cellSize / dy;
			stepY = -1;
		}
		else { tMaxY = Float.MAX_VALUE; tDeltaY = 0; stepY = 0;}
		
		// marks entity area to avoid reporting entity multiple times
		int passId = getNextPassId();
		Set <O> cell;
		while(tMaxX <= 1 || tMaxY <= 1)
		{
			if(tMaxX < tMaxY)
			{
				tMaxX += tDeltaX;
				currGridx += stepX;
			}
			else
			{
				tMaxY += tDeltaY;
				currGridy += stepY;
			}
			cell = map[hash(currGridx, currGridy)];
			for(O object : cell)
			{
				if(object.getArea().getPassId() == passId)
					continue;
				
				AABB aabb = object.getArea();
				if(aabb.crosses(ox, oy, dx, dy))
					if(sensor.objectFound(object))
						break;
				
				object.getArea().setPassId( passId );
			}	
		}		
		
		return sensor;	
	}*/

	/**
	 * TODO: this method must be optimized by all casts.
	 * Update object index
	 * @param object
	 */
	public void update(O object) 
	{
		
		AABB transition = aabbs.get( object.getId() );
		iterateOverAABB( transition, true, object );
		transition.copyFrom( object.getArea() );
		iterateOverAABB( transition, false, object );
	}

	/**
	 * Iterates over specified aabb and performs operation specified by "odeToJava" parameter.
	 * 
	 * @param aabb
	 * @param odeToJava
	 * @param sensor
	 * @param object
	 * @return
	 */
	private void iterateOverAABB( AABB aabb, boolean remove, O object)
	{
		iterateOverAABB( aabb.getCenterX(), aabb.getCenterY(), aabb.getRX(), aabb.getRY(), remove, object );
	}
	
	private void iterateOverAABB( float cx, float cy, float rx, float ry, boolean remove, O object)
	{
		
		int minIdxx = Math.max(toGridIndex(cx - rx), -halfGridWidth);
		int minIdxy = Math.max(toGridIndex(cy - ry), -halfGridHeight);
		int maxIdxx = Math.min(toGridIndex(cx + rx),  halfGridWidth);
		int maxIdxy = Math.min(toGridIndex(cy + ry),  halfGridHeight);
		
		int currx, curry;
		int hash;
		
		Set <O> cell;
		
		for(currx = minIdxx; currx <= maxIdxx; currx ++)
		{
			for(curry = minIdxy; curry <= maxIdxy; curry ++)
			{
//				System.out.println(minx + " " + miny + " " + maxx + " " + maxy);
				hash = hash(currx, curry);
				
				if(hash < 0) // invalid access
					continue;
				
				cell = map[hash];
				if(remove)
					cell.remove( object );
				else
					cell.add(object);
			}
		}
	}
	
	/**
	 * Walks over provided AABB and feeds the sensor with object whose AABBs overlap it
	 */
	public ISpatialSensor <O> queryAABB(ISpatialSensor <O> sensor, AABB aabb)
	{
		return queryAABB( sensor, aabb.getCenterX(), aabb.getCenterY(), aabb.getRX(), aabb.getRY() );
	}

	/**
	 * Walks over provided AABB and feeds the sensor with object whose AABBs overlap it
	 */
	public ISpatialSensor <O> queryAABB(ISpatialSensor <O> sensor, float cx, float cy, float rx, float ry)
	{
		float minx = cx - rx;
		float miny = cy - ry;
		float maxx = cx + rx;
		float maxy = cy + ry;

		
		int minIdxx = Math.max(toGridIndex(minx), -halfGridWidth);
		int minIdxy = Math.max(toGridIndex(miny), -halfGridHeight);
		int maxIdxx = Math.min(toGridIndex(maxx),  halfGridWidth);
		int maxIdxy = Math.min(toGridIndex(maxy),  halfGridHeight);
		
		int currx, curry;
		int passId = getNextPassId();
		int hash;
		
		Set <O> cell;
		AABB objectArea;
		
		found: for(currx = minIdxx; currx <= maxIdxx; currx ++)
			for(curry = minIdxy; curry <= maxIdxy; curry ++)
			{
//				System.out.println(minx + " " + miny + " " + maxx + " " + maxy);
				hash = hash(currx, curry);
				
				if(hash < 0)
					continue;
				cell = map[hash];
				
				for(O obj : cell)
				{
					objectArea = obj.getArea();
					if(objectArea.getPassId() == passId)
						continue;
					if(objectArea.overlaps( minx, miny, maxx, maxy ))
						if(sensor.objectFound(obj))
							break found;
					objectArea.setPassId( passId );
				}			
			}
		
		return sensor;
	}

	/**
	 * Walks over provided circle and feeds the sensor with object whose AABBs overlap it
	 * 
	 * @param sensor
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
	public final ISpatialSensor <O> queryRadius(ISpatialSensor <O> sensor, float x, float y, float radius)
	{
		// TODO: spiral iteration, remove this root calculation:
		float radiusSquare = radius*radius;
		int minx = Math.max(toGridIndex(x-radius), -halfGridWidth);
		int miny = Math.max(toGridIndex(y-radius), -halfGridHeight);
		int maxx = Math.min(toGridIndex(x+radius),  halfGridWidth);
		int maxy = Math.min(toGridIndex(y+radius),  halfGridHeight);
		int passId = getNextPassId();
//		O object;
		
//		System.out.println("dim: " + minx + " " + maxx + " " + miny + " " + maxy + "area size: " + (maxx-minx)*(maxy-miny));
		// removing the object from all overlapping buckets:
		Set <O> cell;
		for(int tx = minx; tx <= maxx; tx ++)
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = map[hash(tx, ty)];
				
				float distanceSquare = FastMath.powOf2(x - tx*cellSize) + FastMath.powOf2(y - ty*cellSize);
				if(radiusSquare < distanceSquare)
					continue;
				
//				System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));
				
				// TODO: make it strictier:
				for(O object : cell)
				{
					if(object.getArea().getPassId() == passId)
						continue;
					
					distanceSquare = 
							FastMath.powOf2(x - object.getArea().getCenterX()) + 
							FastMath.powOf2(y - object.getArea().getCenterY());
					if(radiusSquare < distanceSquare)
						continue;
					
//					float distanceSquare = FastMath.powOf2(x - chunk.getX()) + FastMath.powOf2(y - chunk.getY());
					
//					System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));
					
					// TODO: make it strictier:
//					if(radiusSquare >= distanceSquare)
						if(sensor.objectFound(object/*, distanceSquare*/))
							break;
					
					object.getArea().setPassId( passId );
				}

			}
		
		return sensor;
	}
	
	/**
	 * Walks over provided line and feeds the sensor with object whose AABBs it crosses
	 * 
	 * @param sensor
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
	
	public final ISpatialSensor <O> queryLine(ISpatialSensor <O> sensor, float ox, float oy, float dx, float dy)
	{
		int currGridx = toGridIndex(ox);
		int currGridy = toGridIndex(oy);
		float tMaxX, tMaxY;
		float tDeltaX, tDeltaY;
		int stepX, stepY;
		if(dx > 0)
		{
			tMaxX = ((currGridx*cellSize + halfCellSize) - ox) / dx;
			tDeltaX = cellSize / dx;
			stepX = 1;
		}					
		else
		if(dx < 0)
		{
			tMaxX = ((currGridx*cellSize - halfCellSize) - ox) / dx;
			tDeltaX = -cellSize / dx;
			stepX = -1;
		}
		else { tMaxX = Float.MAX_VALUE; tDeltaX = 0; stepX = 0;}
		
		if(dy > 0)
		{
			tMaxY = ((currGridy*cellSize + halfCellSize) - oy) / dy;
			tDeltaY = cellSize / dy;
			stepY = 1;
		}
		else
		if(dy < 0)
		{
			tMaxY = ((currGridy*cellSize - halfCellSize) - oy) / dy;
			tDeltaY = -cellSize / dy;
			stepY = -1;
		}
		else { tMaxY = Float.MAX_VALUE; tDeltaY = 0; stepY = 0;}
		
		// marks entity area to avoid reporting entity multiple times
		int passId = getNextPassId();
		Set <O> cell;
		while(tMaxX <= 1 || tMaxY <= 1)
		{
			if(tMaxX < tMaxY)
			{
				tMaxX += tDeltaX;
				currGridx += stepX;
			}
			else
			{
				tMaxY += tDeltaY;
				currGridy += stepY;
			}
			cell = map[hash(currGridx, currGridy)];
			for(O object : cell)
			{
				if(object.getArea().getPassId() == passId)
					continue;
				
				AABB aabb = object.getArea();
				if(aabb.crosses(ox, oy, dx, dy))
					if(sensor.objectFound(object))
						break;
				
				object.getArea().setPassId( passId );
			}	
		}		
		
		return sensor;
	}
	

	protected final int getNextPassId()
	{
		return ++passId;
	}
	
	public final int toGridIndex(float value)
	{
		return FastMath.round(value * invCellsize);
	}
	
	public final boolean isInvalidIndex(int x, int y)
	{
		return (x < -halfGridWidth || x > halfGridWidth || y < -halfGridHeight || y > halfGridHeight); 
	}
	
}
