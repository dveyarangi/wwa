/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
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
		Ant ant = pool.obtain();
		if(ant.position == null)
			ant.position = new Vector2();
		
		ant.mesh = factory.getNavMesh();
		
		ant.currNode = node;
		ant.position.set( node.getPoint().x, node.getPoint().y );
		
		if(ant.animation == null)
			ant.animation = factory.loadAnimation( "anima//ant//ant.atlas", "blob" );

		return ant;
	}
	
	public static void freeAnt(Ant ant)
	{
		pool.free( ant );
	}
	
	private Vector2 position;
	
	private NavMesh mesh;
	private NavNode currNode, nextNode;
	private Route route;
	
	private Animation animation;
	private float stateTime;
	
	private float size = 5;
	private Vector2 velocity = new Vector2();
	private float angle;
	private float speed = 4f;
	
	private static float CONTACT_DISTANCE = 2;
	
	private Ant()
	{
		stateTime = 0;

	}

	@Override
	public void reset()
	{
		route = null;
		nextNode = null;
		velocity.set( 0,0 );
		stateTime = RandomUtil.R( 10 );
	}
	
	public void update(float delta)
	{
		if(delta > 1)
			return;
		if(nextNode == null)
		{
			if(route == null || !route.hasNext())
			{
				NavNode targetNode = mesh.getNode( RandomUtil.N( mesh.getNodesNum() ) );
				route = mesh.getShortestRoute( currNode, targetNode );
			}
			
			nextNode = route.next();
			
			updateCourse();
		}
		
		position.add( velocity.tmp().mul( delta ) );
		
		if(position.tmp().sub( nextNode.getPoint() ).len2() < CONTACT_DISTANCE)
		{
			currNode = nextNode;
			nextNode = null;
		}

	}
	
	private void updateCourse()
	{
		velocity.set( nextNode.getPoint() ).sub( position ).nor().mul( speed );
		angle = velocity.angle();
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
	}
	
	public void setTargetNode(NavNode targetNode)
	{
		route = mesh.getShortestRoute( currNode, targetNode);
	}
	
}
