/**
 *
 */
package eir.world.unit.weapon;

import yarangi.math.Angles;

import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.levels.UnitDef;
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

//	protected float angle;

	protected float timeToReload;
	protected int bulletsInMagazine;

	protected Vector2 targetOrientation;

	protected Vector2 relativePosition;


	private Unit owner;

	private static final float PLANK_CONST = 0.0001f;

	private boolean isOriented = false;

	private GameFactory gameFactory;

	private Level level;
	/**
	 *
	 */
	public IWeapon(final Unit owner)
	{
		super();

		this.owner = owner;

		weaponDir = new Vector2(1,0);

//		this.angle = owner.getAngle();
		weaponDir.setAngle( owner.getAngle() );

		targetOrientation = weaponDir.cpy();


		// TODO: set position of weapon relative to host
		this.relativePosition = new Vector2();
	}

	public void init(final GameFactory gameFactory, final Level level)
	{
		this.gameFactory = gameFactory;
		this.level = level;
	}

	public Bullet fire( final ISpatialObject target )
	{

		Vector2 weaponPos = Vector2.tmp2.set( owner.getArea().getAnchor() ).add( relativePosition );

		Bullet bullet = createBullet( target, weaponPos, weaponDir, owner.getFaction().getLevel().getUnitsFactory() );
		if(bullet != null)
		{
			owner.getFaction().getLevel().addUnit( bullet );
		}
		return null;
	}

	protected Bullet createBullet(final ISpatialObject target, final Vector2 weaponPos, final Vector2 fireDir, final UnitsFactory unitFactory)
	{
		float angle = createAngle( fireDir );

		Vector2 direction = Vector2.tmp.set( 0, 1 );
		direction.setAngle( angle );

		float speed = createSpeed();
		Bullet bullet = unitFactory.getUnit(gameFactory, level, getBulletDef(), weaponPos.x, weaponPos.y, angle);


		bullet.weapon = this;
		bullet.getVelocity().set( direction ).mul( speed );
		bullet.target = target;

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


		weaponDir.lerp( targetOrientation, delta * getAngularSpeed() );

		owner.angle = weaponDir.angle();
//		weaponDir.nor();

		float diffAngle =
				(float) Math.acos( targetOrientation.dot( weaponDir ) /
						( targetOrientation.len() * weaponDir.len()	) );

//		float absDistance = Math.abs( angle - targetOrientation.angle() );
		float absDistance = (float) (Math.abs( diffAngle ) * Angles.TO_DEG);

		isOriented = absDistance < getMaxFireAngle();

	}
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
	public abstract float getDispersion();


	public abstract float getBulletLifeDuration();
	/**
	 * @return the bulletBehavior
	 */
	public abstract IBulletBehavior getBulletBehavior();

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
	public abstract float getBulletSpeed();

	public abstract Effect createTraceEffect(Bullet bullet);

	public abstract Effect createHitEffect( Bullet bullet, boolean b );

	/**
	 *
	 */
	public abstract Damage getDamage();

	public Level getLevel() { return owner.getFaction().getLevel(); }

	public Faction getFaction() { return owner.getFaction(); }

	public Vector2 getDirection() { return weaponDir; }

	public Unit getOwner() { return owner; }

	public float getTimeToReload() { return timeToReload; }

	public int getBulletsInMagazine() {	return bulletsInMagazine; }

	public abstract boolean decayOnNoTarget();

	public boolean isOriented() { return isOriented; }
	public void reload() { bulletsInMagazine = getBurstSize(); }

	public Vector2 getTargetOrientation() { return targetOrientation; }

//	public float getAngle() { return angle; }

	public abstract UnitDef getBulletDef();

}