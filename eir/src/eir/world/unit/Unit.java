/**
 *
 */
package eir.world.unit;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.rendering.IRenderer;
import eir.rendering.LevelRenderer;
import eir.resources.GameFactory;
import eir.resources.levels.IUnitDef;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.environment.Environment;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.weapon.Weapon;
import gnu.trove.list.array.TIntArrayList;

/**
 * A generic game unit.
 *
 * TODO: becomes bulky, consider separating rendering and overlays
 *
 * @author dveyarangi
 */
public abstract class Unit implements ISpatialObject, IUnit
{
	///////////////////////////////////////////////
	// Unit initial and static definitions


	/**
	 * Some generic unit definitions;
	 */
	protected IUnitDef def;

	/**
	 * Unit faction
	 */
	protected Faction faction;

	/**
	 * Unit's anchor node, may be null for freely moving units.
	 */
	public Anchor anchor;


	///////////////////////////////////////////

	/**
	 * Some id
	 */
	private int id;

	private int hashcode;

	/**
	 * Unit location and collision box
	 */
	private AABB body;

	/**
	 * Unit orientation
	 * TODO: encapsulate in body?
	 */
	public float angle;

	/**
	 * Specifies if this unit is alive
	 * This flags to unit processing loop to withdraw unit from update cycle
	 */
	private boolean isAlive;

	/**
	 * Unit hull contains hit points and damage resistances
	 * TODO: create from defs
	 */
	protected Hull hull;

	/**
	 * Time since unit creation
	 */
	protected float lifetime;

	/**
	 * Max time length
	 * TODO: mainly used for bullets now; consider replacing with travel distance
	 */
	public float lifelen;

	/**
	 * Unit attack or movement target, if it has one
	 */
	public ISpatialObject target;

	/**
	 * TODO: this is for birdies only, consider removal
	 */
	public float timeToPulse = 0;

	/**
	 * Unit physical velocity, actual for freely moving units
	 */
	private final Vector2 velocity;

	/**
	 * List of overlays that are toggled on for this unit.
	 */
	private TIntArrayList overlays;

	/**
	 * List of overlays that activate on mouse hovering
	 */
	private TIntArrayList hoverOverlays;

	/**
	 * States whether this unit is currenly has cursor over it
	 */
	private boolean isHovered = false;

	private Animation deathAnimation;

	private Sprite unitSprite;

	public Unit( )
	{

		velocity = new Vector2();

		this.body = AABB.createPoint( 0, 0 );

		this.overlays = new TIntArrayList ();
		this.hoverOverlays = new TIntArrayList ();

		this.hashcode = hashCode();
	}

	/**
	 * Resets unit according to {@link #def} properties.
	 * Called when unit is created or retrieved from pool.
	 *
	 * Override this method to reset subclass's custom properties
	 * @param level
	 */
	protected void reset( final GameFactory factory, final Level level)
	{
		this.isAlive = true;
		this.id = Environment.createObjectId();

		this.lifetime = 0;
		this.lifelen = Float.NaN;

		this.timeToPulse = 0;

		this.target = null;

		if(def.getUnitSprite() == null)
		{
			Debug.log( "Unit " + this + " has no sprite!" );
		} else
		{
			this.unitSprite = factory.createSprite( def.getUnitSprite() );
		}

		this.deathAnimation = def.getDeathAnimation() == null ? null :
			factory.getAnimation( def.getDeathAnimation() );

		this.velocity.set( 0,0 );

		registerOverlays();
	}

	protected void registerOverlays()
	{
		this.overlays.clear();
		this.hoverOverlays.clear();

		addHoverOverlay( LevelRenderer.INTEGRITY_OID);
//		toggleOverlay( LevelRenderer.INTEGRITY_OID);
	}

	/**
	 * Initializes this unit using the provided defs and anchor and assigning it to the faction.
	 * @param factory
	 * @param level
	 * @param def
	 * @param anchor
	 * @param faction
	 */
	public void init( final GameFactory factory, final Level level, final IUnitDef def, final Anchor anchor )
	{
		this.anchor = anchor;

		init(factory, level, def, anchor.getPoint().x, anchor.getPoint().y, anchor.getAngle());
	}

	/**
	 * Initializes this unit using the provided defs and position and assigning it to the faction.
	 * @param factory
	 * @param def
	 * @param anchor
	 * @param faction
	 */
	public void init(final GameFactory factory, final Level level, final IUnitDef def, final float x, final float y, final float angle)
	{
		this.def = def;

		this.faction = level.getFaction( def.getFactionId() );

		this.body.update( x, y, getSize()/2, getSize()/2);

		this.angle = angle;

		reset( factory, level );
	}

	public void dispose()
	{
	}

	public AABB getBody() { return body; }

	public Faction getFaction() { return faction; }

	public String getType() { return def.getType(); }


	@Override
	public AABB getArea() { return body; }

	@Override
	public float getAngle() { return angle; }

	@Override
	public int getId() { return id; }

	public String getName()  { return this.getClass().getSimpleName(); }

	@Override
	public String toString() { 	return getName() + " (" + getId() + ")"; }

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

	public void draw( final IRenderer renderer )
	{
		if(unitSprite == null)
			return;
		final SpriteBatch batch = renderer.getSpriteBatch();
		Vector2 position = getBody().getAnchor();
		Sprite sprite = getUnitSprite();
		batch.draw( sprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionWidth(), angle);
	}


	public void draw( final ShapeRenderer shape )
	{
		shape.begin(ShapeType.FilledCircle);
		Color color = faction.getColor();
		shape.setColor(color.r,color.g,color.b,0.5f);
		shape.filledCircle(getBody().getAnchor().x, getBody().getAnchor().y, getSize() / 2);
		shape.end();

		if( this.target != null)
		{
			shape.begin(ShapeType.Line);
			shape.line( this.getArea().getAnchor().x, this.getArea().getAnchor().y,
					this.target.getArea().getAnchor().x, this.target.getArea().getAnchor().y );
			shape.end();
			shape.begin(ShapeType.Circle);
			shape.circle( this.target.getArea().cx(),
					      this.target.getArea().cy(),
					      this.target.getArea().rx());
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
		if(hull == null) // TODO: stub, in future hulless units should not respond to damage
		{
			setDead();
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
		if(deathAnimation != null)
			return Effect.getEffect( deathAnimation, 10, getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 1 );
		return null;
	}

	protected Sprite getUnitSprite() { return unitSprite; }

	public float getSize() { return this.def.getSize(); }



	public Anchor getAnchor() { return anchor; }

	public boolean dealsFriendlyDamage() { return false; }

	public ISpatialObject getTarget() { return target; }

	public abstract float getMaxSpeed();
	public float getLifetime() { return lifetime; }
	public Vector2 getVelocity() { return velocity; }

	protected void toggleOverlay(final int oid)
	{
		if(overlays.contains( oid ))
		{
			overlays.remove( oid );
		} else
		{
			overlays.add( oid );
		}
	}

	protected void addHoverOverlay( final int oid ) { hoverOverlays.add( oid ); }
	@Override
	public Hull getHull() { return hull; }

	public TIntArrayList getActiveOverlays() { return overlays; }
	public TIntArrayList getHoverOverlays() { return hoverOverlays; }

	public float cx() { return body.getAnchor().x; }
	public float cy() { return body.getAnchor().y; }

	public Weapon getWeapon() { return null; }

	public void setIsHovered(final boolean isHovered)	{ this.isHovered = isHovered; }
	public boolean isHovered() { return isHovered; }

	@Override public int hashCode() {	return hashcode; }

	public IUnitDef getDef() { return def; }
}
