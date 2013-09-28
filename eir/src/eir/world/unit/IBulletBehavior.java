/**
 * 
 */
package eir.world.unit;


/**
 * @author dveyarangi
 *
 */
public interface IBulletBehavior
{
	public void update(float delta, Bullet bullet);
	public boolean requiresTarget();
}
