package eir.world.unit.ai;

import eir.world.environment.spatial.ISpatialObject;


/**
 * an order to mine material at target and bring it back to source
 * @author Ni
 *
 */
public class MiningOrder extends Order
{

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public MiningOrder(final float priority, final ISpatialObject source, final ISpatialObject target)
	{
		super(new TaskStage[] {
				TaskStage.TRAVEL_TO_TARGET,
				TaskStage.MINING,
			},
			false,
			priority, source, target);
	}

}
