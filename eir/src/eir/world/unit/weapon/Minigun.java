/**
 *
 */
package eir.world.unit.weapon;


import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.levels.UnitDef;
import eir.world.Effect;
import eir.world.Level;
import eir.world.unit.Damage;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

/**
 * @author dveyarangi
 *
 */
public class Minigun extends IWeapon
{


//	private Animation bulletAnimation;

	private IBulletBehavior bulletBehavior;

	private Damage bulletDamage;

	private Animation hitAnimation;


	public Minigun(final Unit unit)
	{

		super(unit);
		bulletBehavior = new MassDriverBehavior();

		bulletDamage = new Damage(50f,0,0,0);
	}


	@Override
	public void init( final GameFactory gameFactory, final Level level )
	{
		super.init( gameFactory, level );
		this.hitAnimation = gameFactory.getAnimation( GameFactory.EXPLOSION_04_ANIM );
	}


	@Override
	public int getBurstSize() { return 1; }

	@Override
	public float getMagazineReloadTime() { return 0.4f; }

	@Override
	public float getReloadingTime() { return 0.04f; }

	@Override
	public float getDispersion() { return 0.0f; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public float createSpeed() { return getBulletSpeed(); }

	@Override
	public float getBulletSpeed() { return 50; }


	@Override
	protected float createAngle( final Vector2 firingDir )
	{
		return RandomUtil.STD( firingDir.angle(), getDispersion());
	}

	@Override
	public Effect createTraceEffect(final Bullet bullet) { return null; }


	@Override
	public float getBulletLifeDuration() { return 2; }


	@Override
	public Damage getDamage()
	{
		return bulletDamage;
	}
	@Override
	public float getAngularSpeed() { return 20f; }


	@Override
	public float getMaxFireAngle() { return 10f; }

	@Override
	public boolean decayOnNoTarget() { return true; }


	@Override
	public UnitDef getBulletDef()
	{
		return new UnitDef( UnitsFactory.BULLET, 0.5f, GameFactory.FIREBALL_TXR, null );
	}


	@Override
	public Effect createHitEffect( final Bullet bullet, final boolean b ) {
		return Effect.getEffect( hitAnimation,
				15, bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 3 );
	}

}
