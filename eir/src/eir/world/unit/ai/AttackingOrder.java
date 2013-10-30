/**
 * 
 */
package eir.world.unit.ai;

import eir.world.unit.Unit;


/**
 * @author dveyarangi
 *
 */
public class AttackingOrder extends Order
{
	private Unit unit;
	
	public float timeout = 10;

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public AttackingOrder(float priority)
	{
		super( priority, null, null, null );
	}

	@Override
	public Task createTask(Scheduler scheduler)
	{
		return new Task(scheduler, this, new TaskStage [] { TaskStage.ATTACK }, false);
	}


}
