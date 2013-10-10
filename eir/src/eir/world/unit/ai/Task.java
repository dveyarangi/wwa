package eir.world.unit.ai;


import eir.world.unit.ant.BehaviorFactory;
import eir.world.unit.ant.BehaviorFactory.Stage;


/**
 * task tell an ant what it is supposed to do.
 * @author Ni
 *
 */
public abstract class Task
{
	protected final Scheduler scheduler;
	protected final Order order;
	protected Stage stage;
	
	public static enum Status { ONGOING, COMPLETED, CANCELED };
	
	private Status status;
	
	public Task(Scheduler scheduler, Order order)
	{
		this.scheduler = scheduler;
		this.order = order;
		
		this.status = Status.ONGOING;
	}
	
	public abstract Stage nextStage();
	
	public void cancel() 
	{
		scheduler.cancelTask( this );
		
	}

	public Order getOrder() { return order; }

	void setCanceled() 
	{
		status = Status.CANCELED;
	}
	
	public boolean isFinished()
	{
		return status == Status.CANCELED || status == Status.COMPLETED;
	}

	public TaskBehavior getBehavior() {
		return BehaviorFactory.getBehavior( stage );
	}
	
	
}
