package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import eir.world.unit.ai.TaskStage;

public class BehaviorFactory <U extends Unit>
{


	private Map <TaskStage, UnitBehavior <U>> behaviors = new HashMap <TaskStage, UnitBehavior<U>> ();

	protected void registerBehavior(final TaskStage stage, final UnitBehavior <U> behavior)
	{
		behaviors.put( stage, behavior );
	}

	public UnitBehavior <U> getBehavior(final TaskStage stage)
	{
		return behaviors.get(stage);
	}
}
