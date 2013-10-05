/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.resources.GameFactory;
import eir.world.Effect;

/**
 * @author dveyarangi
 *
 */
public class HomingLauncher extends IWeapon
{
	private Animation bulletAnimation;
	
	private IBulletBehavior bulletBehavior;
	
	private Damage bulletDamage;
	
	private float burstAngle;
	
	private static final int BULLET_AID = GameFactory.registerAnimation( "anima//bullets//rocket01.atlas", "bullet" );
	private static final int HIT_01_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion03.atlas", "explosion03" );
	private static final int HIT_02_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion05.atlas", "explosion05" );
	
	public HomingLauncher()
	{
		bulletAnimation = GameFactory.getAnimation( BULLET_AID );
		
		bulletBehavior = new HomingBehavior(this);
		
		bulletDamage = new Damage();
	}
	
	@Override
	public float getSize() { return 2; }


	@Override
	public int getBurstSize() { return 7; }

	@Override
	public float getMagazineReloadTime() { return 1f; }

	@Override
	public float getReloadingTime() { return 0.05f; }

	@Override
	public float getAccuracy() { return 10; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public Animation getBulletAnimation() { return bulletAnimation; }

	@Override
	public float getSpeed() { return 50; }
	@Override
	public float getMaxSpeed() { return 100; }

	@Override
	protected float createAngle()
	{
		if(bulletsInMagazine == getBurstSize())
		{
			burstAngle = (RandomUtil.is() ? 90 : -90) + ( 30 - RandomUtil.N( 15 ) );
		}
		
		return RandomUtil.STD( burstAngle + weaponDir.angle(), getAccuracy());
	}

	@Override
	public Effect createHitEffect(Bullet bullet)
	{
		if(RandomUtil.oneOf( 5 ))
			return Effect.getEffect( HIT_01_AID, 
					RandomUtil.STD( 25, 4 ), bullet.getBody().getAnchor(), RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 1 );
		else
			return Effect.getEffect( HIT_02_AID, 
					RandomUtil.STD( 10, 2 ), bullet.getBody().getAnchor(), RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 1 );

	}

	@Override
	public float getLifeDuration()
	{
		return 50;
	}

	@Override
	public Damage getDamage()
	{
		return bulletDamage;
	}
}
