/**
 *
 */
package eir.input;

import java.util.ArrayList;
import java.util.List;

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

}
