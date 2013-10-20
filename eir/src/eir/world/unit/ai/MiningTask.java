package eir.world.unit.ai;


/**
 * a mining task linked to a mining order
 * @author Ni
 *
 */
public class MiningTask extends Task
{
	
	public MiningTask(Scheduler scheduler, Order order)
	{
		super(scheduler, order);
		stage = TaskStage.TRAVEL_TO_TARGET;
	}

	@Override
	public TaskStage nextStage()
	{
		switch (stage)
		{
		case TRAVEL_TO_SOURCE:
			return stage = TaskStage.TRAVEL_TO_TARGET;
			
		case TRAVEL_TO_TARGET:
			return stage = TaskStage.MINING;
			
		case MINING:
			return stage = TaskStage.TRAVEL_TO_SOURCE;
			
		default:
			return null;
		}
	}

}
