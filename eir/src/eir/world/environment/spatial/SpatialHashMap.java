package eir.world.environment.spatial;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import yarangi.math.FastMath;

/**
 * Straightforward implementation of spatial hash map.
 *
 * Note: cannot be used in multi-threaded environment, due to passId optimization (and lack of any type of synchronization).
 * @param <O>
 */
public class SpatialHashMap <O extends ISpatialObject> extends Grid<List <O>> implements ISpatialIndex <O>
{
	/**
	 * This is cache of object location;
	 * For each indexed object, we keep copy of it's AABB, so when object asks for AABB
	 * update, the hashmap can fix buckets accordingly.
	 */
	private final TIntObjectHashMap<AABB> aabbs = new TIntObjectHashMap<AABB> ();

	/**
	 * Used by query methods to mark tested objects and avoid result duplication;
	 * thusly permits only single threaded usage.
	 */
	private int passId;

	public SpatialHashMap(final String name, final float cellSize, final float width, final float height)
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
	public SpatialHashMap(final String name, final int size, final float cellSize, final float width, final float height)
	{
		super(name, size, cellSize, width, height);
	}


	@Override
	public List<O> [] createGrid()
	{

		List [] map = new List[size];
		for(int idx = 0; idx < size; idx ++)
		{
			map[idx] = new ArrayList <O> ();
		}

		return map;
	}

	/**
	 * Retrieves content of the bucket that holds the contents of (x,y) cell.
	 * Result may contain data from other cells as well.
	 * @param x
	 * @param y
	 * @return
	 */
	public final List <O> getBucket(final int x, final int y)
	{
		return map[index(x, y)];
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
	@Override
	protected final int index(final int x, final int y)
	{
		return ((x+halfGridWidth)*6184547 + (y+halfGridHeight)* 2221069) % size;
	}

	/**
	 * Adds spatial object to the indexer.
	 * @param object
	 */
	@Override
	public void add(final O object)
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
	@Override
	public O remove(final O object)
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
	@Override
	public void update(final O object)
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
	private void iterateOverAABB( final AABB aabb, final boolean remove, final O object)
	{
		iterateOverAABB( aabb.cx(), aabb.cy(), aabb.rx(), aabb.ry(), remove, object );
	}

	private void iterateOverAABB( final float cx, final float cy, final float rx, final float ry, final boolean remove, final O object)
	{

		int minIdxx = Math.max(toGridIndex(cx - rx), -halfGridWidth);
		int minIdxy = Math.max(toGridIndex(cy - ry), -halfGridHeight);
		int maxIdxx = Math.min(toGridIndex(cx + rx),  halfGridWidth);
		int maxIdxy = Math.min(toGridIndex(cy + ry),  halfGridHeight);

		int currx, curry;
		int hash;

		List <O> cell;

		for(currx = minIdxx; currx <= maxIdxx; currx ++)
		{
			for(curry = minIdxy; curry <= maxIdxy; curry ++)
			{
//				System.out.println(minx + " " + miny + " " + maxx + " " + maxy);
				hash = index(currx, curry);

				if(hash < 0)
				{
					continue;
				}

				cell = map[hash];
				if(remove)
				{
					cell.remove( object );
				} else
				{
					cell.add(object);
				}
			}
		}
	}

	/**
	 * Walks over provided AABB and feeds the sensor with object whose AABBs overlap it
	 */
	@Override
	public ISpatialSensor <O> queryAABB(final ISpatialSensor <O> sensor, final AABB aabb)
	{
		return queryAABB( sensor, aabb.cx(), aabb.cy(), aabb.rx(), aabb.ry() );
	}

	/**
	 * Walks over provided AABB and feeds the sensor with object whose AABBs overlap it
	 */
	@Override
	public ISpatialSensor <O> queryAABB(final ISpatialSensor <O> sensor, final float cx, final float cy, final float rx, final float ry)
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

		List <O> cell;
		AABB objectArea;
		O obj = null;

		found: for(currx = minIdxx; currx <= maxIdxx; currx ++)
		{
			for(curry = minIdxy; curry <= maxIdxy; curry ++)
			{
//				System.out.println(minx + " " + miny + " " + maxx + " " + maxy);
				hash = index(currx, curry);

				if(hash < 0)
				{
					continue;
				}
				cell = map[hash];

				for(int idx = 0; idx < cell.size(); idx ++ )
				{
					obj = cell.get( idx );
					objectArea = obj.getArea();
					if(objectArea.getPassId() == passId)
					{
						continue;
					}
					if(objectArea.overlaps( minx, miny, maxx, maxy ))
						if(sensor.objectFound(obj))
						{
							break found;
						}
					objectArea.setPassId( passId );
				}
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
	@Override
	public final ISpatialSensor <O> queryRadius(final ISpatialSensor <O> sensor, final float x, final float y, final float radius)
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
		List <O> cell;
		AABB objectArea;
		O object = null;
		for(int tx = minx; tx <= maxx; tx ++)
		{
			for(int ty = miny; ty <= maxy; ty ++)
			{
				cell = map[index(tx, ty)];

				float distanceSquare = FastMath.powOf2(x - tx*cellSize) + FastMath.powOf2(y - ty*cellSize);
				if(radiusSquare < distanceSquare)
				{
					continue;
				}

//				System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));

				// TODO: make it strictier:
				for(int idx = 0; idx < cell.size(); idx ++ )
				{
					object = cell.get( idx );
					objectArea = object.getArea();
					if(objectArea.getPassId() == passId)
					{
						continue;
					}

					distanceSquare =
							FastMath.powOf2(x - objectArea.getCenterX()) +
							FastMath.powOf2(y - objectArea.getCenterY());
					if(radiusSquare < distanceSquare)
					{
						continue;
					}

//					float distanceSquare = FastMath.powOf2(x - chunk.getX()) + FastMath.powOf2(y - chunk.getY());

//					System.out.println(aabb.r+radius + " : " + Math.sqrt(distanceSquare));

					// TODO: make it strictier:
//					if(!object.getArea().overlaps( x, y, x, y ))
//						continue;
//					if(radiusSquare >= distanceSquare)
						if(sensor.objectFound(object/*, distanceSquare*/))
						{
							break;
						}

					object.getArea().setPassId( passId );
				}

			}
		}

		return sensor;
	}

	@Override
	public O findClosest( final ISpatialFilter <ISpatialObject> filter, final float x, final float y )
	{
		int minx = Math.min(toGridIndex(x),  halfGridWidth);
		int miny = Math.min(toGridIndex(y),  halfGridHeight);
		int passId = getNextPassId();

		float closestDistanceSquare = Float.MAX_VALUE;
		float squareRadius = 0;
		float distanceSquare = 0;
		List <O> cell;

		O closestNeighbor = null;
		cell = map[index(minx, miny)];
		O object = null;
		AABB objectArea;

		// TODO: make it strictier:
		for(int idx = 0; idx < cell.size(); idx ++ )
		{
			object = cell.get( idx );
			objectArea = object.getArea();
			if(objectArea.getPassId() == passId)
			{
				continue;
			}

			if( ! filter.accept( object ) )
			{
				continue;
			}

			distanceSquare =
					FastMath.powOf2(x - objectArea.getCenterX()) +
					FastMath.powOf2(y - objectArea.getCenterY());

			if(distanceSquare < closestDistanceSquare)
			{
				closestDistanceSquare = distanceSquare;
				closestNeighbor = object;
			}

			object.getArea().setPassId( passId );


		}

		int tx, ty;

		spiral: for(int radius = 0; radius < halfGridWidth*2; radius ++)
		{
			int xdelta = 1, ydelta = 0;

			tx = minx-radius;
			ty = miny-radius;

			for(int dir = 0; dir < 4; dir ++)
			{
				for(int idx = -radius; idx <= radius; idx ++)
				{

					cell = map[index(tx+xdelta*idx, ty+ydelta*idx)];

					for(idx = 0; idx < cell.size(); idx ++ )
					{
						object = cell.get( idx );
						objectArea = object.getArea();
						if(objectArea.getPassId() == passId)
						{
							continue;
						}

						if(!objectArea.overlaps( x, y, x, y ))
						{
							continue;
						}

						if( ! filter.accept( object ) )
						{
							continue;
						}

						distanceSquare =
								FastMath.powOf2(x - objectArea.getCenterX()) +
								FastMath.powOf2(y - objectArea.getCenterY());

						if(distanceSquare < closestDistanceSquare)
						{
							closestDistanceSquare = distanceSquare;
							closestNeighbor = object;
							break spiral;
						}

						object.getArea().setPassId( passId );
					}

				}

				int td = ydelta;
				ydelta = xdelta;
				xdelta = -td;

			}
		}

		// TODO: optimize, following is slower than possible

		if( closestNeighbor == null)
			return null;

		ClosestNeighborSensor sensor =  new ClosestNeighborSensor(x, y);
		queryRadius( sensor, x, y, (float)Math.sqrt( closestDistanceSquare ) );

		return sensor.getClosestFound();
	}

	/**
	 * Stores in his belly the point closest to coordinates specified in ctor.
	 * @author Fima
	 *
	 */
	private class ClosestNeighborSensor implements ISpatialSensor <O>
	{
		private O closestObject = null;
		private float closestDistance = Float.MAX_VALUE;
		private final float x, y;

		ClosestNeighborSensor(final float x, final float y)
		{
			this.x = x; this.y = y;
		}

		public O getClosestFound() { return closestObject; }

		@Override
		public boolean objectFound( final O object )
		{
			float dSqrt = FastMath.powOf2(object.getArea().getCenterX()-x) +
						  FastMath.powOf2(object.getArea().getCenterY()-y);
			if(dSqrt < closestDistance)
			{
				closestDistance = dSqrt;
				closestObject = object;
			}
			return false;
		}

		@Override
		public void clear()
		{
			closestObject = null;
			closestDistance = Float.MAX_VALUE;
		}
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

	public final ISpatialSensor <O> queryLine(final ISpatialSensor <O> sensor, final float ox, final float oy, final float dx, final float dy)
	{
		int currGridx = toGridIndex(ox);
		int currGridy = toGridIndex(oy);
		float tMaxX, tMaxY;
		float tDeltaX, tDeltaY;
		int stepX, stepY;

		if(dx > 0)
		{
			tMaxX = (currGridx*cellSize + halfCellSize - ox) / dx;
			tDeltaX = cellSize / dx;
			stepX = 1;
		}
		else
		if(dx < 0)
		{
			tMaxX = (currGridx*cellSize - halfCellSize - ox) / dx;
			tDeltaX = -cellSize / dx;
			stepX = -1;
		}
		else { tMaxX = Float.MAX_VALUE; tDeltaX = 0; stepX = 0;}

		if(dy > 0)
		{
			tMaxY = (currGridy*cellSize + halfCellSize - oy) / dy;
			tDeltaY = cellSize / dy;
			stepY = 1;
		}
		else
		if(dy < 0)
		{
			tMaxY = (currGridy*cellSize - halfCellSize - oy) / dy;
			tDeltaY = -cellSize / dy;
			stepY = -1;
		}
		else { tMaxY = Float.MAX_VALUE; tDeltaY = 0; stepY = 0;}

		// marks entity area to avoid reporting entity multiple times
		int passId = getNextPassId();
		List <O> cell;
		O object = null;
		AABB objectArea;
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
			cell = map[index(currGridx, currGridy)];

			for(int idx = 0; idx < cell.size(); object = cell.get( idx ++ ))
			{
				objectArea = object.getArea();
				if(objectArea.getPassId() == passId)
				{
					continue;
				}

				AABB aabb = object.getArea();
				if(aabb.crosses(ox, oy, dx, dy))
					if(sensor.objectFound(object))
					{
						break;
					}

				objectArea.setPassId( passId );
			}
		}

		return sensor;
	}


	protected final int getNextPassId()
	{
		return ++passId;
	}



}
