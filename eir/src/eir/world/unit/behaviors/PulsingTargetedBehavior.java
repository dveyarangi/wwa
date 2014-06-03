/**
 *
 */
package eir.world.unit.behaviors;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public class PulsingTargetedBehavior <U extends Unit> implements UnitBehavior <U>
{

	private IPulseDef pulseDef;


	public PulsingTargetedBehavior( final IPulseDef pulseDef)
	{
		this.pulseDef = pulseDef;
	}

	@Override
	public void update(final float delta, final Task task, final U unit)
	{
		if(unit.timeToPulse <= 0)
		{

			unit.timeToPulse = pulseDef.getPulseDuration();
			unit.target = task.getOrder().getTarget();

			ISpatialObject targetObject = unit.getTarget();
			if( targetObject == null)
				return;

			Vector2 target = targetObject.getArea().getAnchor();


			Vector2 dir = target.tmp().sub( unit.getBody().getAnchor() );

			float dist = dir.len();
			dir.x += RandomUtil.R( dist ) - dist/2;
			dir.y += RandomUtil.R( dist ) - dist/2;

			dir.div(dist).mul( pulseDef.getPulseStrength() );

			unit.velocity.add( dir );


			unit.angle = unit.velocity.angle();
		}

		float speed = unit.velocity.len();
		Vector2 velocityNor = unit.velocity.tmp().div( speed );
		if(speed > unit.getMaxSpeed())
		{
			unit.velocity.mul(unit.getMaxSpeed() / speed );
		}

		speed = Math.min( speed, unit.getMaxSpeed() );


		unit.getArea().getAnchor().add( Vector2.tmp2.set( unit.velocity ).mul( delta ) );

		if(speed == 0 || speed < pulseDef.getPulseDecay() * delta)
		{
			unit.velocity.set(0,0);
		} else
		{
			unit.velocity.sub(velocityNor.mul( pulseDef.getPulseDecay() * delta ));
		}

		unit.timeToPulse -= delta;

	}

}
