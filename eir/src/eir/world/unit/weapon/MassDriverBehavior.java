/**
 * 
 */
package eir.world.unit.weapon;



/**
 * @author dveyarangi
 *
 */
public class MassDriverBehavior implements IBulletBehavior
{


	@Override
	public void update(float delta, Bullet bullet)
	{
		float dx = bullet.getVelocity().x * delta;
		float dy = bullet.getVelocity().y * delta; 
		bullet.getBody().getAnchor().add( dx, dy );
	}

	@Override public boolean requiresTarget() { return false; }
}
