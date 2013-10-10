package eir.world.unit.ai;

import eir.world.unit.ant.BehaviorFactory.Stage;

public class TravelingTask extends Task 
{
	
	public TravelingTask(Scheduler scheduler, Order order) {
		super(scheduler, order);
		
		this.stage = Stage.TRAVEL_TO;
	}

	@Override
	public Stage nextStage() {
		stage = null;
		return null;
	}

}
