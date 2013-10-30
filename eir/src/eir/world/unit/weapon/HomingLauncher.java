/**
 * 
 */
package eir.world.unit.weapon;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.unit.Damage;
import eir.world.unit.spider.Spider;

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
	
	private Spider spider;
	
		
	private static final int BULLET_AID = GameFactory.registerAnimation( "anima//bullets//rocket01.atlas", "bullet" );
	private static final int TRAIL_AID = GameFactory.registerAnimation( "anima//effects//smoke//smoke.atlas", "smoke" );
	private static final int HIT_01_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion03.atlas", "explosion03" );
	private static final int HIT_02_AID = GameFactory.registerAnimation( "anima//effects//explosion//explosion05.atlas", "explosion05" );
	
	public HomingLauncher(Spider spider)
	{
		super(spider.getFaction());
		
		this.spider = spider;
		
		bulletAnimation = GameFactory.getAnimation( BULLET_AID );
		
		bulletBehavior = new HomingBehavior(this);
		
		bulletDamage = new Damage();
	}
	
	@Override
	public float getSize() { return 1; }


	@Override
	public int getBurstSize() { return 8; }

	@Override
	public float getMagazineReloadTime() { return 1f; }

	@Override
	public float getReloadingTime() { return 0.05f; }

	@Override
	public float getAccuracy() { return 15; }

	@Override
	public IBulletBehavior getBulletBehavior() { return bulletBehavior; }

	@Override
	public Animation getBulletAnimation() { return bulletAnimation; }

	@Override
	public float createSpeed() { return RandomUtil.STD( 50, 10 ); }
	@Override
	public float getMaxSpeed() { return 100; }

	@Override
	protected float createAngle()
	{

//		burstAngle = (weaponDir.crs( spider.getAxis() ) < 0 ? 90 : -90) + 
//				( 15 - RandomUtil.N( 30 ) );
		int dir = (burstAngle < 0 ? 1 : -1);
		burstAngle = dir * 90 + dir * ( 15 + RandomUtil.STD( 0, 5 ) );
		
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
	
	public Effect createTraceEffect(Bullet bullet)
	{
		return Effect.getEffect( TRAIL_AID, RandomUtil.N( 4 ) + 2, bullet.getArea().getAnchor(), RandomUtil.N( 360 ), RandomUtil.STD( 2, 0.15f ) );
	}
}
