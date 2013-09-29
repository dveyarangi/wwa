/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;

/**
 * @author dveyarangi
 *
 */
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
	
	private static Texture crosshair = GameFactory.loadTexture( "skins/crosshair.png" );

	
	/**
	 * level object id
	 */
	private int id;
	
	private int size = 1;

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
	
	float angle;
	
	float lifetime;
	
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
		Sprite sprite = weapon.getBulletSprite();
		sprite.setPosition( body.getCenterX()-sprite.getOriginX(), 
							body.getCenterY()-sprite.getOriginY());
		sprite.setRotation( angle );
		
		sprite.draw( batch );
		
//		if(weapon.getBulletBehavior().requiresTarget() && target != null)
//			batch.draw( crosshair, target.x-2, target.y-2, 4, 4);
	}

	Vector2 getVelocity() { return velocity; }
	AABB getBody() { return body; }
	Vector2 getTarget() { return target; }
	void setIsAlive(boolean isAlive) { this.isAlive = false; }

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
