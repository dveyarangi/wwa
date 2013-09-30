/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.environment.NavEdge;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import eir.world.environment.Route;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;

/**
 * @author dveyarangi
 *
 */
public class Ant implements Poolable, ISpatialObject
{
	private static Pool<Ant> pool = new Pool<Ant> () {

		@Override
		protected Ant newObject()
		{
			return new Ant();
		}
		
	};
	
	public static Ant getAnt(Faction faction )
	{
		if(font == null)
		{
			 font = GameFactory.loadFont("skins//fonts//default", 0.05f);
		}
		
		Ant ant = pool.obtain();
		ant.reset();
		
		ant.faction = faction;
		
		ant.currNode = faction.getSpawnNode();
				
		ant.id = faction.getLevel().createObjectId();

		ant.body = AABB.createSquare( ant.currNode.getPoint().x, ant.currNode.getPoint().y, ant.size/2 );
		
		ant.mesh = faction.getLevel().getNavMesh();

		return ant;
	}
	
	private static final String HIT_EFFECT_ATLAS_FILE_02 = "anima//effects//explosion//explosion02.atlas";
	private static final String HIT_EFFECT_ATLAS_ID_02 = "explosion02";
	
	public static void free(Ant ant)
	{
		ant.body.free( );
		pool.free( ant );
	}
	
	private Faction faction;
	
	private static BitmapFont font;

	private int id;
	
	private AABB body;
	
	private NavMesh mesh;
	private NavNode currNode, nextNode, targetNode;
	private Route route;
	// offset from current node on the nav edge
	private float nodeOffset;
	
	private float stateTime;
	
	private float size = 5;
	private Vector2 velocity = new Vector2();
	private float angle;
	
	private float screamTime;
	
	private float speed = 10f;
	
	private boolean isAlive = true;
	
	private Damage damage = new Damage();

	private Ant()
	{
		stateTime = RandomUtil.R( 10 );
	}

	@Override
	public void reset()
	{
		screamTime = stateTime;
		route = null;
		nextNode = null;
		velocity.set( 0,0 );
		stateTime = RandomUtil.R( 10 );
		isAlive = true;

	}
	
	public void update(float delta)
	{

		if(nextNode == null)
		{
			do
			{
				// either we reached next node, or we do not have target
				if(route == null || !route.hasNext())
				{
					if(route != null)
						screamTime = stateTime;
					// pick a random target
					targetNode = mesh.getNode( RandomUtil.N( mesh.getNodesNum() ) );
					route = mesh.getShortestRoute( currNode, targetNode );
				}
	
				route.next(); // skipping the source
				if(!route.hasNext())
					route = null;
				else
				nextNode = route.next(); // picking next
			}
			while(route == null);
			
			nodeOffset = 0;
			velocity.set( nextNode.getPoint() ).sub( body.getAnchor() ).nor().mul( speed );			
			angle = velocity.angle();
		}
		
		float travelDistance = speed * delta + // the real travel distance 
				nodeOffset;
		
		NavEdge edge = mesh.getEdge( currNode, nextNode );
		if(edge == null)
		{
			nextNode = null;
			return;
		}
		
		while(travelDistance > 0)
		{
			travelDistance -= edge.getLength();
			if(travelDistance < 0)
			{
				velocity.set( nextNode.getPoint() ).sub( currNode.getPoint() ).nor().mul( speed );			
				angle = velocity.angle();
				break;
			}
			
			currNode = nextNode;
			if(!route.hasNext())
			{
				nextNode = null;
				break;
			}
			
			nextNode = route.next();
		}
		
		nodeOffset = edge.getLength()+travelDistance;
		body.getAnchor().set( edge.getDirection() ).mul( nodeOffset ).add( currNode.getPoint() );
		
	}

	public void draw(float delta, SpriteBatch batch)
	{
		Vector2 position = body.getAnchor();
		TextureRegion region = faction.getAntAnimation().getKeyFrame( stateTime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle);
		stateTime += delta;

		// TODO: remove debug rendering
		if(Debug.debug.drawNavMesh)
		{
			if(stateTime - screamTime < 1)
				font.draw( batch, "Yarr!", position.x, position.y );
			if(targetNode != null)
				font.draw( batch, String.valueOf( targetNode.idx ), position.x+2, position.y-2 );
		}
	}
	
	public void setTargetNode(NavNode targetNode)
	{
		this.targetNode = targetNode;
		route = mesh.getShortestRoute( currNode, targetNode);
	}

	@Override
	public AABB getArea() { return body; }

	@Override
	public int getId() { return id; }

	/**
	 * @return
	 */
	public boolean isAlive()
	{
		return isAlive;
	}

	/**
	 * @param damage
	 */
	public void hit(Damage damage)
	{
		isAlive = false;
	}

	/**
	 * @return
	 */
	public Effect getDeathEffect()
	{
		return Effect.getEffect( HIT_EFFECT_ATLAS_FILE_02, HIT_EFFECT_ATLAS_ID_02, 
				10, body.getAnchor(), RandomUtil.N( 360 ), 1 );
	}

	public Faction getFaction() { return faction; }

	/**
	 * @return
	 */
	public Damage getDamage() {	return damage; }
}
