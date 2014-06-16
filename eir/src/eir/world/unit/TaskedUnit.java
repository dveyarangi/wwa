package eir.world.unit;

import eir.world.unit.ai.Task;

public abstract class TaskedUnit extends Unit
{

	private Task task;



	@Override
	protected void init()
	{
		super.init();
		if(task != null)
			throw new IllegalStateException("Cannot reset tasked unit: task not cleared.");
	}

	@Override
	public void update( final float delta )
	{
		super.update( delta );

		if(task == null || task.isFinished())
		{
			// requesting a new task:
			task = faction.getScheduler().gettaTask( this, task );
			if(task == null)
				return;
		}

		// performing task:
		UnitBehavior behavior = task.getBehavior( this );
		if( behavior == null)
			return;

		behavior.update( delta, task, this );


	}

	@Override
	public float hit(final Damage source, final IDamager damager, final float damageCoef)
	{
		float damage = super.hit( source, damager, damageCoef );

		if( !this.isAlive() && task != null  )
		{
			task.cancel();
			task = null;
		}

		return damage;
	}

	@Override
	public void dispose()
	{
		super.dispose();
		if(task != null)
		{
			task.cancel();
		}

		task = null;

	}
}
