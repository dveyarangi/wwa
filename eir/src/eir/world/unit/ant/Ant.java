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
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.Route;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Faction;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public abstract class Ant implements Poolable, ISpatialObject
{


	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas", 
			"explosion02");

	
	public static void free(Ant ant)
	{
		ant.body.free( );
		AntFactory.free( ant );
	}
	
	private Faction faction;

	private int id;
	
	AABB body;
	
	NavMesh mesh;
	Route route;
	
	protected float stateTime;
	
	protected float size = 5;
	Vector2 velocity = new Vector2();
	float angle;
	
	float speed = 10f;
	
	protected boolean isAlive = true;
	
	protected Damage damage = new Damage();
	
	Task task;

	Ant()
	{
		stateTime = RandomUtil.R( 10 );
	}
	
	
	abstract int getType();
	
	void init(Faction faction)
	{
		reset();
		
		this.faction = faction;
		
		NavNode spawnNode = faction.getSpawnNode();
				
		this.id = faction.getLevel().createObjectId();

		this.body = AABB.createSquare( spawnNode.getPoint().x, spawnNode.getPoint().y, this.size/2 );
		
		this.mesh = faction.getLevel().getNavMesh();

	}

	@Override
	public void reset()
	{
		stateTime = 0;
		route = null;
		velocity.set( 0,0 );
		stateTime = RandomUtil.R( 10 );
		isAlive = true;
		
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
		
		//TODO simple solution for now, if task is finished return it and ask for another
		if(task.isFinished())
		{
			faction.getScheduler().taskComplete(task);
			task = faction.getScheduler().gettaTask(this);
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
		if(task != null)
		{
			task.cancel();
			task = null;
		}
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
