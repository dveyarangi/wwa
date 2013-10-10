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
	
	private static final int LEGS = 8;
	private Vector2 leftLegJoint;
	private Vector2 rightLegJoint;
	private Leg [] legs;
	private int stepCount = LEGS;
	private float timeToStep = 0;
	private float stepInterval = 0.2f;
	
	public Spider(int ownerId, Level level, Asteroid asteroid, float surfaceIdx, float size, float speed)
	{
		this.ownerId = ownerId;
		this.level = level;
		this.asteroid = asteroid;
		
		this.size = size;
		this.speed = 10*speed;
		this.surfaceIdx = surfaceIdx;
		position = new Vector2();
		asteroid.getModel().getSurfacePoint( surfaceIdx, position );
		
		sprite = new Sprite(GameFactory.loadTexture( "models/spider_placeholder.png" ));
		sprite.setOrigin( sprite.getWidth()/2, sprite.getHeight()/2 );
		sprite.setScale( size / sprite.getWidth() );
		
//		weapon = new Minigun();
		weapon = new HomingLauncher(this);
		
		axis = new Vector2();
		leftLegJoint = new Vector2();
		rightLegJoint = new Vector2();
		
		legs = new Leg[LEGS];
		for(int idx = 0; idx < LEGS; idx ++)
		{
			int left = idx > LEGS/2-1 ? 1 : -1;
			legs[idx] = new Leg( this, left > 0 ? leftLegJoint : rightLegJoint, 
						asteroid.getModel().getStepSurfaceIndex(
								surfaceIdx,  (left < 0 ? 0 : 7) + (  2.5f*(idx) ) ), left > 0);
		}
	}
	
	public int getOwnerId() { return ownerId; }
	
	public void update(float delta)
	{
		timeToStep -= delta;
		if(timeToStep < 0)
			timeToStep = 0;
		
		for(int idx = 0; idx < LEGS; idx ++)
		{
			legs[idx].update( delta );
		}
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
				
				if(!legs[(stepCount-1+LEGS)%LEGS].isStepping())
				{
					
					int legIdx = stepCount % LEGS;
					Leg leg = legs[legIdx];
					
					leg.startStepTo( asteroid.getModel().getStepSurfaceIndex(leg.getSurfaceIdx(), walkCW ? -2.5f : 2.5f ) );
					
					timeToStep = stepInterval;
					stepCount ++;
				}
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
		
		Vector2 offset = Vector2.tmp.set(0,0);
		position.set( 0,0 );
		for(Leg leg : legs)
		{
			offset.add( Vector2.tmp2.set( leg.getAncleJoint() ).sub( leg.getToeJoint() ) );
			position.add( leg.getAncleJoint() );
		}
		position.div( LEGS ).add( offset.div(LEGS).mul( 5 ) );
		leftLegJoint.set(position); 
		rightLegJoint.set(position); 
		
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
		if(walk == false)
			stepCount = 0;
	}
	public void walkCCW(boolean walk) 
	{ 
		this.walkCCW = walk; 
		if(walk == false)
			stepCount = 0;
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
		
		for(Leg leg : legs)
			leg.draw( batch, shape );
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
}
