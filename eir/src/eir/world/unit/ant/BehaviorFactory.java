package eir.world.unit.ant;

import java.util.HashMap;
import java.util.Map;

import eir.world.unit.ai.TaskBehavior;

public class BehaviorFactory {
	public static enum Stage
	{
		TRAVEL_TO_SOURCE,
		TRAVEL_TO_TARGET,
		HARVEST,
		UNLOAD,
		FARM,
		GUARD
	}
	
	private static Map <Stage, TaskBehavior> behaviors = new HashMap <Stage, TaskBehavior> ();
	
	static {
		behaviors.put(Stage.TRAVEL_TO_SOURCE, new TravelingBehavior.TravelToSourceBehavior());
		behaviors.put(Stage.TRAVEL_TO_TARGET, new TravelingBehavior.TravelToTargetBehavior());
	}
	
	public static TaskBehavior getBehavior(Stage stage)
	{
		return behaviors.get(stage);
	}
}
