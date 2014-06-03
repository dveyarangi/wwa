package eir.debug;

import java.util.List;

import yarangi.math.FastMath;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;

/**
 * Renders entity index
 *
 * @author dveyarangi
 *
 */
public class SpatialHashMapLook
{

	private final SpatialHashMap <ISpatialObject> map;

	public SpatialHashMapLook(final SpatialHashMap <ISpatialObject> map)
	{
		this.map = map;
	}

	public void draw(final ShapeRenderer renderer)
	{
		int cellX, cellY;
		float cellsize = map.getCellSize();
		float halfCellSize = cellsize / 2.f;
		float minx = -map.getWidth()/2-halfCellSize;
		float maxx = map.getWidth()/2-halfCellSize;
		float miny = -map.getHeight()/2-halfCellSize;
		float maxy = map.getHeight()/2-halfCellSize;
		renderer.setColor(0f, 0f, 0.4f, 0.5f);
		renderer.begin( ShapeType.Line );
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			renderer.line( minx, y, maxx, y);
		}

		for(float x = minx; x <= maxx; x += map.getCellSize())
		{
			renderer.line( x, miny,x, maxy);

		}

		renderer.end();

		List <ISpatialObject> bucket = null;
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			cellY = FastMath.round(y / map.getCellSize());
			for(float x = minx; x < maxx; x += map.getCellSize())
			{
				cellX = FastMath.round(x / map.getCellSize());
				try {
					bucket = map.getBucket(cellX, cellY);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}

				if(bucket != null && bucket.size() != 0)
				{
					boolean isReal = false;
					ISpatialObject o = null;
					for(int idx = 0; idx < bucket.size(); idx ++)
					{
						o = bucket.get(idx);
						if(o.getArea().overlaps( x, y, x+cellsize, y+cellsize ))
						{
							isReal = true;
							break;
						}
					}
					if(isReal)
					{
						renderer.setColor(0.8f, 0.6f, 0.8f, 0.2f);
						renderer.begin( ShapeType.FilledRectangle );
						renderer.filledRect( x, y, cellsize, cellsize);
						renderer.end();
					}
				}
			}
		}
	}

}
