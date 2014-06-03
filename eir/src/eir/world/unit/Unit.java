/**
 *
 */
package eir.world.unit;

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

/**
 * @author dveyarangi
 *
 */
public abstract class Unit implements ISpatialObject, IUnit
{
	///////////////////////////////////////////////
	// loaded by level loader


	protected String type;

	protected Faction faction;

	public SurfaceNavNode anchor;

	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas",
			"explosion02");


	///////////////////////////////////////////

	private int id;

	private AABB body;

	public float angle;

	private boolean isAlive;

	protected Hull hull;

	/**
	 * Time since unit creation
	 */
	public float lifetime;

	/**
	 * Max time length
	 */
	public float lifelen;

	public ISpatialObject target;

	public float timeToPulse = 0;

	public final Vector2 velocity = new Vector2();

	public Unit()
	{

		velocity.set(0,0);

		timeToPulse = 0;

		this.body = AABB.createPoint( 0, 0 );
	}

	/**
	 * Override this method to reset subclass's custom properties
	 */
	protected void init()
	{

		this.isAlive = true;
		this.id = Environment.createObjectId();

		this.type = type.intern();

		this.lifetime = 0;
		this.lifelen = Float.NaN;

		this.target = null;
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

	public void dispose()
	{
	}



	public void postinit( final Level level )
	{

	}

	public AABB getBody() { return body; }

	public Faction getFaction() { return faction; }

	public String getType() { return type; }


	@Override
	public AABB getArea() { return body; }

	public float getAngle() { return angle; }

	@Override
	public int getId() { return id; }

	public String getName()  { return this.getClass().getSimpleName(); }

	@Override
	public String toString() {
		return getName() + " (" + getId() + ")";
	}

	public void update(final float delta)
	{

		// this is sad:
		lifetime += delta;

		if( lifetime > lifelen )
		{
			setDead();
			return;
		}

	}

	public abstract void draw( final SpriteBatch batch );


	public void draw( final ShapeRenderer shape )
	{
		shape.begin(ShapeType.FilledCircle);
		shape.setColor(faction.color.r,faction.color.g,faction.color.b,0.5f);
		shape.filledCircle(getBody().getAnchor().x, getBody().getAnchor().y, getSize() / 2);
		shape.end();

		if( this.target != null)
		{
			shape.begin(ShapeType.Line);
			shape.line( this.getArea().getAnchor().x, this.getArea().getAnchor().y,
					this.target.getArea().getAnchor().x, this.target.getArea().getAnchor().y );
			shape.end();
			shape.begin(ShapeType.Circle);
			shape.circle( this.target.getArea().getAnchor().x,
					      this.target.getArea().getAnchor().y,
					      this.target.getArea().getRX());
			shape.end();

		}
	}


	@Override
	public boolean isAlive() { return isAlive; }

	public void setDead() { this.isAlive = false; }


	/**
	 * @param damageReduction
	 * @param damage
	 */
	public float hit(final Damage source, final IDamager damager, final float damageCoef)
	{
		float damage = damage( damager.getDamage(), damageCoef );
		faction.getController().yellUnitHit( this, damager );

		return damage;
	}

	protected float damage( final Damage source, final float damageCoef )
	{
		if(hull == null) // TODO: stub
		{
			Debug.log( "Unit has no hull: " + this);
//			setDead();
			return 0;
		}

		float damage = hull.hit( source, damageCoef );

		if(hull.isBreached())
		{
			setDead(); // TODO: consider decision by unit controller
		}

		return damage;
	}
	/**
	 * @return
	 */
	public Effect getDeathEffect()
	{
		return null;//Effect.getEffect( hitAnimationId, 25, body.getAnchor(), RandomUtil.N( 360 ), 1 );
	}


	public abstract float getSize();



	public SurfaceNavNode getAnchorNode()
	{
		return anchor;
	}

	public boolean dealsFriendlyDamage() { return false; }

	public ISpatialObject getTarget() { return target; }

	public abstract float getMaxSpeed();
}
