package eir.world.unit.ai;

import eir.world.unit.UnitBehavior;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;




/**
 * task tell an ant what it is supposed to do.
 * @author Ni
 *
 */
public class Task
{
	protected final Scheduler scheduler;
	protected final Order order;
	protected TaskStage stage;
	
	public static enum Status { ONGOING, COMPLETED, CANCELED };
	
	protected Status status;
	
	protected TaskStage [] stages;
	protected boolean cycle = false;
	protected int stageIdx = 0;
	
	
	public Task(Scheduler scheduler, Order order, TaskStage [] stages, boolean cycle)
	{

		this.scheduler = scheduler;
		this.order = order;
		
		this.stages = stages;
		this.stage = stages[stageIdx ++];
		this.cycle = cycle;
		
		this.status = Status.ONGOING;
	}
	
	public TaskStage nextStage()
	{
		if(stageIdx >= stages.length)
		{
			if(!cycle)
			{
				this.setCompleted();
				return null;
			}
			
			stageIdx = 0;
		}
		
		return stages [stageIdx ++];
	}
	
	public void cancel() 
	{
		scheduler.cancelTask( this );
		
	}

	public Order getOrder() 
	{ 
		return order; 
	}

	public void setCanceled() 
	{
		status = Status.CANCELED;
	}
	
	public void setCompleted()
	{
		status = Status.COMPLETED;
	}
	
	public boolean isFinished()
	{
		return status == Status.CANCELED || status == Status.COMPLETED;
	}

	/**
	 * @return
	 */
	public <U extends Unit> UnitBehavior <U> getBehavior(Unit unit)
	{
		return (UnitBehavior<U>) UnitsFactory.getBehaviorFactory( unit.getType() ).getBehavior( stage );
	}

}
