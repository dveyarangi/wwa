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
	
	public static Bullet getBullet(Level level, Weapon weapon, float x, float y, float dx, float dy, Vector2 target)
	{

		Bullet bullet = pool.obtain();
		
		bullet.id = level.createObjectId();
		bullet.isAlive = true;
		
		bullet.body.getAnchor().set( x, y );
		bullet.body.getDimensions().set( bullet.size, bullet.size );
		
		bullet.velocity.set(dx, dy);
		bullet.target.set( target );
		
		bullet.behavior = weapon.bulletBehavior;
		bullet.sprite = weapon.bulletSprite;
		bullet.size = weapon.size;
		
		bullet.lifetime = 0;
		
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
	
	private Sprite sprite;
	
	private IBulletBehavior behavior;
	
	private boolean isAlive;
	
	private Vector2 target;
	
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
	public void reset()	{ }

	/**
	 * @param delta
	 */
	public void update(float delta)
	{
		lifetime += delta;
		behavior.update( delta, this );
	}
	
	public void draw( SpriteBatch batch )
	{
		sprite.setPosition( body.getCenterX()-sprite.getOriginX(), 
							body.getCenterY()-sprite.getOriginY());
		sprite.setRotation( angle );
		
		sprite.draw( batch );
		
		if(behavior.requiresTarget() && target != null)
			batch.draw( crosshair, target.x-2, target.y-2, 4, 4);
	}

	Vector2 getVelocity() { return velocity; }
	AABB getBody() { return body; }
	Vector2 getTarget() { return target; }
	void setIsAlive(boolean isAlive) { this.isAlive = false; }

	/**
	 * @return
	 */
	public boolean isAlive() { return isAlive; }
}
