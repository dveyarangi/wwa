package eir.world.unit.ant;

import java.util.HashMap;
import java.util.Map;

import eir.world.unit.ai.TaskBehavior;

public class BehaviorFactory {
	public static enum Stage
	{
		TRAVEL_TO,
		TRAVEL_FROM,
		HARVEST,
		UNLOAD,
		FARM,
		GUARD
	}
	
	private static Map <Stage, TaskBehavior> behaviors = new HashMap <Stage, TaskBehavior> ();
	
	static {
		behaviors.put(Stage.TRAVEL_TO,   new TravelingBehavior());
		behaviors.put(Stage.TRAVEL_FROM, new TravelingBehavior());
	}
	
	public static TaskBehavior getBehavior(Stage stage)
	{
		return behaviors.get(stage);
	}
}
