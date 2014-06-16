/**
 *
 */
package eir.input;

import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.ISpatialSensor;

/**
 * @author dveyarangi
 *
 */
public class PickingSensor implements ISpatialSensor<ISpatialObject>
{

	private SurfaceNavNode pickedNode;

	@Override
	public boolean objectFound(final ISpatialObject object)
	{
		if(object instanceof SurfaceNavNode)
		{
			pickedNode = (SurfaceNavNode) object;
		}

		return false;
	}

	@Override
	public void clear()
	{
		pickedNode = null;
	}

	/**
	 * @return
	 */
	public SurfaceNavNode getNode()
	{
		return pickedNode;
	}

}
