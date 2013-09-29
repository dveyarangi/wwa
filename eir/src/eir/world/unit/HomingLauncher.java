/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.Sprite;

import eir.resources.GameFactory;

/**
 * @author dveyarangi
 *
 */
public class HomingLauncher extends Minigun
{
	private Sprite bulletSprite;
	
	private IBulletBehavior bulletBehavior;
	
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
	public float getAccuracy() { return 10; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public Sprite getBulletSprite() { return bulletSprite; }

	@Override
	public float getSpeed() { return 100; }
}
