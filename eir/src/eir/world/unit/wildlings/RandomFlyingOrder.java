/**
 * 
 */
package eir.world.unit.wildlings;

import eir.world.environment.nav.NavNode;
import eir.world.unit.ai.Order;
import eir.world.unit.ai.Scheduler;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public class RandomFlyingOrder extends Order
{

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public RandomFlyingOrder(float priority, NavNode sourceNode, NavNode targetNode)
	{
		super( priority, sourceNode, targetNode );
	}

	
	@Override
	public Task createTask(Scheduler scheduler)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
