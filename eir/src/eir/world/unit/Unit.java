/**
 *
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.Environment;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.SurfaceNavNode;
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

	public SurfaceNavNode anchor;

	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas",
			"explosion02");


	///////////////////////////////////////////

	private AABB body;

	private int id;

	public float angle;

	private Task task;

	private boolean isAlive = true;

	protected Hull hull;


	public float lifetime;


	public Unit()
	{
		this.body = AABB.createPoint( 0, 0 );
	}

	public void init(final String type, final NavNode anchor, final Faction faction)
	{

		if(body == null)
		{
			this.body = AABB.createFromCenter( anchor.getPoint().x, anchor.getPoint().y, getSize()/2, getSize()/2 );
		} else
		{
			this.body.update( anchor.getPoint().x, anchor.getPoint().y, getSize()/2, getSize()/2 );
		}

		this.type = type;
		this.anchor = (SurfaceNavNode)anchor;
		this.faction = faction;

		body.update( anchor.getPoint().x, anchor.getPoint().y, getSize()/2, getSize()/2 );

		init();
	}
	public void init(final String type, final float x, final float y, final float angle, final Faction faction)
	{
		this.type = type;
		this.faction = faction;

		this.body.update( x, y, getSize()/2, getSize()/2);

		this.angle = angle;

		init();
	}


	protected void init()
	{

		this.isAlive = true;
		this.id = Environment.createObjectId();
		this.task = null;

		this.type = type.intern();

		this.lifetime = 0;
	}

	public void postinit( final Level level )
	{

	}

	public AABB getBody() { return body; }

	public Faction getFaction() { return faction; }

	public String getType() { return type; }


	@Override
	public AABB getArea() { return body; }

	@Override
	public int getId() { return id; }

	public String getName()  { return this.getClass().getSimpleName(); }

	@Override
	public String toString() {
		return getName() + " (" + getId() + ")";
	}

	public void update(final float delta)
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

	public abstract void draw( final SpriteBatch batch );


	public void draw( final ShapeRenderer shape )
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
	public void hit(final Damage source, final IDamager damager)
	{
		damage( damager.getDamage() );
		faction.getController().yellUnitHit( this, damager );
		if(task != null)
		{
			task.cancel();
			task = null;
		}
	}

	protected Damage damage( final Damage source )
	{
		if(hull == null) // TODO: stub
		{
			Debug.log( "Unit has no hull: " + this);
//			setDead();
			return source;
		}

		hull.hit( source );

		if(hull.isBreached())
		{
			setDead();
		}

		return source;
	}
	/**
	 * @return
	 */
	public Effect getDeathEffect()
	{
		return Effect.getEffect( hitAnimationId, 25, body.getAnchor(), RandomUtil.N( 360 ), 1 );
	}


	public abstract float getSize();


	public void setShootingTarget(final Vector2 targetPos) {}
	public void walkDown(final boolean b) {}
	public void walkCCW(final boolean b) {}
	public void walkCW(final boolean b) {}
	public void walkUp(final boolean b) {}

}
