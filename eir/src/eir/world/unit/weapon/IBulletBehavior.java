/**
 * 
 */
package eir.world.unit.weapon;

import eir.world.unit.Bullet;


/**
 * @author dveyarangi
 *
 */
public interface IBulletBehavior
{
	public void update(float delta, Bullet bullet);
	public boolean requiresTarget();
}
