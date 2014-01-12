/**
 * 
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

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

	public NavNode anchor;

	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas", 
			"explosion02");
	

	///////////////////////////////////////////
	
	private AABB body;
	
	private int id;
	
	public float angle;
	
	private Task task;
	
	private boolean isAlive = true;
	
	protected Damage damage = new Damage();

	
	public float lifetime;
	

	public Unit()
	{
		body = AABB.createPoint( 0, 0 );
	}
	
	
	public void init(String type, NavNode anchor, Faction faction)
	{
		this.type = type;
		this.anchor = anchor;
		this.faction = faction;
		
		body.update( anchor.getPoint().x, anchor.getPoint().y, getSize()/2, getSize()/2 );
	
		init();
	}
	public void init(String type, float x, float y, float angle, Faction faction)
	{
		this.type = type;
		this.faction = faction;
		
		body.update( x, y, getSize()/2, getSize()/2 );
		
		this.angle = angle;
		
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
		
		this.lifetime = 0;
	}
	
	public void update(float delta)
	{
		lifetime += delta;
		
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
		task.getBehavior( this ).update( delta, task, this );

	}

	public abstract void draw( SpriteBatch batch );
	
	public void draw( ShapeRenderer shape )
	{
		shape.begin(ShapeType.FilledCircle);
		shape.setColor(faction.color.r,faction.color.g,faction.color.b,0.5f);
		shape.filledCircle(getBody().getAnchor().x, getBody().getAnchor().y, getSize() / 2);
		shape.end();
	}

	
	public boolean isAlive() { return isAlive; }

	public void setDead() { this.isAlive = false; }
	

	/**
	 * @param damage
	 */
	public void hit(Unit source)
	{
		faction.getController().yellUnitHit( this, source );
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


	public void setShootingTarget(Vector2 targetPos) {}
	public void walkDown(boolean b) {}
	public void walkCCW(boolean b) {}
	public void walkCW(boolean b) {}
	public void walkUp(boolean b) {}

}
