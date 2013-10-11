package eir.world.unit.ai;

import eir.world.environment.NavNode;

/**
 * general order given by user or ai controller
 * @author Ni
 *
 */
public abstract class Order
{
	
	private NavNode sourceNode;
	private NavNode targetNode;

	private float priority;
	
	public Order(float priority, NavNode sourceNode, NavNode targetNode)
	{
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.priority = priority;
	}
	
	public abstract Task createTask(Scheduler scheduler);

	/**
	 * @return
	 */
	public NavNode getSourceNode() { return sourceNode;	}
	public NavNode getTargetNode() { return targetNode;	}
}
