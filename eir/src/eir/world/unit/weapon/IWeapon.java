/**
 *
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import eir.world.Effect;
import eir.world.Level;
import eir.world.unit.Damage;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

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


	private Unit owner;
	/**
	 *
	 */
	public IWeapon(final Unit owner)
	{
		super();

		this.owner = owner;

		weaponDir = new Vector2();
	}

	public Bullet fire( final Vector2 shootingTarget )
	{
		Bullet bullet = createBullet( shootingTarget, owner.getFaction().getLevel().getUnitsFactory() );
		if(bullet != null)
		{
			owner.getFaction().getLevel().addUnit( bullet );
		}
		return null;
	}

	protected Bullet createBullet(final Vector2 targetPos, final UnitsFactory unitFactory)
	{
		if(timeToReload > 0)
			return null;
		if(bulletsInMagazine == 0)
		{
			bulletsInMagazine = getBurstSize();
		}

		Vector2 weaponPos = owner.getArea().getAnchor();

		weaponDir.set( targetPos ).sub( weaponPos ).nor();
		float angle = createAngle();
		weaponDir.setAngle( angle );

		float speed = createSpeed();
		Bullet bullet = unitFactory.getUnit(UnitsFactory.BULLET, weaponPos.x, weaponPos.y, angle, owner.getFaction());

		bullet.weapon = this;
		bullet.velocity.set(this.getDirection()).mul( speed );
		bullet.size = this.getSize();
		bullet.target = targetPos;

		bullet.angle = weaponDir.angle();

		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
		{
			timeToReload = getReloadingTime();
		} else
		{
			timeToReload = getMagazineReloadTime();
		}

		return bullet;
	}

	/**
	 * @return
	 */
	protected abstract float createAngle();

	public void update(final float delta)
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

	public Level getLevel() { return owner.getFaction().getLevel(); }

	public Faction getFaction() { return owner.getFaction(); }

	public Vector2 getDirection() { return weaponDir; }

	public Unit getOwner() { return owner; }

}