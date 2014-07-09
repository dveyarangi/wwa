package eir.world.unit.weapon;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import eir.resources.AnimationHandle;
import eir.resources.GameFactory;
import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;
import eir.world.Effect;
import eir.world.unit.Damage;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.cannons.TargetProvider;

public class SniperDef extends WeaponDef
{

	public SniperDef(final String type, final int faction, final float size,
			final TextureHandle unitSprite, final AnimationHandle deathAnimation, final boolean isPickable)
	{
		super( type, faction, size, unitSprite, deathAnimation, isPickable );

		bulletBehavior = new MassDriverBehavior();

		bulletDamage = new Damage(200f,0,0,0);

		this.bulletDef = new UnitDef( UnitsFactory.BULLET, faction, 1f, GameFactory.STRIPE_TXR, null, false );
	}

	private IBulletBehavior bulletBehavior;

	private Damage bulletDamage;

	private Animation hitAnimation;

	private UnitDef bulletDef;


	@Override
	public void init(final GameFactory gameFactory)
	{
		this.hitAnimation = gameFactory.getAnimation( GameFactory.EXPLOSION_04_ANIM );

	}

	@Override
	public int getBurstSize() { return 1; }

	@Override
	public float getMagazineReloadTime() { return 2f; }

	@Override
	public float getReloadingTime() { return 0.04f; }

	@Override
	public float getDispersion() { return 0.0f; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public float createSpeed() { return getBulletSpeed(); }

	@Override
	public float getBulletSpeed() { return 150; }

	@Override
	public Effect createTraceEffect(final Bullet bullet) { return null; }


	@Override
	public float getBulletLifeDuration() { return 1; }


	@Override
	public Damage getDamage()
	{
		return bulletDamage;
	}
	@Override
	public float getAngularSpeed() { return 15f; }


	@Override
	public float getMaxFireAngle() { return 1f; }

	@Override
	public boolean decayOnNoTarget() { return true; }


	@Override
	public UnitDef getBulletDef() { return bulletDef; }


	@Override
	public Effect createHitEffect( final Bullet bullet, final boolean b ) {
		return Effect.getEffect( hitAnimation,
				15, bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 3 );
	}

	@Override
	protected float createAngle( final Weapon weapon, final Vector2 firingDir )
	{
		return RandomUtil.STD( firingDir.angle(), getDispersion());
	}

	@Override
	public TargetProvider createTargetProvider( final Unit owner )
	{
		return TargetProvider.CLOSEST_TARGETER( owner );
	}

	@Override
	public float getSensorRadius() { return 100; }
}


