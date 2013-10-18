/**
 * 
 */
package eir.world.unit.ant;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.Route;
import eir.world.unit.Faction;

/**
 * @author dveyarangi
 *
 */
public class StickyAnt extends Ant
{
	Route route;
	// offset from current node on the nav edge
	float nodeOffset;
	
	private float screamTime;
	
	NavMesh mesh;
	
	NavNode currNode, nextNode, targetNode;


	@Override
	int getType()
	{
		 return AntFactory.ANT_STICKY;
	}

	@Override
	void init(Faction faction)
	{
		super.init( faction );
		
		screamTime = 0;
		nextNode = null;
		
		currNode = faction.getSpawnNode();
		
		mesh = faction.level.getNavMesh();
		
		nodeOffset = 0;
	}
	
	public void draw(float delta, SpriteBatch batch)
	{
		super.draw( delta, batch );

		Vector2 position = body.getAnchor();
		// TODO: remove debug rendering
		if(Debug.debug.drawNavMesh)
		{
			if(stateTime - screamTime < 1)
				Debug.FONT.draw( batch, "Yarr!", position.x, position.y );
			if(targetNode != null)
				Debug.FONT.draw( batch, String.valueOf( targetNode.idx ), position.x+2, position.y-2 );
		}
	}

}
