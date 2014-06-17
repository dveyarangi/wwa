/**
 *
 */
package eir.world.unit.weapon;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.unit.AOEFunction;
import eir.world.unit.Damage;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class HomingLauncher extends IWeapon
{
	private Animation bulletAnimation;

	private IBulletBehavior bulletBehavior;

	private Damage bulletDamage;

	private int burstChirality = 1;


//	private static final int BULLET_AID = GameFactory.registerAnimation( "anima//bullets//rocket01.atlas", "bullet" );
	private static final int BULLET_AID = GameFactory.registerAnimation( "anima//bullets//rocket02.atlas", "bullet" );

	private static final int TRAIL_AID  = GameFactory.registerAnimation( "anima//effects//smoke//smoke.atlas", "smoke" );
	private static final int HIT_01_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion03.atlas", "explosion03" );
	private static final int HIT_02_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion05.atlas", "explosion05" );


	private static Sprite bulletSprite = GameFactory.createSprite( "anima//bullets//rocket01.png" );

	public HomingLauncher(final Unit unit)
	{
		super(unit);


		bulletAnimation = GameFactory.getAnimation( BULLET_AID );

		bulletBehavior = new HomingBehavior(this);

		bulletDamage = new Damage( AOEFunction.LINEAR_DECAY, 10f, 100f,0f,0f,0f );

	}

	@Override
	public float getBulletSize() { return 3; }


	@Override
	public int getBurstSize() { return 3; }

	@Override
	public float getMagazineReloadTime() { return 1f; }

	@Override
	public float getReloadingTime() { return 0.15f; }

	@Override
	public float getAccuracy() { return 15; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public Animation getBulletAnimation() { return bulletAnimation; }

	@Override
	public Sprite getBulletSprite() { return bulletSprite; }

	@Override
	public float createSpeed() { return RandomUtil.STD( 50, 10 ); }
	@Override
	public float getMaxSpeed() { return 100; }

	@Override
	protected float createAngle( final Vector2 firingDir)
	{
		if(this.bulletsInMagazine == 0)
		{
			burstChirality *= -1;
//		burstAngle = (weaponDir.crs( spider.getAxis() ) < 0 ? 90 : -90) +
//				( 15 - RandomUtil.N( 30 ) );
		}

		float burstAngle = burstChirality * 90 + burstChirality * ( 15 + RandomUtil.STD( 0, 5 ) );

		return RandomUtil.STD( burstAngle + this.getOwner().getAngle(), getAccuracy());
	}

	@Override
	public Effect createHitEffect(final Bullet bullet)
	{
		if(RandomUtil.oneOf( 5 ))
			return Effect.getEffect( HIT_01_AID,
					RandomUtil.STD( 40, 4 ), bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 3 );
		else
			return Effect.getEffect( HIT_02_AID,
					RandomUtil.STD( 15, 2 ), bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 3 );

	}

	@Override
	public float getBulletLifeDuration()
	{
		return 10;
	}

	@Override
	public Damage getDamage()
	{
		return bulletDamage;
	}

	@Override
	public Effect createTraceEffect(final Bullet bullet)
	{
		return Effect.getEffect( TRAIL_AID,
				RandomUtil.N( 4 ) + 2,
				bullet.getArea().getAnchor(),
				Vector2.tmp2.set( 0,0 ).sub(bullet.getVelocity().tmp().nor()).mul( 50f ),
				RandomUtil.N( 360 ), 7f*RandomUtil.STD( 2, 0.15f ) );
	}

	@Override
	public float getAngularSpeed() { return 0.5f; }
	@Override
	public float getMaxFireAngle() { return 180f; }


}
