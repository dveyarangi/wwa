/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Sprite;

import eir.resources.GameFactory;
import eir.world.Effect;

/**
 * @author dveyarangi
 *
 */
public class HomingLauncher extends IWeapon
{
	private Sprite bulletSprite;
	
	private IBulletBehavior bulletBehavior;
	
	private static final String HIT_EFFECT_ATLAS_FILE = "anima//effects//explosion//explosion02.atlas";
	private static final String HIT_EFFECT_ATLAS_ID = "explosion02";
	
	public HomingLauncher()
	{
		bulletSprite = new Sprite(GameFactory.loadTexture("models/rocket.png"));
		bulletSprite.setOrigin( bulletSprite.getWidth()/2, bulletSprite.getHeight()/2 );
		bulletSprite.setScale( getSize() / bulletSprite.getWidth() );
		
		bulletBehavior = new HomingBehavior(this);
	}
	
	@Override
	public int getSize() { return 4; }


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
	public Sprite getBulletSprite() { return bulletSprite; }

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

	/* (non-Javadoc)
	 * @see eir.world.unit.IWeapon#createHitEffect()
	 */
	@Override
	public Effect createHitEffect(Bullet bullet)
	{
		return Effect.getEffect( HIT_EFFECT_ATLAS_FILE, HIT_EFFECT_ATLAS_ID, 
				15, bullet.getBody().getAnchor(), RandomUtil.N( 360 ), 3 );
	}
}
