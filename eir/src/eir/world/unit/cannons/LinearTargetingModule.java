package eir.world.unit.cannons;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;

public class LinearTargetingModule extends TargetingModule
{

	/**
	 * Predicts point of impact assuming the target velocity is not going to change
	 */
	@Override
	public Vector2 getShootingDirection( final ISpatialObject target, final Cannon cannon  )
	{

		Unit targetUnit = (Unit) target;

		if(target == null)
			return null;

		// current direction to target
		Vector2 targetDir = target.getArea().getAnchor().tmp().sub( cannon.getArea().getAnchor() );

		double A = targetDir.x * targetDir.x + targetDir.y * targetDir.y;

		double B = 2 * (targetUnit.getVelocity().x * targetDir.x +
				targetUnit.getVelocity().y * targetDir.y);

		double C = targetUnit.getVelocity().x * targetUnit.getVelocity().x +
				   targetUnit.getVelocity().y * targetUnit.getVelocity().y -
				   cannon.getWeapon().getMaxSpeed() * cannon.getWeapon().getMaxSpeed();

		double D = B*B - 4*A*C;
		if( D < 0 )
			return null;

		D = Math.sqrt( D );

		float flyTime = (float) ( ( -B + D ) / (2 * C) );
		if(flyTime < 0)
		{
			flyTime = (float) ( ( -B - D ) / (2 * C) );
		}

		if(flyTime < 0)
			return null; // bullet cannot catch the target

		Vector2 predictedDir = targetDir.add( Vector2.tmp2.set( targetUnit.getVelocity() ).mul( flyTime ) );

		return predictedDir;
	}

}
