/**
 *
 */
package eir.world.unit.spider;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.weapon.HomingLauncher;
import eir.world.unit.weapon.IWeapon;
/**
 * Spider
 *
 */
public class Spider extends Unit
{
	private Faction faction;

	private final float size = 10;

	private Vector2 axis;

	private float speed;

	private Asteroid asteroid;
	private float surfaceIdx;
	private float webIdx;

	private Sprite sprite;


	private boolean walkCW, walkCCW, walkUp, walkDown;

	private Vector2 shootingTarget;

	private IWeapon weapon;

	private NavEdge web;

	private Chassis chassis;

	private static Sprite spiderTexture = GameFactory.createSprite( "anima//spider//spider_body.png" );

	private UnitsFactory unitsFactory;

	public Spider(final UnitsFactory unitsFactory)
	{
		super();
		this.unitsFactory = unitsFactory;
	}

	@Override
	protected void init( )
	{
		super.init();
		this.speed = 10*speed;


		asteroid = (Asteroid) anchor.getDescriptor().getObject();
		asteroid.getModel().getSurfacePoint( surfaceIdx, getBody().getAnchor() );

		sprite = new Sprite(GameFactory.loadTexture( "models/spider_placeholder.png" ));
		sprite.setOrigin( sprite.getWidth()/2, sprite.getHeight()/2 );
		sprite.setScale( size / sprite.getWidth() );

		//		weapon = new Minigun(this);
		weapon = new HomingLauncher(this);

		axis = new Vector2();
		this.chassis = new Chassis( this, getBody().getAnchor(), getBody().getAnchor() );
	}

	@Override
	public Faction getFaction() { return faction; }

	@Override
	public void update(final float delta)
	{

		if(walkCW || walkCCW)
		{
			if(web != null)
			{
				stepFromWeb();
			}

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
				{
					step =  delta*speed;
				} else
				{
					step = -delta*speed;
				}
				if(web != null)
				{
					webIdx += step/web.getLength();
					if(webIdx < 0)
					{
						webIdx = 0;
					}
					if(webIdx > 1)
					{
						webIdx = 1;
					}

					getBody().getAnchor().set(web.getDirection())
					.mul(webIdx*web.getLength())
					.add( web.getNode1().getPoint() );
					axis.set( web.getDirection() );
				}
			}
		}

		asteroid.getModel().getNavNode(
				Math.round( surfaceIdx ) % asteroid.getModel().getSize() );

		chassis.update( delta );

		// floating toward chassis center point
		getBody().getAnchor().add( chassis.getPosition().tmp().sub( getBody().getAnchor() ).div( 10 ) );
		//		leftLegJoint.set(targetPosition);
		//		rightLegJoint.set(targetPosition);

		weapon.update( delta );

		if(shootingTarget != null)
		{
			weapon.fire( shootingTarget );
		}
	}

	public void stepFromWeb()
	{
		if(web.getNode1().getPoint().dst2( getBody().getAnchor() ) < 10d)
		{
			asteroid = web.getNode1().getDescriptor().getObject();
			surfaceIdx = web.getNode1().getDescriptor().getIndex();

			web = null;
		}
		else
			if(web.getNode2().getPoint().dst2( getBody().getAnchor() ) < 10)
			{
				asteroid = web.getNode2().getDescriptor().getObject();
				surfaceIdx = web.getNode2().getDescriptor().getIndex();

				web = null;
			}
	}

	private SurfaceNavNode getClosestNode()
	{
		return anchor;
	}


	@Override
	public void walkCW(final boolean walk)
	{
		this.walkCW = walk;
		//		if(walk == false)
		//			stepCount = 0;
	}
	@Override
	public void walkCCW(final boolean walk)
	{
		this.walkCCW = walk;
		//		if(walk == false)
		//			stepCount = 0;
	}
	@Override
	public void walkUp(final boolean walk)
	{
		if(web == null)
		{
			NavEdge walkingEdge = null;
			for(NavNode node : getClosestNode().getNeighbors())
			{
				NavEdge edge = faction.getEnvironment().getGroundMesh().getEdge( getClosestNode(), (SurfaceNavNode) node);
				if(edge.getType() == NavEdge.Type.WEB)
				{
					walkingEdge = edge;
					break;
				}
			}

			web = walkingEdge;
			if(web != null)
			{
				webIdx = 0;
			}
		}

		if(web != null)
		{
			this.walkUp = walk;
		}
	}
	@Override
	public void walkDown(final boolean walk)
	{
		if(web != null)
		{
			this.walkDown = walk;
		}
	}

	/**
	 * Debug rendering method
	 * @param shape
	 */
	@Override
	public void draw(final SpriteBatch batch)
	{
		/*		sprite.setPosition( position.x-sprite.getOriginX(), position.y-sprite.getOriginY() );
		sprite.setRotation( axis.angle() + 90 );
		sprite.draw( batch );*/

		chassis.draw(batch);

		//		spiderTexture.setRotation( weapon.get )
		batch.draw( spiderTexture, getBody().getAnchor().x-2.5f, getBody().getAnchor().y-2f, 4, 4 );
	}

	/**
	 * @param b
	 */
	@Override
	public void setShootingTarget(final Vector2 targetPos)
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


	@Override
	public float getSize() {
		return size;
	}

}
