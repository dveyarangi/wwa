/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;


/**
 * @author dveyarangi
 *
 */
public class HomingBehavior implements IBulletBehavior
{


	@Override
	public void update(float delta, Bullet bullet)
	{
		float dx = bullet.getVelocity().x * delta;
		float dy = bullet.getVelocity().y * delta; 
		bullet.getBody().getAnchor().add( dx, dy );
		
		Vector2 force = bullet.getTarget().tmp().sub( bullet.getBody().getAnchor() ).nor()
				.mul( RandomUtil.STD( 600, 100) *delta );
		
		bullet.getVelocity().add( force );
		if(bullet.getVelocity().len2() > 10000)
			bullet.getVelocity().nor().mul( 100 );
		
		// hit:
		if(bullet.getBody().getAnchor().dst2( bullet.getTarget() ) < 10)
		{
			bullet.setIsAlive(false);
		}
	}
}
