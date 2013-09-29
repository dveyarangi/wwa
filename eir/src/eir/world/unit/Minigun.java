/**
 * 
 */
package eir.world.unit;


import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Sprite;

import eir.resources.GameFactory;

/**
 * @author dveyarangi
 *
 */
public class Minigun extends IWeapon
{
	
	private Sprite bulletSprite;
	
	private IBulletBehavior bulletBehavior;
	
	public Minigun()
	{
		bulletSprite = new Sprite(GameFactory.loadTexture("models/fireball.png"));
		bulletSprite.setOrigin( bulletSprite.getWidth()/2, bulletSprite.getHeight()/2 );
		bulletSprite.setScale( getSize() / bulletSprite.getWidth() );
		
		bulletBehavior = new MassDriverBehavior();
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
}
