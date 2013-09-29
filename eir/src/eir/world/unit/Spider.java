/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.NavEdge;
import eir.world.environment.NavNode;

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
	private float webIdx;
	
	private Sprite sprite;
	
	
	private boolean walkCW, walkCCW, walkUp, walkDown;
	
	private Vector2 shootingTarget;
	
	private IWeapon weapon;
	
	private NavEdge web;
	
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
		
//		weapon = new Minigun();
		weapon = new HomingLauncher();
	}
	
	public void update(float delta)
	{
		if(walkCW || walkCCW)
		{
			float step;
			
			if(web != null)
				stepFromWeb();
			
			if(web == null)
			{
				if(walkCW)
					step = -delta*speed;
				else
					step =  delta*speed;
			
				surfaceIdx = asteroid.getModel().getStepSurfaceIndex( surfaceIdx, step );
				
				asteroid.getModel().getSurfacePoint( surfaceIdx, position );
			}
		}
		else
		{
			if(walkUp || walkDown)
			{
				float step;
				if(walkUp)
					step =  delta*speed;
				else
					step = -delta*speed;
				if(web != null)
				{
					webIdx += step/web.getLength();
					if(webIdx < 0)
						webIdx = 0;
					if(webIdx > 1)
						webIdx = 1;
						
					position.set(web.getDirection())
							.mul(webIdx*web.getLength())
							.add( web.getNode1().getPoint() );					
				}
			}
		}
		
		weapon.update( delta );
		
		if(shootingTarget != null)
		{
			Bullet bullet = weapon.createBullet( level, position, shootingTarget );
			if(bullet != null) // is reloaded
			level.shoot( this, bullet);
		}
	}
	
	public void stepFromWeb()
	{
		if(web.getNode1().getPoint().dst2( position ) < 10d)
		{
			asteroid = web.getNode1().getAsteroid();
			surfaceIdx = web.getNode1().getAsteroidIdx();
			
			web = null;
		}
		else
		if(web.getNode2().getPoint().dst2( position ) < 10)
		{
			asteroid = web.getNode2().getAsteroid();
			surfaceIdx = web.getNode2().getAsteroidIdx();
			
			web = null;
		}
	}
	
	public NavNode getClosestNode()
	{
		return asteroid.getModel().getNavNode( 
				Math.round( surfaceIdx ) % asteroid.getModel().getSize() );
	}


	public void walkCW(boolean walk) { this.walkCW = walk; }
	public void walkCCW(boolean walk) { this.walkCCW = walk; }
	public void walkUp(boolean walk) 
	{ 
		if(web == null)
		{
			NavEdge walkingEdge = null; 
			for(NavNode node : getClosestNode().getNeighbors())
			{
				NavEdge edge = level.getNavMesh().getEdge( getClosestNode(), node);
				if(edge.getType() == NavEdge.Type.WEB)
				{
					walkingEdge = edge;
					break;
				}
			}
		
			web = walkingEdge;
			if(web != null)
				webIdx = 0;
		}
		
		if(web != null)
			this.walkUp = walk; 
	}
	public void walkDown(boolean walk) 
	{ 
		if(web != null)
			this.walkDown = walk; 
	}
	
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(SpriteBatch batch)
	{
		sprite.setPosition( position.x-sprite.getOriginX(), position.y-sprite.getOriginY() );
		if(web == null)
			sprite.setRotation( asteroid.getModel().getNormal(surfaceIdx).angle() + 90 );
		else
			sprite.setRotation( web.getDirection().angle() );
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
