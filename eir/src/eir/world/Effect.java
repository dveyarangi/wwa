/**
 *
 */
package eir.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.google.common.base.Preconditions;

/**
 * @author dveyarangi
 *
 */
public class Effect implements Poolable
{

	////////////////////////////////////////////////////
	private static Pool<Effect> pool = new Pool<Effect> () {

		@Override
		protected Effect newObject()
		{
			return new Effect();
		}
	};

	public static Effect getEffect( final Animation animation, final float size, final Vector2 position, final Vector2 velocity, final float angle, final float timeModifier)
	{
		Effect effect = pool.obtain();

		effect.reset();

		effect.animation = Preconditions.checkNotNull( animation );

		effect.size = size;
		effect.position.set( position );
		effect.velocity.set( velocity );
		effect.angle = angle;
		effect.timeModifier = timeModifier;

		return effect;
	}
	public static Effect getEffect( final Animation animation, final float size, final Vector2 position, final float angle, final float timeModifier)
	{
		return getEffect( animation, size, position, Vector2.Zero, angle, timeModifier );
	}

	public static void free(final Effect effect)
	{
		pool.free( effect );
	}

	private Animation animation;

	private boolean isAlive = true;

	private Vector2 position;

	private Vector2 velocity;

	private float angle;

	private float size;

	private float stateTime;

	private float timeModifier;

	public Effect()
	{
		position = new Vector2();
		velocity = new Vector2();
	}

	@Override
	public void reset()
	{
		stateTime = 0;
		isAlive = true;
	}

	public void update( final float delta )
	{
		stateTime += delta*timeModifier;
		if(stateTime > animation.animationDuration)
		{
			isAlive = false;
		}

		position.add( velocity.tmp().mul( delta ) );
	}

	public boolean isAlive() { return isAlive; }

	public void draw( final SpriteBatch batch )
	{
		TextureRegion region = animation.getKeyFrame( stateTime, true );
		batch.draw( region,
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2,
				region.getRegionWidth(), region.getRegionHeight(),
				size/region.getRegionWidth(),
				size/region.getRegionWidth(), angle);
	}


}
