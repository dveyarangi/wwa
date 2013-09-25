package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.NavEdge;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import eir.world.environment.FloydWarshalRoute;

/**
 * @author Ni
 *
 */
public class CopyOfAnt implements Poolable
{
	private static Pool<CopyOfAnt> pool = new Pool<CopyOfAnt> () {

		@Override
		protected CopyOfAnt newObject()
		{
			return new CopyOfAnt();
		}
		
	};
	
	public static CopyOfAnt getAnt(Level level, NavNode node)
	{
		if(font == null)
		{
			 font = GameFactory.loadFont("skins//fonts//default", 0.05f);
		}
		CopyOfAnt ant = pool.obtain();
		if(ant.position == null)
			ant.position = new Vector2();
		
		ant.mesh = level.getNavMesh();
		
		ant.currNode = node;
		ant.position.set( node.getPoint().x, node.getPoint().y );
		
		if(ant.animation == null)
			ant.animation = GameFactory.loadAnimation( 
					RandomUtil.oneOf(2) ?
							"anima//ant//blob_black.atlas" : 
							"anima//ant//blob_black.atlas", 
							"blob" );

		return ant;
	}
	
	public static void freeAnt(CopyOfAnt ant)
	{
		pool.free( ant );
	}
	
	private static BitmapFont font;

	
	private Vector2 position;
	
	private NavMesh mesh;
	private NavNode currNode, nextNode, targetNode;
	private FloydWarshalRoute route;
	// offset from current node on the nav edge
	private float nodeOffset;
	
	private Animation animation;
	private float stateTime;
	
	private float size = 5;
	private Vector2 velocity = new Vector2();
	private float angle;
	
	private float screamTime;
	
	private float speed = 10f;
	
	private CopyOfAnt()
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
	
}
