/**
 * 
 */
package eir.world.unit;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import eir.world.Level;
import eir.world.controllers.IController;
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
	
	public Color color;
	
	/////////////////////////////////////////////////////
	//
	
	public Level level;
	
	public Set <Unit> units;
	public Multimap <String, Unit> unitsByTypes;
	
	private IController controller;
	
	private Scheduler scheduler;
	

	public Faction()
	{
		this.units = new HashSet <Unit> ();
		this.unitsByTypes = HashMultimap.create ();
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
		unitsByTypes.put( unit.getType(), unit );
	}
	
	public void removeUnit( Unit unit )
	{
		units.remove( unit );
		unitsByTypes.remove( unit.getType(), unit );
	}
	
	public Collection <Unit> getUnitsByType( String unitType )
	{
		return unitsByTypes.get( unitType );
	}
	
	public void update( float delta )
	{
		controller.update( delta );
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

	public Set <Unit> getUnits() {
		return units;
	}

}
