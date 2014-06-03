/**
 *
 */
package eir.world.unit.ai;

import eir.world.environment.spatial.ISpatialObject;


/**
 * @author dveyarangi
 *
 */
public class AttackingOrder extends Order
{

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public AttackingOrder(final float priority)
	{
		this( priority, null );
	}

	public AttackingOrder(final float priority, final ISpatialObject target)
	{
		super( new TaskStage [] { TaskStage.ATTACK }, false, priority, null, target );
	}

}
