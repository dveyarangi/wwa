package eir.debug;

import java.util.Set;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;

import yarangi.math.FastMath;

/**
 * Renders entity index
 * 
 * @author dveyarangi
 *
 */
public class SpatialHashMapLook
{
	
	private SpatialHashMap <ISpatialObject> map;
	
	public SpatialHashMapLook(SpatialHashMap <ISpatialObject> map)
	{
		this.map = map;
	}
	
	public void draw(ShapeRenderer renderer) 
	{
		int cellX, cellY;
		float cellsize = (float)map.getCellSize();
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
		
		Set <ISpatialObject> bucket = null;
		for(float y = miny; y <= maxy; y += map.getCellSize())
		{
			cellY = FastMath.round(y / map.getCellSize());
			for(float x = minx; x < maxx; x += map.getCellSize())
			{
				cellX = FastMath.round(x / map.getCellSize());
				boolean isReal = false;
				try {
					bucket = map.getBucket(cellX, cellY);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					
				}
//				if(bucket.size() > 0)
				if(bucket != null)
				for(ISpatialObject chunk : bucket)
				{
//					System.out.println(chunk);
//					if(chunk.overlaps(x, y, x+map.getCellSize(), y+map.getCellSize()))
					{
						isReal = true;
						break;
					}
				}
				if(bucket == null) {
					renderer.setColor(1f, 0f, 0.0f, 0.5f);
					renderer.begin( ShapeType.Line );
					renderer.line(x, y, x+cellsize, y+cellsize);

					renderer.line(x, y+cellsize, x+cellsize, y);
					renderer.end();


				}
				else
				if(bucket.size() != 0)
				{
					if(isReal)		
						renderer.setColor(0.8f, 0.6f, 0.8f, 0.2f);
					else
						renderer.setColor(0.1f, 0.6f, 0.8f, 0.1f);
					renderer.begin( ShapeType.Rectangle );
					renderer.rect( x, y, cellsize, cellsize);
					renderer.end();
				}	
			}
		}
	}

}
