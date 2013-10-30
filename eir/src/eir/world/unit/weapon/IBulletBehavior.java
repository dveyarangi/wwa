/**
 * 
 */
package eir.world.unit.weapon;



/**
 * @author dveyarangi
 *
 */
public interface IBulletBehavior
{
	public void update(float delta, Bullet bullet);
	public boolean requiresTarget();
}
