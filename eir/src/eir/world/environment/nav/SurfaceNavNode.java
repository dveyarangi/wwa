package eir.world.environment.nav;

import com.badlogic.gdx.math.Vector2;

import eir.resources.PolygonalModel;
import eir.world.Asteroid;
import eir.world.environment.Anchor;

public class SurfaceNavNode extends NavNode implements Anchor
{


	/**
	 * index of the asteroid containing this nav node
	 */
	final int				aIdx;

	/**
	 * Normal angle
	 */
	float             angle;

	SurfaceNavNode( final NavNodeDescriptor descriptor, final Vector2 point, final int idx, final int aIdx )
	{
		super( descriptor, point, idx );

		this.aIdx = aIdx;

		this.angle = Float.NaN;
	}

	@Override
	public float getAngle()
	{
		if(!Float.isNaN( angle ))
			return angle;

		int navIdx = getDescriptor().getIndex();
		Asteroid asteroid = (Asteroid)getDescriptor().getObject();

		PolygonalModel model = asteroid.getModel();

		Vector2 surface = model.getNavNode( navIdx-1 ).getPoint().tmp()
					.sub( model.getNavNode( navIdx+1 )                  .getPoint() );

		this.angle = surface.rotate( 90 ).angle();
		return angle;
	}

}
