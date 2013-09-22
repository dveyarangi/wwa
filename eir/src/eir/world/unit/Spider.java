/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Asteroid;

/**
 * Spider
 *
 */
public class Spider
{
	private float size;
	
	private Vector2 position;
	
	/**
	 * currently traversed asteroid, maybe null in transition
	 */
	private Asteroid asteroid;
	
	private float speed;

	
	private float surfaceIdx;
	
	private Sprite sprite;
	
	
	public Spider(Asteroid asteroid, float size, float surfaceIdx, float speed)
	{
		this.asteroid = asteroid;
		
		this.size = size;
		this.speed = speed;
		this.surfaceIdx = surfaceIdx;
		position = new Vector2();
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
		
		sprite = new Sprite(GameFactory.loadTexture( "models/spider_placeholder.png" ));
		sprite.setOrigin( sprite.getWidth()/2, sprite.getHeight()/2 );
		sprite.setScale( size / sprite.getWidth() );
		
	}
	
	public void update(float delta)
	{
		surfaceIdx = asteroid.getModel().getStepSurfaceIndex( surfaceIdx, delta*speed );
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
	}

	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(SpriteBatch batch)
	{
		sprite.setPosition( position.x-sprite.getOriginX(), position.y-sprite.getOriginY() );
		sprite.setRotation( asteroid.getModel().getNormal(surfaceIdx).angle() + 90 );
		sprite.draw( batch );
	}
}
