/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.world.Level;

/**
 * @author dveyarangi
 *
 */
public abstract class IWeapon
{

	//////////////////////////////////
	// properties
	
	
	////////////////////////////////
	// state
	protected Vector2 weaponDir;
	private float timeToReload;
	private int bulletsInMagazine;

	/**
	 * 
	 */
	public IWeapon()
	{
		super();
		
		weaponDir = new Vector2();
	}

	public Bullet createBullet(Level level, Vector2 weaponPos, Vector2 targetPos)
	{
		if(timeToReload > 0)
		{
			return null;
		}
		if(bulletsInMagazine == 0)
			bulletsInMagazine = getBurstSize();
		
		weaponDir.set( targetPos ).sub( weaponPos ).nor();
		float angle = RandomUtil.STD( weaponDir.angle(), getAccuracy());
		weaponDir.setAngle( angle );
		
		Bullet bullet = Bullet.getBullet( level, this, 
				weaponPos.x, weaponPos.y, 
			 	weaponDir.x * getSpeed(), weaponDir.y*getSpeed(),
				targetPos);
		
		bullet.angle = weaponDir.angle();
		
		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
			timeToReload = getReloadingTime();
		else
			timeToReload = getMagazineReloadTime();
		
		return bullet;
	}

	public void update(float delta)
	{
		timeToReload -= delta;
	}
	
	/**
	 * @return the size
	 */
	public abstract int getSize();
	/**
	 * @return the burstSize
	 */
	public abstract int getBurstSize();

	/**
	 * @return the magazineReloadTime
	 */
	public abstract float getMagazineReloadTime();

	/**
	 * @return the reloadingTime
	 */
	public abstract float getReloadingTime();

	/**
	 * @return the accuracy
	 */
	public abstract float getAccuracy();

	/**
	 * @return the bulletBehavior
	 */
	public abstract IBulletBehavior getBulletBehavior();
	/**
	 * @return the bulletSprite
	 */
	public abstract Sprite getBulletSprite();

	/**
	 * @return the speed
	 */
	public abstract float getSpeed();



}