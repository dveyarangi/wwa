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

import eir.resources.GameFactory;

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
	
	public static Effect getEffect(int animationId, float size, Vector2 position, float angle, float timeModifier)
	{
		Effect effect = pool.obtain();
		
		effect.reset();
		
		effect.animation = GameFactory.getAnimation( animationId );
		
		effect.size = size;
		effect.position.set( position );
		effect.angle = angle;
		effect.timeModifier = timeModifier;
		
		return effect;
	}
		
	public static void free(Effect effect)
	{
		pool.free( effect );
	}
	
	private Animation animation;
	
	private boolean isAlive = true;
	
	private Vector2 position;
	
	private float angle;
	
	private float size;	
	
	private float stateTime;
	
	private float timeModifier;
	
	public Effect()
	{
		position = new Vector2();
	}
	
	public void reset()
	{
		stateTime = 0;
		isAlive = true;
	}
	
	public void update( float delta )
	{
		stateTime += delta*timeModifier;
		if(stateTime > animation.animationDuration)
			isAlive = false;
	}
	
	public boolean isAlive() { return isAlive; }
	
	public void draw( SpriteBatch batch )
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
