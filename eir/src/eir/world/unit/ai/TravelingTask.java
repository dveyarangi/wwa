package eir.world.unit.ai;


public class TravelingTask extends Task 
{
	
	public TravelingTask(Scheduler scheduler, Order order) 
	{
		super(scheduler, order);
		
		this.stage = TaskStage.TRAVEL_TO_SOURCE;
	}

	@Override
	public TaskStage nextStage() 
	{
		switch (stage)
		{
		case TRAVEL_TO_SOURCE:
			return stage = TaskStage.TRAVEL_TO_TARGET;
			
		case TRAVEL_TO_TARGET:
			status = Status.COMPLETED;
			return stage = null;
			
		default:
			status = Status.CANCELED;
			return stage = null;
		}
	}

}
