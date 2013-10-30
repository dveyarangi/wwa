/**
 * 
 */
package eir.world.unit.weapon;


import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;

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
	
	public Minigun(Unit unit)
	{
		
		super(unit); 
//		bulletSprite = new Sprite(GameFactory.loadTexture("models/fireball.png"));
//		bulletSprite.setOrigin( bulletSprite.getWidth()/2, bulletSprite.getHeight()/2 );
//		bulletSprite.setScale( getSize() / bulletSprite.getWidth() );
		
		bulletAnimation = GameFactory.getAnimation( BULLET_AID );
		
		bulletBehavior = new MassDriverBehavior();
		
		bulletDamage = new Damage();
	}


	@Override
	public float getSize() { return 1; }


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
	public Animation getBulletAnimation() { return bulletAnimation; }

	@Override
	public float createSpeed() { return 100; }


	@Override
	protected float createAngle()
	{
		return RandomUtil.STD( weaponDir.angle(), getAccuracy());
	}


	@Override
	public Effect createHitEffect(Bullet bullet)
	{
		return Effect.getEffect( HIT_01_AID, 
				10, bullet.getBody().getAnchor(), RandomUtil.N( 360 ), 3 );
	}
	
	@Override
	public Effect createTraceEffect(Bullet bullet) { return null; }


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
