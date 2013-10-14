/**
 * 
 */
package eir.world.unit.spider;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.NavEdge;
import eir.world.environment.NavNode;
import eir.world.unit.Bullet;
import eir.world.unit.weapon.HomingLauncher;
import eir.world.unit.weapon.IWeapon;

/**
 * Spider
 *
 */
public class Spider
{
	private int ownerId;
	
	private Level level;
	
	private float size;
	
	private Vector2 position;

	private Vector2 axis;
	
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
	
	private Chassis chassis;
	
	private static Sprite spiderTexture = GameFactory.createSprite( "anima//spider//spider_body.png" );

	public Spider(int ownerId, Level level, Asteroid asteroid, float surfaceIdx, float size, float speed)
	{
		this.ownerId = ownerId;
		this.level = level;
		this.asteroid = asteroid;
		
		this.size = size;
		this.speed = 10*speed;
		
		position = new Vector2();
		
		this.surfaceIdx = surfaceIdx;
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
		
		sprite = new Sprite(GameFactory.loadTexture( "models/spider_placeholder.png" ));
		sprite.setOrigin( sprite.getWidth()/2, sprite.getHeight()/2 );
		sprite.setScale( size / sprite.getWidth() );
		
//		weapon = new Minigun();
		weapon = new HomingLauncher(this);
		
		axis = new Vector2();
		
		this.chassis = new Chassis( this, position );
	}
	
	public int getOwnerId() { return ownerId; }
	
	public void update(float delta)
	{
		
		if(walkCW || walkCCW)
		{
			if(web != null)
				stepFromWeb();
			
			if(web == null)
			{
				chassis.step(walkCW);
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
					axis.set( web.getDirection() );
				}
			}
		}
		
		chassis.update( delta );
	
		
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
			asteroid = web.getNode1().getDescriptor().getObject();
			surfaceIdx = web.getNode1().getDescriptor().getIndex();
			
			web = null;
		}
		else
		if(web.getNode2().getPoint().dst2( position ) < 10)
		{
			asteroid = web.getNode2().getDescriptor().getObject();
			surfaceIdx = web.getNode2().getDescriptor().getIndex();
			
			web = null;
		}
	}
	
	public NavNode getClosestNode()
	{
		return asteroid.getModel().getNavNode( 
				Math.round( surfaceIdx ) % asteroid.getModel().getSize() );
	}


	public void walkCW(boolean walk) 
	{ 
		this.walkCW = walk;
//		if(walk == false)
//			stepCount = 0;
	}
	public void walkCCW(boolean walk) 
	{ 
		this.walkCCW = walk; 
//		if(walk == false)
//			stepCount = 0;
	}
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
	public void draw(SpriteBatch batch, ShapeRenderer shape)
	{
/*		sprite.setPosition( position.x-sprite.getOriginX(), position.y-sprite.getOriginY() );
		sprite.setRotation( axis.angle() + 90 );
		sprite.draw( batch );*/
		
		chassis.draw(batch, shape);
				
		batch.begin();
//		spiderTexture.setRotation( weapon.get )
		batch.draw( spiderTexture, position.x-2.5f, position.y-2f, 4, 4 );
		batch.end();
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

	/**
	 * @return
	 */
	public Vector2 getAxis() { return axis; }

	public Asteroid getAsteroid() { return asteroid; }

	/**
	 * @return
	 */
	float getSurfaceIdx() { return surfaceIdx; }
}
