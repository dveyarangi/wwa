package eir.world.unit.ai;

import eir.world.environment.NavNode;

/**
 * general order given by user or ai controller
 * @author Ni
 *
 */
public abstract class Order
{

	private float priority;
	
	public Order(float priority)
	{
		this.priority = priority;
	}
	
	public abstract Task createTask(Scheduler scheduler);
}
