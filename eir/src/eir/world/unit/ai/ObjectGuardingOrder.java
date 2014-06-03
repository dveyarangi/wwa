/**
 *
 */
package eir.world.unit.ai;

import eir.world.unit.Unit;


/**
 * @author dveyarangi
 *
 */
public class ObjectGuardingOrder extends Order
{

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public ObjectGuardingOrder(final float priority, final Unit target)
	{
		super( new TaskStage [] { TaskStage.GUARD }, false, priority, null, target );
	}

}
