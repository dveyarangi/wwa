/**
 * 
 */
package eir.input;

import eir.world.environment.nav.NavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.ISpatialSensor;

/**
 * @author dveyarangi
 *
 */
public class PickingSensor implements ISpatialSensor<ISpatialObject>
{

	private NavNode pickedNode;

	@Override
	public boolean objectFound(ISpatialObject object)
	{
		if(object instanceof NavNode)
		{
			pickedNode = (NavNode) object;
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
	public NavNode getNode()
	{
		return pickedNode;
	}

}
