package eir.world.unit.weapon;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.GameFactory;
import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;
import eir.world.Effect;
import eir.world.unit.AOEFunction;
import eir.world.unit.Damage;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.cannons.TargetProvider;

public class HomingLauncherDef extends WeaponDef
{
	private IBulletBehavior bulletBehavior;

	private Damage bulletDamage;

	private UnitDef bulletDef;

	private Animation hitAnimation01;
	private Animation hitAnimation02;
	private Animation trailAnimation;


	private int burstChirality = 1;
	public HomingLauncherDef(final String type, final int faction, final float size,
			final TextureHandle unitSprite, final AnimationHandle deathAnimation, final boolean isPickable)
	{
		super( type, faction, size, unitSprite, deathAnimation, isPickable );

		bulletBehavior = new HomingBehavior();

		bulletDamage = new Damage( AOEFunction.LINEAR_DECAY, 10f, 100f,0f,0f,0f );

		bulletDef = new UnitDef( UnitsFactory.BULLET, faction, 2f, GameFactory.ROCKET_TXR, null, false );
	}


	@Override
	public void init(final GameFactory gameFactory)
	{
		this.hitAnimation01 = gameFactory.getAnimation( GameFactory.EXPLOSION_03_ANIM );
		this.hitAnimation02 = gameFactory.getAnimation( GameFactory.EXPLOSION_05_ANIM );
		this.trailAnimation = gameFactory.getAnimation( GameFactory.SMOKE_ANIM );
	}

	@Override
	public int getBurstSize() { return 3; }

	@Override
	public float getMagazineReloadTime() { return 1f; }

	@Override
	public float getReloadingTime() { return 0.15f; }

	@Override
	public float getDispersion() { return 15; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public float createSpeed() { return RandomUtil.STD( getBulletSpeed()/2, getBulletSpeed()/10 ); }

	@Override
	protected float createAngle( final Weapon weapon, final Vector2 firingDir)
	{
		if(weapon.getBulletsInMagazine() == 0)
		{
			burstChirality *= -1;
//		burstAngle = (weaponDir.crs( spider.getAxis() ) < 0 ? 90 : -90) +
//				( 15 - RandomUtil.N( 30 ) );
		}

		float burstAngle = burstChirality * 90 + burstChirality * ( 15 + RandomUtil.STD( 0, 5 ) );

		return RandomUtil.STD( burstAngle + weapon.getAngle(), getDispersion());
	}

	@Override
	public float getBulletSpeed() { return 50; }

	@Override
	public Effect createHitEffect(final Bullet bullet, final boolean isTargetDead)
	{
		if(RandomUtil.oneOf( 5 ))
			return Effect.getEffect( hitAnimation01,
					RandomUtil.STD( 20, 4 ), bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 3 );
		else
			return Effect.getEffect( hitAnimation02,
					RandomUtil.STD( 8, 2 ), bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 3 );

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
		return Effect.getEffect( trailAnimation,
				RandomUtil.N( 2 ) + 2,
				bullet.getArea().getAnchor(),
				Vector2.tmp2.set( 0,0 ).sub(bullet.getVelocity().tmp().nor()).mul( getBulletSpeed()/2f ),
				RandomUtil.N( 360 ), 5f*RandomUtil.STD( 2, 0.15f ) );
	}

	@Override
	public float getAngularSpeed() { return 0.5f; }
	@Override
	public float getMaxFireAngle() { return 180f; }

	@Override
	public boolean decayOnNoTarget() { return true; }

	@Override
	public UnitDef getBulletDef() { return bulletDef; }
	@Override
	public TargetProvider createTargetProvider( final Unit owner )
	{
		return TargetProvider.RANDOM_TARGETER( owner );
	}

	@Override
	public float getSensorRadius() { return 100; }

}
