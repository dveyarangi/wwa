/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
import eir.world.environment.NavEdge;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import eir.world.environment.Route;

/**
 * @author dveyarangi
 *
 */
public class Ant implements Poolable
{
	private static Pool<Ant> pool = new Pool<Ant> () {

		@Override
		protected Ant newObject()
		{
			return new Ant();
		}
		
	};
	
	public static Ant getAnt(GameFactory factory, NavNode node)
	{
		if(font == null)
		{
			 font = factory.loadFont("skins//fonts//default", 0.05f);
		}
		Ant ant = pool.obtain();
		if(ant.position == null)
			ant.position = new Vector2();
		
		ant.mesh = factory.getNavMesh();
		
		ant.currNode = node;
		ant.position.set( node.getPoint().x, node.getPoint().y );
		
		if(ant.animation == null)
			ant.animation = factory.loadAnimation( 
					RandomUtil.oneOf(2) ?
							"anima//ant//blob_black.atlas" : 
							"anima//ant//blob_black.atlas", 
							"blob" );

		return ant;
	}
	
	public static void freeAnt(Ant ant)
	{
		pool.free( ant );
	}
	
	private static BitmapFont font;

	
	private Vector2 position;
	
	private NavMesh mesh;
	private NavNode currNode, nextNode, targetNode;
	private Route route;
	// offset from current node on the nav edge
	private float nodeOffset;
	
	private Animation animation;
	private float stateTime;
	
	private float size = 5;
	private Vector2 velocity = new Vector2();
	private float angle;
	
	private float screamTime;
	/**
	 * TODO: raise CONTACT_DISTANCE if u raise the speed,
	 * otherwise they will go astray. 
	 */
	private float speed = 10f;
	
	private static float CONTACT_DISTANCE = 2;
	
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
	}
	
	public void update(float delta)
	{

		if(nextNode == null)
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
			nextNode = route.next(); // picking next
			
			nodeOffset = 0;
			velocity.set( nextNode.getPoint() ).sub( position ).nor().mul( speed );			
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
		position.set( edge.getDirection() ).mul( nodeOffset ).add( currNode.getPoint() );
	}
/*	public void update(float delta)
	{
		if(delta > 1)
			return;
		if(nextNode == null)
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
			
			
			// updating course to the next node
			nextNode = route.next();
			
			// update course:
			velocity.set( nextNode.getPoint() ).sub( position ).nor().mul( speed );
			angle = velocity.angle();
		}
		
		// moving ant toward the next nav node 
		// TODO: this is bad method, because if ant speed is too big it may skip 
		// the next node and never return to the navmesh
		position.add( velocity.tmp().mul( delta ) );
		
		if(position.tmp().sub( nextNode.getPoint() ).len2() < CONTACT_DISTANCE)
		{
			currNode = nextNode;
			nextNode = null;
		}

	}*/

	public void draw(float delta, SpriteBatch batch)
	{
		TextureRegion region = animation.getKeyFrame( stateTime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle);
		stateTime += delta;
		
		if(stateTime - screamTime < 1)
			font.draw( batch, "Yarr!", position.x, position.y );
		if(targetNode != null)
		font.draw( batch, String.valueOf( targetNode.index ), position.x+2, position.y-2 );
	}
	
	public void setTargetNode(NavNode targetNode)
	{
		this.targetNode = targetNode;
		route = mesh.getShortestRoute( currNode, targetNode);
	}
	
}
