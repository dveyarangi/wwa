/**
 * 
 */
package eir.world;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
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
	
	public static Bullet getBullet(Level level, float x, float y, float dx, float dy)
	{

		Bullet bullet = pool.obtain();
		
		bullet.id = level.createObjectId();
		
		bullet.body.getAnchor().set( x, y );
		bullet.body.getDimensions().set( bullet.size, bullet.size );
		
		bullet.velocity.set(dx, dy);
		
		if(bullet.animation == null)
			bullet.animation = GameFactory.loadAnimation( 
					RandomUtil.oneOf(2) ?
							"anima//ant//blob_black.atlas" : 
							"anima//ant//blob_black.atlas", 
							"blob" );

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
	
	
	private Animation animation;

	
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

}
