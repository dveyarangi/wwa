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
	
	private Vector2 shootingTarget;
	
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
		
		weapon.update( delta );
		
		if(shootingTarget != null)
		{
			Bullet bullet = weapon.createBullet( level, position, shootingTarget );
			if(bullet != null) // is reloaded
			level.shoot( this, bullet);
		}
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
	 * @return
	 */
	public Vector2 getPosition()
	{
		return position;
	}

	/**
	 * @param b
	 */
	public void setShootingTarget(Vector2 targetPos)
	{
		this.shootingTarget = targetPos;
	}


}
