/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Level;

/**
 * @author dveyarangi
 *
 */
public class Weapon
{
	//////////////////////////////////
	// properties
	public final float speed = 100;
	
	public final int size = 4;
	
	private int burstSize = 7;
	private float magazineReloadTime = 1f;
	private float reloadingTime = 0.05f;
	
	private float accuracy = 360f;
	
	public final IBulletBehavior bulletBehavior = new HomingBehavior(this);
//	private IBulletBehavior bulletBehavior = new MassDriverBehavior();
	
	public final Sprite bulletSprite;
	
	////////////////////////////////
	// state
	
	private Vector2 weaponDir;
	
	private float timeToReload = 0;
	
	private int bulletsInMagazine = burstSize;
	
	public Weapon()
	{
		this.weaponDir = new Vector2();
		bulletSprite = new Sprite(GameFactory.loadTexture("models/rocket.png"));
		bulletSprite.setOrigin( bulletSprite.getWidth()/2, bulletSprite.getHeight()/2 );
		bulletSprite.setScale( size / bulletSprite.getWidth() );

	}
	
	public Bullet createBullet(Level level, Vector2 weaponPos, Vector2 targetPos)
	{
		if(timeToReload > 0)
		{
			return null;
		}
		if(bulletsInMagazine == 0)
			bulletsInMagazine = burstSize;
		
		weaponDir.set( targetPos ).sub( weaponPos ).nor();
		float angle = RandomUtil.STD( weaponDir.angle(), accuracy);
		weaponDir.setAngle( angle );
		
		Bullet bullet = Bullet.getBullet( level, this, 
				weaponPos.x, weaponPos.y, 
				weaponDir.x * speed, weaponDir.y*speed,
				targetPos);
		
		bulletsInMagazine --;
		if(bulletsInMagazine > 0)
			timeToReload = reloadingTime;
		else
			timeToReload = magazineReloadTime;
		
		return bullet;
	}
	
	public void update( float delta )
	{
		timeToReload -= delta;
	}
}
