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
public class Minigun extends IWeapon
{
	
	
	private static final String HIT_EFFECT_ATLAS_FILE = "anima//effects//explosion//explosion04.atlas";
	private static final String HIT_EFFECT_ATLAS_ID = "explosion04";
	
	private Sprite bulletSprite;
	
	private IBulletBehavior bulletBehavior;
	
	private Damage bulletDamage;
	
	public Minigun()
	{
		bulletSprite = new Sprite(GameFactory.loadTexture("models/fireball.png"));
		bulletSprite.setOrigin( bulletSprite.getWidth()/2, bulletSprite.getHeight()/2 );
		bulletSprite.setScale( getSize() / bulletSprite.getWidth() );
		
		bulletBehavior = new MassDriverBehavior();
		
		bulletDamage = new Damage();
	}


	@Override
	public int getSize() { return 1; }


	@Override
	public int getBurstSize() { return 1; }

	@Override
	public float getMagazineReloadTime() { return 0.07f; }

	@Override
	public float getReloadingTime() { return 0.07f; }

	@Override
	public float getAccuracy() { return 0.5f; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public Sprite getBulletSprite() { return bulletSprite; }

	@Override
	public float getSpeed() { return 100; }


	@Override
	protected float createAngle()
	{
		return RandomUtil.STD( weaponDir.angle(), getAccuracy());
	}


	@Override
	public Effect createHitEffect(Bullet bullet)
	{
		return Effect.getEffect( HIT_EFFECT_ATLAS_FILE, HIT_EFFECT_ATLAS_ID, 
				10, bullet.getBody().getAnchor(), RandomUtil.N( 360 ), 3 );
	}

	@Override
	public float getLifeDuration()
	{
		return 1;
	}


	@Override
	public Damage getDamage()
	{
		return bulletDamage;
	}
}
