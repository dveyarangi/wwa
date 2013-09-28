/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.Bullet;
import eir.world.Level;

/**
 * Spider
 *
 */
public class Spider
{
	private Level level;
	
	private float size;
	
	private Vector2 position;

	
	/**
	 * currently traversed asteroid, maybe null in transition
	 */
	private Asteroid asteroid;
	
	private float speed;

	
	private float surfaceIdx;
	
	private Sprite sprite;
	
	
	private boolean walkCW, walkCCW, walkUp, walkDown;
	
	private Weapon weapon;
	
	public Spider(Level level, Asteroid asteroid, float surfaceIdx, float size, float speed)
	{
		
		this.level = level;
		this.asteroid = asteroid;
		
		this.size = size;
		this.speed = speed;
		this.surfaceIdx = surfaceIdx;
		position = new Vector2();
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
		
		sprite = new Sprite(GameFactory.loadTexture( "models/spider_placeholder.png" ));
		sprite.setOrigin( sprite.getWidth()/2, sprite.getHeight()/2 );
		sprite.setScale( size / sprite.getWidth() );
		
		weapon = new Weapon();
	}
	
	public void update(float delta)
	{
		if(walkCW)
			surfaceIdx = asteroid.getModel().getStepSurfaceIndex( surfaceIdx, -delta*speed );
		if(walkCCW)
			surfaceIdx = asteroid.getModel().getStepSurfaceIndex( surfaceIdx, delta*speed );
		
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
	}


	public void walkCW(boolean walk) { this.walkCW = walk; }
	public void walkCCW(boolean walk) { this.walkCCW = walk; }
	public void walkUp(boolean walk) { this.walkUp = walk; }
	public void walkDown(boolean walk) { this.walkDown = walk; }
	
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

	/**
	 * @param pointerPosition2
	 */
	public Bullet shoot(Vector2 targetPos)
	{
		
		return weapon.createBullet( level, position, targetPos );

	}

	/**
	 * @return
	 */
	public Vector2 getPosition()
	{
		return position;
	}


}
