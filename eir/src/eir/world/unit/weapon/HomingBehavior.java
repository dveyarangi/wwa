/**
 *
 */
package eir.world.unit.weapon;


import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;



/**
 * @author dveyarangi
 *
 */
public class HomingBehavior implements IBulletBehavior
{

	private IWeapon weapon;

	public HomingBehavior(final IWeapon weapon)
	{
		this.weapon = weapon;
	}

	@Override
	public void update(final float delta, final Bullet bullet)
	{
		ISpatialObject targetUnit = bullet.getTarget();
		if(targetUnit == null || ! targetUnit.isAlive())
		{
			targetUnit = bullet.getTarget(); // last known target
		}

		if( targetUnit == null )
		{
//			bullet.lifetime = bullet.weapon.getBulletLifeDuration();

			float dx = bullet.getVelocity().x * delta;
			float dy = bullet.getVelocity().y * delta;
			bullet.getBody().getAnchor().add( dx, dy );
			return;
		}

		Vector2	target = bullet.getTarget().getArea().getAnchor();

/*			float chirality = hashCode() % 2 == 0 ? -1 : 1;
			target = Vector2.tmp3.set( chirality * bullet.getVelocity().y/2, -chirality * bullet.getVelocity().x*2 )
					.nor()
					.mul( (float)Math.log( bullet.lifetime) * 1 )
					.add( bullet.getArea().getAnchor() );*/


//			float chirality = hashCode() % 2 == 0 ? -1 : 1;
/*			target = Vector2.tmp3.set( bullet.getBody().getAnchor() ).add( bullet.velocity )
					.add( RandomUtil.STD( 10, 5 ), RandomUtil.STD( 10, 5 ) );*/

//			bullet.lifetime += (bullet.weapon.getLifeDuration() - bullet.lifetime) / 2 * delta;


		float dx = bullet.getVelocity().x * delta;
		float dy = bullet.getVelocity().y * delta;
		bullet.getBody().getAnchor().add( dx, dy );

		Vector2 force = target.tmp().sub( bullet.getBody().getAnchor() ).nor()
							.mul( 1000 * bullet.getLifetime() * bullet.getLifetime() * delta );
		if(bullet.getLifetime() < 0.4)
		{
			bullet.getVelocity().mul( 0.95f  );
		}
		else
		{
			bullet.leaveTrace = true;
			bullet.getVelocity().add( force );
			if(bullet.getVelocity().len2() > bullet.getMaxSpeed()*bullet.getMaxSpeed())
			{
				bullet.getVelocity().nor().mul( bullet.getMaxSpeed() );
			}

			bullet.angle = force.angle();
		}

		// hit:
/*		if(bullet.getBody().getAnchor().dst2( target ) < 1)
		{
			bullet.setDead();
		}*/
	}


	@Override public boolean requiresTarget() {	return true; }
}
