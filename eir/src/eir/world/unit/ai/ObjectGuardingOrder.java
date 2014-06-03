/**
 * 
 */
package eir.world.unit.ai;


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
	public GuardingOrder(float priority)
	{
		super( priority, null, null, null );
	}

	@Override
	public Task createTask(Scheduler scheduler)
	{
		return new Task( scheduler, this, new TaskStage [] { TaskStage.GUARD }, false );
	}

}
