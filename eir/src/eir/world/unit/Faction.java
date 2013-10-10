/**
 * 
 */
package eir.world.unit;


import java.util.HashSet;
import java.util.Set;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.NavNode;
import eir.world.unit.ai.RandomTravelingOrder;
import eir.world.unit.ai.Scheduler;
import eir.world.unit.ant.Ant;

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
	
	public Set <Ant> ants;
	
	public float timeToSpawn = 0;

	public final Animation antAnimation;
	
	private Scheduler scheduler;
	

	public Faction(int ownerId, Level level, Asteroid homeAsteroid, String antAnimationFile)
	{
		this.ownerId = ownerId;
		this.level = level;
		this.homeAsteroid = homeAsteroid;
		this.ants = new HashSet <Ant> ();
		this.scheduler = new Scheduler( level );
		
		scheduler.addOrder(new RandomTravelingOrder(0));
		
		int antAnimationId = GameFactory.registerAnimation( antAnimationFile, "blob" );
		this.antAnimation = GameFactory.getAnimation( antAnimationId );
	}
	
	public int getOwnerId()	{ return ownerId; }

	
	public void update( float delta )
	{
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{
			
			if(ants.size() < MAX_ANTS)
				ants.add( level.addAnt( this ) );
			
			timeToSpawn = SPAWN_INTERVAL;
		}
	}
	
	public void removeAnt( Ant ant )
	{
		ants.remove( ant );
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

	public Scheduler getScheduler() { return scheduler; }

}
