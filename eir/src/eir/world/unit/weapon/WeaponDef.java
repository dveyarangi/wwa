package eir.world.unit.weapon;

import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.GameFactory;
import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;
import eir.world.Effect;
import eir.world.unit.Damage;
import eir.world.unit.Unit;
import eir.world.unit.cannons.TargetProvider;

public abstract class WeaponDef extends UnitDef
{
	public WeaponDef(final String type, final int faction, final float size,
			final TextureHandle unitSprite, final AnimationHandle deathAnimation, final boolean isPickable)
	{
		super( type, faction, size, unitSprite, deathAnimation, isPickable );
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
	/**
	 *
	 */
	public abstract Damage getDamage();

	public abstract boolean decayOnNoTarget();

	public abstract UnitDef getBulletDef();

	/**
	 * @param firingDir
	 * @return
	 */
	protected abstract float createAngle( Weapon weapon, Vector2 firingDir);


	public abstract Effect createTraceEffect(Bullet bullet);

	public abstract Effect createHitEffect( Bullet bullet, boolean b );

	public abstract TargetProvider createTargetProvider( Unit owner );

	public abstract float getSensorRadius();

	public abstract void init( GameFactory gameFactory );
}
