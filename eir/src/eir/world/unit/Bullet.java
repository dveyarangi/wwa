/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.weapon.IWeapon;

/**
 * @author dveyarangi
 *
 */
@SuppressWarnings("unused")
public class Bullet implements Poolable, ISpatialObject
{
	//////////////////////////////////////////////////////////////////
	// life cycle methods
	//////////////////////////////////////////////////////////////////

	private static Pool<Bullet> pool = new Pool<Bullet> () {

		@Override
		protected Bullet newObject()
		{
			return new Bullet();
		}
	};
	
	public static Bullet getBullet(Level level, IWeapon weapon, float x, float y, float dx, float dy, Vector2 target)
	{

		Bullet bullet = pool.obtain();
		bullet.reset();
		bullet.id = level.createObjectId();
		bullet.size = weapon.getSize();
		
		bullet.body.getAnchor().set( x, y );
		bullet.body.getDimensions().set( bullet.size, bullet.size );
		
		bullet.velocity.set(dx, dy);
		bullet.target.set( target );
		bullet.weapon = weapon;
		return bullet;
	}
	
	public static void free(Bullet bullet)
	{
		pool.free( bullet );
	}
	
	//////////////////////////////////////////////////////////////////
	
	private static int crosshairAnimationId = GameFactory.registerAnimation("anima//ui//crosshair02.atlas", "crosshair");
	private static Animation crosshair = GameFactory.getAnimation( crosshairAnimationId );

	
	/**
	 * level object id
	 */
	private int id;
	
	private float size = 1;

	/**
	 * Collision box
	 */
	private AABB body;
	
	/**
	 * Bullet velocity
	 */
	private Vector2 velocity;
	
	private boolean isAlive;
	
	private Vector2 target;
	
	public IWeapon weapon;
	
	public float angle;
	
	public float lifetime;
	
	private Bullet()
	{
		body = AABB.createSquare(0, 0, 0);
		
		velocity = Vector2.Zero.cpy();
		
		target = Vector2.Zero.cpy();
	}

	
	@Override
	public AABB getArea()
	{
		return body;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public void reset()	
	{ 
		this.isAlive = true;
		this.lifetime = 0;
	}

	/**
	 * @param delta
	 */
	public void update(float delta)
	{
		lifetime += delta;
		if(lifetime > weapon.getLifeDuration())
		{
			isAlive = false;
			return;
		}
		weapon.getBulletBehavior().update( delta, this );
	}
	
	public void draw( SpriteBatch batch )
	{
		Vector2 position = body.getAnchor();
		TextureRegion region = weapon.getBulletAnimation().getKeyFrame( lifetime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle+90);
		
		if(weapon.getBulletBehavior().requiresTarget() && target != null)
		{
			TextureRegion crossHairregion = crosshair.getKeyFrame( lifetime, true );
			batch.draw( crossHairregion, 
					target.x-crossHairregion.getRegionWidth()/2, target.y-crossHairregion.getRegionHeight()/2,
					crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2, 
					crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(), 
					5f/crossHairregion.getRegionWidth(), 
					5f/crossHairregion.getRegionWidth(), angle);
		}
		
		Effect effect = weapon.createTraceEffect(this);
		if(effect != null)
			weapon.getLevel().addEffect( effect );

	}

	public Vector2 getVelocity() { return velocity; }
	public AABB getBody() { return body; }
	public Vector2 getTarget() { return target; }
	public void setIsAlive(boolean isAlive) { this.isAlive = false; }

	/**
	 * @return
	 */
	public boolean isAlive() { return isAlive; }

	/**
	 * @return
	 */
	public Damage getDamage()
	{
		isAlive = false;
		return weapon.getDamage();
	}
}
