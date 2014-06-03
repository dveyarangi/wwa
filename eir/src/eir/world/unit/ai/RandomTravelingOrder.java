package eir.world.unit.ai;

import yarangi.numbers.RandomUtil;
import eir.world.environment.Environment;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.spatial.ISpatialObject;


public class RandomTravelingOrder extends Order
{

	private NavMesh mesh;

	public RandomTravelingOrder(final Environment environment, final float priority) {
		super( new TaskStage[] {
				TaskStage.TRAVEL_TO_SOURCE,
				TaskStage.TRAVEL_TO_TARGET
			},
			false,
			priority, null, null );

		this.mesh = environment.getGroundMesh();
	}


	@Override
	public ISpatialObject getSource()
	{
		return mesh.getNode(RandomUtil.N( mesh.getNodesNum()) );
	}

	@Override
	public ISpatialObject getTarget()
	{
		return mesh.getNode(RandomUtil.N( mesh.getNodesNum()) );
	}

}
