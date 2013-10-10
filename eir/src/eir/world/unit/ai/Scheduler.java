package eir.world.unit.ai;

import java.util.LinkedList;
import java.util.List;

import yarangi.numbers.RandomUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import eir.world.Level;
import eir.world.unit.ant.Ant;

/**
 * schedules tasks for ant <br>
 * its a hive mind yo
 * @author Ni
 *
 */
public class Scheduler
{
	
	private Level level;
	
	
	private List <Order> orders;
	
	private Multimap <Order, Task> tasks;
	
	
	public Scheduler(Level level)
	{
		this.level = level;
		
		this.orders = new LinkedList <Order> (); 
		
		tasks = HashMultimap.create();
	}

	public void addOrder(Order order) 
	{
		orders.add( order );
	}	
	
	/**
	 * give an ant a task 
	 * @param ant identify yourself!
	 * @return
	 */
	public Task gettaTask( Ant ant )
	{
		if(orders.isEmpty())
			return null;
		
		// TODO: stub; here goes the priority and ant location consideration:
		Order order = orders.get(  RandomUtil.N(orders.size() ) );
		
		
		Task task = order.createTask( this );
		
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
