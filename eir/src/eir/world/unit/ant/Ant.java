/**
 *
 */
package eir.world.unit.ant;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.IRenderer;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.Route;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.Damage;
import eir.world.unit.Faction;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.TaskedUnit;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Ant extends TaskedUnit implements IDamager
{


	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas",
			"explosion02");

	NavMesh <SurfaceNavNode> mesh;
	Route  <SurfaceNavNode> route;

	protected float stateTime;

	protected float size = 5;
	Vector2 velocity = new Vector2();

	float speed = 10f;

	private float screamTime;
	float nodeOffset;

	SurfaceNavNode nextNode, targetNode;

	Damage damage = new Damage(1, 1, 1, 1);

	public Ant()
	{
		super();
	}

	@Override
	protected void init()
	{
		super.init();

		this.mesh = faction.getEnvironment().getGroundMesh();

		route = null;
		velocity.set( 0,0 );
		stateTime = RandomUtil.R( 10 );

		this.hull = new Hull(1f, 0f, new float [] {0f,0f,0f,0f});

	}

	@Override
	public void update( final float delta )
	{
		super.update( delta );

		stateTime += delta;
	}

	@Override
	public void draw( final IRenderer renderer )
	{
		final SpriteBatch batch = renderer.getSpriteBatch();
		Vector2 position = getBody().getAnchor();
		TextureRegion region = faction.getAntAnimation().getKeyFrame( stateTime, true );
		batch.draw( region,
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2,
				region.getRegionWidth(), region.getRegionHeight(),
				size/region.getRegionWidth(),
				size/region.getRegionWidth(), getAngle());

		// TODO: remove debug rendering
		if(Debug.debug.drawNavMesh)
		{
			if(stateTime - screamTime < 1)
			{
				Debug.FONT.draw( batch, "Yarr!", position.x, position.y );
			}
			if(targetNode != null)
			{
				Debug.FONT.draw( batch, String.valueOf( targetNode.getIndex() ), position.x+2, position.y-2 );
			}
		}
	}

	/**
	 * @return
	 */
	@Override
	public Effect getDeathEffect()
	{
		return Effect.getEffect( hitAnimationId, 25, getBody().getAnchor(), Vector2.Zero, RandomUtil.N( 360 ), 1 );
	}

	@Override
	public Faction getFaction() { return faction; }

	/**
	 * @return
	 */
	@Override
	public Damage getDamage() {	return damage; }

	@Override public float getSize() { return size; }
	@Override
	public Unit getSource()
	{
		return this; // TODO: maybe generalize to drone and make source the spawner?
	}
	@Override
	public void dispose()
	{
		super.dispose();

		if( route != null)
		{
			route.recycle();
		}
	}

	@Override
	public float getMaxSpeed()
	{
		return speed;
	}

}
