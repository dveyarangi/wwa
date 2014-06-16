package eir.world.unit.ai;

import java.util.List;

import yarangi.numbers.RandomUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

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
	private final ListMultimap <String, Order> orders;

	private final ListMultimap <Order, Task> tasks;

	private final UnitsFactory unitsFactory;


	public Scheduler(final UnitsFactory unitsFactory)
	{
		this.unitsFactory = unitsFactory;

		// TODO: those two create wrappers on #get(), consider
		// own implementation to prevent object creation
		this.orders = ArrayListMultimap.create();

		tasks = ArrayListMultimap.create();
	}

	public void addOrder(final String unitType, final Order order)
	{
		orders.put( unitType, order );
	}

	/**
	 * give an ant a task
	 * @param ant identify yourself!
	 * @return
	 */
	public Task gettaTask( final Unit unit, final Task finishedTask )
	{
		if( finishedTask != null)
		{
			taskComplete( finishedTask );
		}

		if(orders.isEmpty())
			return null;

		// TODO: stub; here goes the priority and ant location consideration:


		//
		List <Order> unitOrders = orders.get( unit.getType() );
		if(unitOrders == null || unitOrders.size() == 0)
			return null;

		Order order = null;
		do
		{
			int orderIdx = RandomUtil.N( unitOrders.size() );
			order = unitOrders.get(orderIdx);
		}
		while(!order.isActive());



		Task task = null;

		if(order != null)
		{
			task = order.createTask( this );
		}

		if(task != null)
		{
			tasks.put(order, task);
		}

		return task;
	}

	/**
	 * inform the scheduler that a task was complete
	 * @param task
	 */
	public void taskComplete( final Task task )
	{
		tasks.remove(task.getOrder(), task);
		task.free();
	}

	/**
	 * request that a task be cancelled
	 * @param task
	 */
	public void cancelTask( final Task task )
	{
		task.setCanceled();
		tasks.remove(task.getOrder(), task);
		task.free();
	}

	public void removeOrder(final String unitType, final Order order)
	{
		for(Task task : tasks.get(order))
		{
			task.setCanceled();
			task.free();
		}

		tasks.removeAll(order);

		orders.remove(unitType, order);
	}

	public UnitsFactory getUnitFactory()
	{
		return unitsFactory;
	}

}
