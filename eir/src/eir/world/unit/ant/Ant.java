/**
 * 
 */
package eir.world.unit.ant;

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
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import eir.world.environment.Route;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Faction;
import eir.world.unit.ai.Task;

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

	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas", 
			"explosion02");

	
	public static void free(Ant ant)
	{
		ant.body.free( );
		pool.free( ant );
	}
	
	private Faction faction;
	
	private static BitmapFont font;

	private int id;
	
	AABB body;
	
	NavMesh mesh;
	NavNode currNode, nextNode, targetNode;
	Route route;
	// offset from current node on the nav edge
	float nodeOffset;
	
	private float stateTime;
	
	private float size = 5;
	Vector2 velocity = new Vector2();
	float angle;
	
	private float screamTime;
	
	float speed = 10f;
	
	private boolean isAlive = true;
	
	private Damage damage = new Damage();
	
	Task task;

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
		
		nodeOffset = 0;
		
		task = null;

	}
	
	public void update(float delta)
	{
		if(task == null || task.isFinished())
		{
			// requesting a new task:
			task = faction.getScheduler().gettaTask( this );
			if(task == null)
				return;
		}
		
		// performing task:
		task.getBehavior().update( delta, task, this );
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
		task.cancel();
		task = null;
	}

	/**
	 * @return
	 */
	public Effect getDeathEffect()
	{
		return Effect.getEffect( hitAnimationId, 25, body.getAnchor(), RandomUtil.N( 360 ), 1 );
	}

	public Faction getFaction() { return faction; }

	/**
	 * @return
	 */
	public Damage getDamage() {	return damage; }
}
