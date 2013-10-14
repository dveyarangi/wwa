package eir.world.unit.ai;

import eir.world.unit.ant.BehaviorFactory.Stage;

public class TravelingTask extends Task 
{
	
	public TravelingTask(Scheduler scheduler, Order order) 
	{
		super(scheduler, order);
		
		this.stage = Stage.TRAVEL_TO_SOURCE;
	}

	@Override
	public Stage nextStage() 
	{
		switch (stage)
		{
		case TRAVEL_TO_SOURCE:
			return stage = Stage.TRAVEL_TO_TARGET;
			
		case TRAVEL_TO_TARGET:
			status = Status.COMPLETED;
			return stage = null;
			
		default:
			status = Status.CANCELED;
			return stage = null;
		}
	}

}
