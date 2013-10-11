package eir.world.unit.ai;

import yarangi.numbers.RandomUtil;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;


public class RandomTravelingOrder extends Order 
{
	
	private NavMesh mesh;

	public RandomTravelingOrder(NavMesh mesh, float priority) {
		super(priority, null, null);
		
		this.mesh = mesh;
	}

	@Override
	public Task createTask(Scheduler scheduler) {
		return new TravelingTask(scheduler, this);
	}
	
	public NavNode getSourceNode() 
	{ 
		return mesh.getNode( RandomUtil.N( mesh.getNodesNum() ) );
	}

}
