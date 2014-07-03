/**
 *
 */
package eir.world.unit.weapon;


import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;



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
		Vector2	target;
		if( bullet.target == null )
		{
//			bullet.lifetime = bullet.weapon.getBulletLifeDuration();
			float chirality = hashCode() % 2 == 0 ? -1 : 1;

			target = bullet.getArea().getAnchor().tmp().add(
					Vector2.tmp.set( bullet.getVelocity() )
					.mul(100)
							.rotate( chirality * RandomUtil.STD( 0, 10 ) )
					);

		}
		else
		{
			target = bullet.getTarget().getArea().getAnchor();
		}
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
			bullet.getVelocity().mul( 1 - 0.75f*delta  );
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
