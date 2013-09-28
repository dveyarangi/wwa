/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.math.Vector2;

import eir.world.Bullet;
import eir.world.Level;

/**
 * @author dveyarangi
 *
 */
public class Weapon
{
	private float speed = 100;
	
	private float size = 1;
	
	private Vector2 weaponDir;
	
	
	public Weapon()
	{
		this.weaponDir = new Vector2();
	}
	
	public Bullet createBullet(Level level, Vector2 weaponPos, Vector2 targetPos)
	{
		weaponDir.set( targetPos ).sub( weaponPos ).nor();
		
		Bullet bullet = Bullet.getBullet( level, "fireball",
				size, 
				weaponPos.x, weaponPos.y, 
				weaponDir.x * speed, weaponDir.y*speed );

		return bullet;
	}
}
