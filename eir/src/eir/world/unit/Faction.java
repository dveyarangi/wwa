/**
 * 
 */
package eir.world.unit;


import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;

import yarangi.numbers.RandomUtil;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.NavNode;

/**
 * Player faction
 * 
 */
public class Faction
{
	
	public static final int MAX_ANTS = 300;
	
	public static final float SPAWN_INTERVAL = 0.5f;
	
	public int ownerId;
	
	public Level level;
	
	public Asteroid homeAsteroid;
	
	public List <Ant> ants;
	
	public float timeToSpawn = 0;

	public Animation antAnimation;
	

	public Faction(int ownerId, Level level, Asteroid homeAsteroid, String antAnimationId)
	{
		this.ownerId = ownerId;
		this.level = level;
		this.homeAsteroid = homeAsteroid;
		this.ants = new LinkedList <Ant> ();
		
		this.antAnimation = GameFactory.loadAnimation( antAnimationId, "blob" );
	}
	
	public int getOwnerId()	{ return ownerId; }

	
	public void update( float delta )
	{
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{
			
			level.addAnt( this );
			
			timeToSpawn = SPAWN_INTERVAL;
		}
	}

	/**
	 * @return
	 */
	public Level getLevel()	{ return level; }

	/**
	 * @return
	 */
	public Animation getAntAnimation()
	{
		return antAnimation;
	}

	/**
	 * @return
	 */
	public NavNode getSpawnNode()
	{
		return homeAsteroid.getModel().getNavNode( 
				RandomUtil.N( homeAsteroid.getModel().getNavNodesCount() ) );
	}

}
