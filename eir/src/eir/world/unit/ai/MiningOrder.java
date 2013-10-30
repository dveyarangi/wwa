package eir.world.unit.ai;

import eir.world.environment.nav.NavNode;


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
	public MiningOrder(float priority, NavNode sourceNode, NavNode targetNode)
	{
		super(priority, sourceNode, targetNode);
	}

	@Override
	public Task createTask(Scheduler scheduler)
	{
		return new Task(scheduler, 
				this, 
				new TaskStage[] { 
					TaskStage.TRAVEL_TO_TARGET,
					TaskStage.MINING,
				},
				false
			);
	}

}
