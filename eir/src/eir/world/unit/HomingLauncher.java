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
	
	private static final String BULLET_ATLAS_FILE_01 = "anima//bullets//rocket01.atlas";
	private static final String BULLET_ATLAS_ID_01 = "bullet";
	private static final String HIT_EFFECT_ATLAS_FILE_01 = "anima//effects//explosion//explosion03.atlas";
	private static final String HIT_EFFECT_ATLAS_ID_01 = "explosion03";
	private static final String HIT_EFFECT_ATLAS_FILE_02 = "anima//effects//explosion//explosion05.atlas";
	private static final String HIT_EFFECT_ATLAS_ID_02 = "explosion05";
	
	public HomingLauncher()
	{
		bulletAnimation = GameFactory.loadAnimation(
				BULLET_ATLAS_FILE_01, BULLET_ATLAS_ID_01 );
		
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
	public float getAccuracy() { return 20; }

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
		return RandomUtil.STD( weaponDir.angle(), getAccuracy());
			//+ 20 + (getBurstSize() - bulletsInMagazine) * 6;
	}

	@Override
	public Effect createHitEffect(Bullet bullet)
	{
		if(RandomUtil.oneOf( 5 ))
			return Effect.getEffect( HIT_EFFECT_ATLAS_FILE_01, HIT_EFFECT_ATLAS_ID_01, 
					RandomUtil.STD( 25, 4 ), bullet.getBody().getAnchor(), RandomUtil.N( 360 ), Math.abs( RandomUtil.STD( 0, 0.5f )) + 1 );
		else
			return Effect.getEffect( HIT_EFFECT_ATLAS_FILE_02, HIT_EFFECT_ATLAS_ID_02, 
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
