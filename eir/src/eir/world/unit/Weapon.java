/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

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
	
	private int burstSize = 7;
	private float magazineReloadTime = 0.5f;
	private float reloadingTime = 0.05f;
	
	private float accuracy = 360f;
	
	private IBulletBehavior bulletBehavior = new HomingBehavior();
//	private IBulletBehavior bulletBehavior = new MassDriverBehavior();
	
	////////////////////////////////
	// state
	
	private Vector2 weaponDir;
	
	private float timeToReload = 0;
	
	private int bulletsInMagazine = burstSize;
	
	public Weapon()
	{
		this.weaponDir = new Vector2();
	}
	
	public Bullet createBullet(Level level, Vector2 weaponPos, Vector2 targetPos)
	{
		if(timeToReload > 0)
		{
			return null;
		}
		if(bulletsInMagazine == 0)
			bulletsInMagazine = burstSize;
		
		weaponDir.set( targetPos ).sub( weaponPos ).nor();
		float angle = RandomUtil.STD( weaponDir.angle(), accuracy);
		weaponDir.setAngle( angle );
		
		Bullet bullet = Bullet.getBullet( level, bulletBehavior,
				"fireball",
				size, 
				weaponPos.x, weaponPos.y, 
				weaponDir.x * speed, weaponDir.y*speed,
				targetPos);
		
		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
			timeToReload = reloadingTime;
		else
			timeToReload = magazineReloadTime;
		
		return bullet;
	}
	
	public void update( float delta )
	{
		timeToReload -= delta;
	}
}
