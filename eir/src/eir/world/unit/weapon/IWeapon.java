/**
 *
 */
package eir.world.unit.weapon;

import yarangi.math.Vector2D;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;
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

	protected float angle;

	protected float timeToReload;
	protected int bulletsInMagazine;

	protected Vector2 targetOrientation;

	protected Vector2 relativePosition;


	private Unit owner;

	private static final float PLANK_CONST = 0.0001f;

	private boolean isOriented = false;
	/**
	 *
	 */
	public IWeapon(final Unit owner)
	{
		super();

		this.owner = owner;

		weaponDir = new Vector2(0,1);

		this.angle = owner.getAngle();
		weaponDir.setAngle( angle );

		targetOrientation = weaponDir.cpy();


		// TODO: set position of weapon relative to host
		this.relativePosition = new Vector2();
	}

	public Bullet fire( final ISpatialObject target, final Vector2 direction )
	{
		Vector2 weaponPos = Vector2.tmp2.set( owner.getArea().getAnchor() ).add( relativePosition );

		Vector2 firingDir = direction;//Vector2.tmp.set( target.getArea().getAnchor() ).sub( weaponPos ).nor();

		targetOrientation.set( direction );
		Bullet bullet = createBullet( target, weaponPos, firingDir, owner.getFaction().getLevel().getUnitsFactory() );
		if(bullet != null)
		{
			owner.getFaction().getLevel().addUnit( bullet );
		}
		return null;
	}

	protected Bullet createBullet(final ISpatialObject target, final Vector2 weaponPos, final Vector2 direction, final UnitsFactory unitFactory)
	{
		if(timeToReload > 0)
			return null;

		if(! isOriented )
			return null;

		if(bulletsInMagazine == 0)
		{
			bulletsInMagazine = getBurstSize();
		}

		float angle = createAngle( direction );

		direction.setAngle( angle );

		float speed = createSpeed();
		Bullet bullet = unitFactory.getUnit(UnitsFactory.BULLET, weaponPos.x, weaponPos.y, angle, owner.getFaction());


		bullet.weapon = this;
		bullet.getVelocity().set( direction ).mul( speed );
		bullet.size = this.getBulletSize();
		bullet.target = target;

		bullet.angle = angle;

		bullet.lifelen = getBulletLifeDuration();

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
	 * @param firingDir
	 * @return
	 */
	protected abstract float createAngle(Vector2 firingDir);

	public void update(final float delta)
	{
		timeToReload -= delta;

		// angle delta
		float da = delta * getAngularSpeed();

		float targetAngle = targetOrientation.angle();

		float diffAngle =
				(float)Math.acos( targetOrientation.dot( weaponDir ) /
				(
						targetOrientation.len() * weaponDir.len()
				) );

//		float absDistance = Math.abs( angle - targetOrientation.angle() );
		float absDistance = Math.abs( diffAngle );

		if( absDistance < getMaxFireAngle())
		{
			isOriented = true;
		} else
		{
			isOriented = false;
		}

		if( absDistance < da ) // arrived
		{
			angle = targetAngle;
		}
		else
		{
			if(Vector2D.crossZComponent(
					weaponDir.x, weaponDir.y,
					targetOrientation.x, targetOrientation.y )
				> 0)
			{
				angle -= da;
				angle = angle % 360;
			}
			else
			{
				angle += da;
				angle = angle % 360;
			}
		}

		weaponDir.setAngle( angle );

	}

	/**
	 * @return the size
	 */
	public abstract float getBulletSize();
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


	public abstract float getBulletLifeDuration();
	/**
	 * @return the bulletBehavior
	 */
	public abstract IBulletBehavior getBulletBehavior();
	/**
	 * @return the bulletSprite
	 */
	public abstract Animation getBulletAnimation();
	public abstract Sprite getBulletSprite();

	/**
	 * @return the speed
	 */
	public abstract float createSpeed();

	public abstract float getAngularSpeed();

	/**
	 * Max angular offset from target orientation when weapon is allowed to fire
	 * @return
	 */
	public abstract float getMaxFireAngle();

	/**
	 * @return
	 */
	public float getMaxSpeed() { return createSpeed(); }

	/**
	 * @return
	 */
	public abstract Effect createHitEffect(Bullet bullet);
	public abstract Effect createTraceEffect(Bullet bullet);

	/**
	 *
	 */
	public abstract Damage getDamage();

	public Level getLevel() { return owner.getFaction().getLevel(); }

	public Faction getFaction() { return owner.getFaction(); }

	public Vector2 getDirection() { return weaponDir; }

	public Unit getOwner() { return owner; }

	public float getTimeToReload() { return timeToReload; }

	public float getAngle() { return angle; }


}