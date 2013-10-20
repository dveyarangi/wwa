/**
 * 
 */
package eir.world.unit.ai;

/**
 * @author dveyarangi
 *
 */
public class GuardingTask extends Task
{

	/**
	 * @param scheduler
	 * @param order
	 */
	public GuardingTask(Scheduler scheduler, Order order)
	{
		super( scheduler, order );
		
		this.stage = TaskStage.GUARD;
	}

	@Override
	public TaskStage nextStage()
	{
		return TaskStage.GUARD;
	}

}
