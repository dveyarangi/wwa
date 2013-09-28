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
	//////////////////////////////////
	// properties
	private float speed = 100;
	
	private float size = 1;
	
	private float reloadingTime = 0.1f;
	
	////////////////////////////////
	// state
	
	private Vector2 weaponDir;
	
	private float timeToReload = 0;
	
	public Weapon()
	{
		this.weaponDir = new Vector2();
	}
	
	public Bullet createBullet(Level level, Vector2 weaponPos, Vector2 targetPos)
	{
		if(timeToReload > 0)
			return null;
		
		weaponDir.set( targetPos ).sub( weaponPos ).nor();
		
		Bullet bullet = Bullet.getBullet( level, "fireball",
				size, 
				weaponPos.x, weaponPos.y, 
				weaponDir.x * speed, weaponDir.y*speed );
		
		timeToReload = reloadingTime;
		
		return bullet;
	}
	
	public void update( float delta )
	{
		timeToReload -= delta;
	}
}
