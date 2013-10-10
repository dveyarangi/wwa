package eir.world.unit.ai;


public class RandomTravelingOrder extends Order 
{

	public RandomTravelingOrder(float priority) {
		super(priority);
	}

	@Override
	public Task createTask(Scheduler scheduler) {
		return new TravelingTask(scheduler, this);
	}

}
