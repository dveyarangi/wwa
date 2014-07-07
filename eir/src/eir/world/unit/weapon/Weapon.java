/**
 *
 */
package eir.world.unit.weapon;

import yarangi.math.Angles;

import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

/**
 * @author dveyarangi
 *
 */
public class Weapon extends Unit
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

	private static final float PLANK_CONST = 0.0001f;

	private boolean isOriented = false;

	private GameFactory gameFactory;

	private Level level;

	WeaponDef weaponDef;
	/**
	 *
	 */
	public Weapon ()
	{
		super();

		weaponDir = new Vector2(1,0);


		// TODO: set position of weapon relative to host
		this.relativePosition = new Vector2();
	}

	@Override
	public void reset(final GameFactory gameFactory, final Level level)
	{
		super.reset( gameFactory, level );
		this.gameFactory = gameFactory;
		this.level = level;
		weaponDef = (WeaponDef)def;

		weaponDef.init(gameFactory);

		targetOrientation = weaponDir.cpy();
	}

	public Bullet fire( final ISpatialObject target )
	{

		Bullet bullet = createBullet( target, getArea().getAnchor(), weaponDir, level.getUnitsFactory() );
		if(bullet != null)
		{
			level.addUnit( bullet );
		}
		return null;
	}

	protected Bullet createBullet(final ISpatialObject target, final Vector2 weaponPos, final Vector2 fireDir, final UnitsFactory unitFactory)
	{
		float angle = weaponDef.createAngle( this, fireDir );

		Vector2 direction = Vector2.tmp.set( 0, 1 );
		direction.setAngle( angle );

		float speed = weaponDef.createSpeed();
		Bullet bullet = unitFactory.getUnit(gameFactory, level, weaponDef.getBulletDef(), weaponPos.x, weaponPos.y, angle);


		bullet.weapon = this;
		bullet.getVelocity().set( direction ).mul( speed );
		bullet.target = target;
		bullet.angle = angle;


		bullet.lifelen = weaponDef.getBulletLifeDuration();

		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
		{
			timeToReload = weaponDef.getReloadingTime();
		} else
		{
			timeToReload = weaponDef.getMagazineReloadTime();
		}

		return bullet;
	}

	@Override
	public void update(final float delta)
	{
		timeToReload -= delta;

		float diffAngle =
				(float)( Math.acos( targetOrientation.dot( weaponDir ) )* Angles.TO_DEG);
		if(Math.abs( diffAngle ) < Math.abs( delta * weaponDef.getAngularSpeed() ))
		{
			weaponDir.set( targetOrientation );
		}
		else
		{
			// TODO: create spinor utility?
			int dir = weaponDir.crs( targetOrientation ) > 0 ? 1 : -1;

			float angleDelta = dir * delta * weaponDef.getAngularSpeed();

			float dx = (float) Math.cos(angleDelta * Angles.TO_RAD);
			float dy = (float) Math.sin(angleDelta * Angles.TO_RAD);

			float x = weaponDir.x * dx - weaponDir.y * dy;
			float y = weaponDir.y * dx + weaponDir.x * dy;

			weaponDir.set( x, y ).nor();
		}

//		weaponDir.lerp( targetOrientation, delta * weaponDef.getAngularSpeed() );

//		weaponDir.nor();

		diffAngle =
				(float) (Math.acos( targetOrientation.dot( weaponDir ) )* Angles.TO_DEG);

		float absDistance = Math.abs( diffAngle );

		isOriented = absDistance < weaponDef.getMaxFireAngle();

		this.angle = weaponDir.angle();

	}


	public Level getLevel() { return level; }


	public Vector2 getDirection() { return weaponDir; }

	public boolean isOriented() { return isOriented; }
	public void reload() { bulletsInMagazine = weaponDef.getBurstSize(); }

	public Vector2 getTargetOrientation() { return targetOrientation; }

	public float getTimeToReload() { return timeToReload; }

	public int getBulletsInMagazine() {	return bulletsInMagazine; }

	@Override
	public WeaponDef getDef() { return weaponDef; }

	@Override
	public float getMaxSpeed() { return 0; }

}