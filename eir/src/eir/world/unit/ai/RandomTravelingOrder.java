package eir.world.unit.ai;

import yarangi.numbers.RandomUtil;
import eir.world.environment.Environment;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;


public class RandomTravelingOrder extends Order 
{
	
	private NavMesh mesh;

	public RandomTravelingOrder(Environment environment, float priority) {
		super(priority, null, null, null);
		
		this.mesh = environment.getGroundMesh();
	}

	@Override
	public Task createTask(Scheduler scheduler) {
		return new Task(scheduler, 
						this, 
						new TaskStage[] { 
							TaskStage.TRAVEL_TO_SOURCE,
							TaskStage.TRAVEL_TO_TARGET
						},
						false
		);
	}
	
	@Override
	public NavNode getSourceNode() 
	{ 
		return mesh.getNode(RandomUtil.N( mesh.getNodesNum()) );
	}
	
	@Override
	public NavNode getTargetNode()
	{
		return mesh.getNode(RandomUtil.N( mesh.getNodesNum()) );
	}

}
