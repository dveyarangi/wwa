/**
 *
 */
package eir.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialFilter;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.ISpatialSensor;

/**
 * This is processor for mouse picking query;
 *
 * Uses filter to determine whether the object should be picked
 *
 * @author dveyarangi
 */
public class PickingSensor implements ISpatialSensor <ISpatialObject>
{

	private List <ISpatialObject> pickedObjects;

	private ISpatialFilter <ISpatialObject> filter;

	// cursor coordinates:
	private float cx, cy;

	public PickingSensor( final ISpatialFilter <ISpatialObject> filter)
	{
		this.filter = filter;
		pickedObjects = new ArrayList <ISpatialObject> ();
	}

	@Override
	public boolean objectFound(final ISpatialObject object)
	{
		if(filter.accept( object ))
		{
			if(object.getArea().contains( cx, cy ))
				pickedObjects.add( object );
			return false;
		}

		return false;
	}

	@Override
	public void clear()
	{
		pickedObjects.clear();;
	}

	/**
	 * @return
	 */
	public List <ISpatialObject> getPickedObjects()
	{
		return pickedObjects;
	}

	public void setCursor( final Vector2 pointerPosition2 )
	{
		this.cx = pointerPosition2.x;
		this.cy = pointerPosition2.y;
	}

}
