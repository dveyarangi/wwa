/**
 * 
 */
package eir.world.unit;


import java.util.HashSet;
import java.util.Set;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.nav.NavNode;
import eir.world.unit.ai.RandomTravelingOrder;
import eir.world.unit.ai.Scheduler;
import eir.world.unit.ant.Ant;

/**
 * Player faction
 * 
 */
public class Faction
{
	
	/////////////////////////////////////////////////////
	// loaded by LevelLoader
	//
	
	/**
	 * unit owner for hits resolving
	 */
	public int ownerId;
	
	/**
	 * unit type
	 */
	public int unitType;
	
	public Asteroid homeAsteroid;
	
	public Animation antAnimation;
	
	/**
	 * Max units to spawn
	 */
	public int maxAnts = 300;
	
	public float spawnInterval = 0.5f;
	
	/////////////////////////////////////////////////////
	//
	
	public Level level;
	
	public Set <Ant> ants;
	
	public float timeToSpawn = 0;
	
	private Scheduler scheduler;
	

	public Faction()
	{
	}
	
	public void init(Level level)
	{
		this.level = level;
		this.ants = new HashSet <Ant> ();
		this.scheduler = new Scheduler( level );
		
		scheduler.addOrder(new RandomTravelingOrder( level.getNavMesh(), 0 ));
	}
	
	public int getOwnerId()	{ return ownerId; }

	
	public void update( float delta )
	{
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{
			
			if(ants.size() < maxAnts)
				ants.add( level.addAnt( this ) );
			
			timeToSpawn = spawnInterval;
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
