/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.math.Vector2;


/**
 * @author dveyarangi
 *
 */
public class HomingBehavior implements IBulletBehavior
{

	private Weapon weapon;
	
	public HomingBehavior(Weapon weapon)
	{
		this.weapon = weapon;
	}
	
	@Override
	public void update(float delta, Bullet bullet)
	{
		float dx = bullet.getVelocity().x * delta;
		float dy = bullet.getVelocity().y * delta; 
		bullet.getBody().getAnchor().add( dx, dy );
		
		Vector2 force = bullet.getTarget().tmp().sub( bullet.getBody().getAnchor() ).nor()
				.mul( 1000 * delta );
		
		bullet.getVelocity().add( force );
		if(bullet.getVelocity().len2() > weapon.speed*weapon.speed)
			bullet.getVelocity().nor().mul( weapon.speed );
		bullet.angle = force.angle();
		
		// hit:
		if(bullet.getBody().getAnchor().dst2( bullet.getTarget() ) < 10)
		{
			bullet.setIsAlive(false);
		}
	}


	@Override public boolean requiresTarget() {	return true; }
}
