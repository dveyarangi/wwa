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
import eir.world.unit.Damage;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Minigun extends IWeapon
{


	private static final int BULLET_AID = GameFactory.registerAnimation( "anima//bullets//rocket01.atlas", "bullet" );
	private static final int HIT_01_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion04.atlas", "explosion04" );

	private Animation bulletAnimation;

	private IBulletBehavior bulletBehavior;

	private Damage bulletDamage;

	private static Sprite bulletSprite = GameFactory.createSprite( "anima//bullets//fireball.png" );

	public Minigun(final Unit unit)
	{

		super(unit);
//		bulletSprite = new Sprite(GameFactory.loadTexture("models/fireball.png"));
//		bulletSprite.setOrigin( bulletSprite.getWidth()/2, bulletSprite.getHeight()/2 );
//		bulletSprite.setScale( getSize() / bulletSprite.getWidth() );

		bulletAnimation = GameFactory.getAnimation( BULLET_AID );

		bulletBehavior = new MassDriverBehavior();

		bulletDamage = new Damage(50f,0,0,0);
	}


	@Override
	public float getBulletSize() { return 0.5f; }


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
	public Animation getBulletAnimation() { return bulletAnimation; }

	@Override
	public Sprite getBulletSprite() { return bulletSprite; }

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
	public Effect createHitEffect(final Bullet bullet, final boolean isTargetDead)
	{
		return Effect.getEffect( HIT_01_AID,
				15, bullet.getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 3 );
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

}
