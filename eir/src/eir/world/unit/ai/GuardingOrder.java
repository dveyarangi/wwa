/**
 * 
 */
package eir.world.unit.ai;

import eir.world.environment.nav.NavNode;

/**
 * @author dveyarangi
 *
 */
public class GuardingOrder extends Order
{

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public GuardingOrder(float priority, NavNode targetNode)
	{
		super( priority, null, targetNode );
	}

	@Override
	public Task createTask(Scheduler scheduler)
	{
		return new Task(scheduler, this, new TaskStage [] { TaskStage.GUARD }, true);
	}

}
