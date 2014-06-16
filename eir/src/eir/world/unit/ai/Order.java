package eir.world.unit.ai;

import eir.world.environment.spatial.ISpatialObject;

/**
 * general order given by user or ai controller
 * @author Ni
 *
 */
public abstract class Order
{
	private boolean isActive = true;

	private String unitType;

	private ISpatialObject source;
	private ISpatialObject target;

	private float priority;

	private TaskStage [] stages;

	private boolean cycleStages;

	public Order(final TaskStage [] stages, final boolean cycleStages, final float priority, final ISpatialObject source, final ISpatialObject target)
	{
		this.stages = stages;
		this.cycleStages = cycleStages;

		this.priority = priority;

		this.source = source;
		this.target = target;

	}

	public Task createTask(final Scheduler scheduler)
	{
		return Task.create( scheduler, this );
	}

	/**
	 * @return
	 */
	public ISpatialObject getSource() { return source;	}
	public ISpatialObject getTarget() { return target;	}

	public void setActive(final boolean isActive) { this.isActive = isActive; }

	public float getPriority() { return priority; }
	public String getUnitType() { return unitType; }

	public boolean isActive() { return isActive; }

	public TaskStage[] getStages() { return stages; }

	public boolean cycleTask() { return cycleStages; }

}
