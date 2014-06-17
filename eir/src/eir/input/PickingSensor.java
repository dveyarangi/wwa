/**
 *
 */
package eir.input;

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

	private ISpatialObject pickedObject;

	private ISpatialFilter <ISpatialObject> filter;

	public PickingSensor( final ISpatialFilter <ISpatialObject> filter)
	{
		this.filter = filter;
	}

	@Override
	public boolean objectFound(final ISpatialObject object)
	{
		if(filter.accept( object ))
		{
			pickedObject = object;
			return true;
		}

		return false;
	}

	@Override
	public void clear()
	{
		pickedObject = null;
	}

	/**
	 * @return
	 */
	public ISpatialObject getPickedObject()
	{
		return pickedObject;
	}

}
