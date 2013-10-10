/**
 * 
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import eir.world.Effect;
import eir.world.Level;
import eir.world.unit.Bullet;
import eir.world.unit.Damage;

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
	protected float timeToReload;
	protected int bulletsInMagazine;


	protected Level level;
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
		float angle = createAngle();
		weaponDir.setAngle( angle );
		
		float speed = createSpeed();
		Bullet bullet = Bullet.getBullet( level, this, 
				weaponPos.x, weaponPos.y, 
			 	weaponDir.x * speed, weaponDir.y * speed,
				targetPos);
		
		bullet.angle = weaponDir.angle();
		this.level = level;
		
		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
			timeToReload = getReloadingTime();
		else
			timeToReload = getMagazineReloadTime();
		
		return bullet;
	}

	/**
	 * @return
	 */
	protected abstract float createAngle();

	public void update(float delta)
	{
		timeToReload -= delta;
	}
	
	/**
	 * @return the size
	 */
	public abstract float getSize();
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
	public abstract Animation getBulletAnimation();

	/**
	 * @return the speed
	 */
	public abstract float createSpeed();

	/**
	 * @return
	 */
	public float getMaxSpeed() { return createSpeed(); }

	/**
	 * @return
	 */
	public abstract Effect createHitEffect(Bullet bullet);
	public abstract Effect createTraceEffect(Bullet bullet);

	public abstract float getLifeDuration();

	/**
	 * 
	 */
	public abstract Damage getDamage();

	public Level getLevel() { return level; }
}