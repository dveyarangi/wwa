package eir.world.unit.ai;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import eir.world.Level;
import eir.world.unit.Unit;

/**
 * schedules tasks for ant <br>
 * its a hive mind yo
 * @author Ni
 *
 */
public class Scheduler
{
		
	/**
	 * Orders by unit types?
	 */
	private Multimap <String, Order> orders;
	
	private Multimap <Order, Task> tasks;
	
	
	public Scheduler()
	{
		this.orders = HashMultimap.create(); 
		
		tasks = HashMultimap.create();
	}

	public void addOrder(String unitType, Order order) 
	{
		orders.put( unitType, order );
	}	
	
	/**
	 * give an ant a task 
	 * @param ant identify yourself!
	 * @return
	 */
	public Task gettaTask( Unit unit )
	{
		if(orders.isEmpty())
			return null;
		
		// TODO: stub; here goes the priority and ant location consideration:
		
		
		// 
		Collection <Order> unitOrders = orders.get( unit.getType() );
		if(unitOrders == null)
			return null;
		
		Order order = null;
		for(Order aOrder : unitOrders)
		{
			if(aOrder.isActive())
			{
				order = aOrder;
				break;
			}
		}
		
		Task task = null;
		
		if(order != null)
			task = order.createTask( this );
		
		if(task != null)
			tasks.put(order, task);
		
		return task;
	}
	
	/**
	 * inform the scheduler that a task was complete
	 * @param task
	 */
	public void taskComplete( Task task )
	{
		tasks.remove(task.getOrder(), task);
	}
	
	/**
	 * request that a task be cancelled
	 * @param task
	 */
	public void cancelTask( Task task )
	{
		task.setCanceled();
		tasks.remove(task.getOrder(), task);
	}

}
