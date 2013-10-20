package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import eir.world.unit.ai.TaskStage;

public class BehaviorFactory <U extends Unit>
{

	
	private Map <TaskStage, UnitBehavior <U>> behaviors = new HashMap <TaskStage, UnitBehavior<U>> ();
	
	public void registerBehavior(TaskStage stage, UnitBehavior <U> behavior)
	{
		behaviors.put( stage, behavior );
	}
	
	public UnitBehavior <U> getBehavior(TaskStage stage)
	{
		return behaviors.get(stage);
	}
}
