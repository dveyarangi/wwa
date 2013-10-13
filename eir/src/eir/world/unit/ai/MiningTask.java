package eir.world.unit.ai;

import eir.world.unit.ant.BehaviorFactory.Stage;

/**
 * a mining task linked to a mining order
 * @author Ni
 *
 */
public class MiningTask extends Task
{
	private Stage stage;
	
	public MiningTask(Scheduler scheduler, Order order)
	{
		super(scheduler, order);
		stage = Stage.TRAVEL_TO_TARGET;
	}

	@Override
	public Stage nextStage()
	{
		switch (stage)
		{
		case TRAVEL_TO_SOURCE:
			return stage = Stage.TRAVEL_TO_TARGET;
			
		case TRAVEL_TO_TARGET:
			return stage = Stage.MINING;
			
		case MINING:
			return stage = Stage.TRAVEL_TO_SOURCE;
			
		default:
			return null;
		}
	}

}
