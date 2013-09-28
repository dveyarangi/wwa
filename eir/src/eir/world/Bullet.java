/**
 * 
 */
package eir.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
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
	
	public static Bullet getBullet(Level level, String modelId, float size, float x, float y, float dx, float dy)
	{

		Bullet bullet = pool.obtain();
		
		bullet.id = level.createObjectId();
		
		bullet.body.getAnchor().set( x, y );
		bullet.body.getDimensions().set( bullet.size, bullet.size );
		
		bullet.velocity.set(dx, dy);
		
		if(bullet.sprite == null)
			bullet.sprite = GameFactory.createSprite(modelId, 
					bullet.body.getAnchor(), 0.5f, 0.5f, size, size, 0);

		return bullet;
	}
	
	public static void free(Bullet bullet)
	{
		pool.free( bullet );
	}
	
	//////////////////////////////////////////////////////////////////
	
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
	
	private Bullet()
	{
		body = AABB.createSquare(0, 0, 0);
		
		velocity = Vector2.Zero.cpy();
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
		float dx = velocity.x * delta;
		float dy = velocity.y * delta; 
		body.getAnchor().add( velocity.x * delta, velocity.y * delta );
		sprite.setPosition( sprite.getX()+dx, sprite.getY() + dy);
	}
	
	public void draw( SpriteBatch batch )
	{
		sprite.draw( batch );
	}

}
