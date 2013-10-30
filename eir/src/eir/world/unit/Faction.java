/**
 * 
 */
package eir.world.unit;


import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.world.Asteroid;
import eir.world.Level;
import eir.world.controllers.IController;
import eir.world.unit.ai.RandomTravelingOrder;
import eir.world.unit.ai.Scheduler;

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
	
	public Animation antAnimation;
	
	/////////////////////////////////////////////////////
	//
	
	public Level level;
	
	public Set <Unit> units;
	
	private IController controller;
	
	private Scheduler scheduler;
	

	public Faction()
	{
		this.units = new HashSet <Unit> ();
		scheduler = new Scheduler();
	}
	
	public void init(Level level)
	{
		this.level = level;
		this.controller.init( this );
	}
	
	public int getOwnerId()	{ return ownerId; }

	public void addUnit( Unit unit )
	{
		units.add( unit );
	}
	
	public void removeUnit( Unit unit )
	{
		units.remove( unit );

	}
	
	public void update( float delta )
	{
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


	public Scheduler getScheduler() { return scheduler; }

	public IController getController() {
		return controller;
	}

}
