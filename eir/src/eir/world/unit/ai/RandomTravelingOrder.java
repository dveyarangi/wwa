package eir.world.unit.ai;

import yarangi.numbers.RandomUtil;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;


public class RandomTravelingOrder extends Order 
{
	
	private NavMesh mesh;

	public RandomTravelingOrder(NavMesh mesh, float priority) {
		super(priority, null, null);
		
		this.mesh = mesh;
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
