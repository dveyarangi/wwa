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
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.Route;
import eir.world.unit.Damage;
import eir.world.unit.Faction;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Ant extends Unit
{


	private static int hitAnimationId = GameFactory.registerAnimation("anima//effects//explosion//explosion02.atlas", 
			"explosion02");

	NavMesh mesh;
	Route route;
	
	protected float stateTime;
	
	protected float size = 5;
	Vector2 velocity = new Vector2();
	
	float speed = 10f;
	
	private float screamTime;
	float nodeOffset;
	
	NavNode nextNode, targetNode;

	public Ant()
	{
	}
	
	public void init()
	{
		super.init();
		
		this.mesh = faction.getLevel().getGroundNavMesh();
		
		route = null;
		velocity.set( 0,0 );
		stateTime = RandomUtil.R( 10 );

	}

	public void update( float delta )
	{
		super.update( delta );
		
		stateTime += delta;
	}

	public void draw(SpriteBatch batch)
	{
		Vector2 position = getBody().getAnchor();
		TextureRegion region = faction.getAntAnimation().getKeyFrame( stateTime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle);

		// TODO: remove debug rendering
		if(Debug.debug.drawNavMesh)
		{
			if(stateTime - screamTime < 1)
				Debug.FONT.draw( batch, "Yarr!", position.x, position.y );
			if(targetNode != null)
				Debug.FONT.draw( batch, String.valueOf( targetNode.idx ), position.x+2, position.y-2 );
		}
	}

	/**
	 * @return
	 */
	public Effect getDeathEffect()
	{
		return Effect.getEffect( hitAnimationId, 25, getBody().getAnchor(), RandomUtil.N( 360 ), 1 );
	}

	public Faction getFaction() { return faction; }

	/**
	 * @return
	 */
	public Damage getDamage() {	return damage; }

	@Override public float getSize() { return size; }

}
