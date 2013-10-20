/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import yarangi.numbers.RandomUtil;
import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.nav.NavNode;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public abstract class Unit implements ISpatialObject
{
	///////////////////////////////////////////////
	// loaded by level loader
	
	protected String type;
	
	protected Faction faction;


	public NavNode position;

	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas", 
			"explosion02");
	

	///////////////////////////////////////////
	
	private AABB body;
	
	private int id;
	
	public float angle;
	
	
	private Task task;
	
	private boolean isAlive = true;
	
	protected Damage damage = new Damage();


	public Unit()
	{
		body = AABB.createPoint( 0, 0 );
	}
	
	
	public void init(String type, NavNode position, Faction faction)
	{
		this.type = type;
		this.position = position;
		this.faction = faction;
		
		init();
	}
	
	public AABB getBody() { return body; }
	
	public Faction getFaction() { return faction; }
	
	public String getType() { return type; }
	

	@Override
	public AABB getArea() { return body; }

	@Override
	public int getId() { return id; }

	public void init()
	{
		this.isAlive = true;
		this.id = Level.createObjectId();
		this.task = null;
		
		this.type = type.intern();
		
		body.update( position.getPoint().x, position.getPoint().y, getSize()/2, getSize()/2 );
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

	public abstract void draw( SpriteBatch batch );

	
	public boolean isAlive() { return isAlive; }

	public void setDead() { this.isAlive = false; }
	

	/**
	 * @param damage
	 */
	public void hit(Damage damage)
	{
		setDead();
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


	/**
	 * @return
	 */
	public Damage getDamage() {	return damage; }

	public abstract float getSize();
}
