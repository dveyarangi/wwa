package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

public class SurfaceNavNode extends NavNode
{
	

	/**
	 * index of the asteroid containing this nav node
	 */
	final int				aIdx;

	SurfaceNavNode( NavNodeDescriptor descriptor, Vector2 point, int idx, int aIdx )
	{
		super( descriptor, point, idx );

		this.aIdx = aIdx;
	}

}
