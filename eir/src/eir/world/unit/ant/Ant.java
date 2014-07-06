/**
 *
 */
package eir.world.unit.ant;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.GameFactory;
import eir.world.Level;
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



	NavMesh <SurfaceNavNode> mesh;
	Route  <SurfaceNavNode> route;

	protected float stateTime;

	Vector2 velocity = new Vector2();

	float speed = 10f;

	private float screamTime;
	float nodeOffset;

	SurfaceNavNode nextNode;

	Damage damage = new Damage(1, 1, 1, 1);

	public Ant()
	{
		super();
	}

	@Override
	protected void reset( final GameFactory gameFactory, final Level level )
	{
		super.reset( gameFactory, level );

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

		this.target = getTask() == null ? null : getTask().getOrder().getTarget();

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
				getSize()/region.getRegionWidth(),
				getSize()/region.getRegionWidth(), getAngle());


	}

	/**
	 * @return
	 */

	@Override
	public Faction getFaction() { return faction; }

	/**
	 * @return
	 */
	@Override
	public Damage getDamage() {	return damage; }
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
