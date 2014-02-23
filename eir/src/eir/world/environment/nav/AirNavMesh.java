package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

public class AirNavMesh extends NavMesh  <AirNavNode>
{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Route <AirNavNode> getShortestRoute(AirNavNode from, AirNavNode to) {
		return null;
	}

	@Override
	protected AirNavNode createNavNode( NavNodeDescriptor descriptor, Vector2 point, int nodeIdx )
	{
		return new AirNavNode( descriptor, point, nodeIdx );
	}

}
