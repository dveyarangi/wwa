/**
 *
 */
package eir.world.unit;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import eir.resources.GameFactory;
import eir.resources.levels.FactionDef;
import eir.resources.levels.UnitDef;
import eir.world.Level;
import eir.world.controllers.ControllerFactory;
import eir.world.controllers.IController;
import eir.world.environment.Anchor;
import eir.world.environment.Environment;
import eir.world.environment.sensors.ISensingFilter;
import eir.world.environment.spatial.ISpatialObject;
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
	private FactionDef def;


	public Animation antAnimation;
	/////////////////////////////////////////////////////
	//

	public Level level;

	private GameFactory gameFactory;

	public Set <Unit> units;
	public Multimap <String, Unit> unitsByTypes;

	private IController controller;

	private Scheduler scheduler;

	private ISensingFilter enemyFilter = new ISensingFilter() {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			if( ! ( entity instanceof IUnit ) )
				return false;

			Unit unit = (Unit) entity;

			return isEnemy( unit );
		}

	};


	public Faction()
	{

		this.units = new HashSet <Unit> ();
		this.unitsByTypes = HashMultimap.create ();
	}

	public void init(final GameFactory gameFactory, final Level level, final FactionDef def)
	{
		this.gameFactory = gameFactory;
		this.def = def;
		this.level = level;

		this.controller = ControllerFactory.createController( def.getControllerType() );
		controller.init( this );
		this.controller.init( this );
		scheduler = new Scheduler( level.getUnitsFactory() );


	}

	public int getOwnerId()	{ return def.getOwnerId(); }

	public void addUnit( final Unit unit )
	{
		units.add( unit );
		unitsByTypes.put( unit.getType(), unit );
		controller.unitAdded( unit );
	}

	public void removeUnit( final Unit unit )
	{
		units.remove( unit );
		unitsByTypes.remove( unit.getType(), unit );
	}

	public Collection <Unit> getUnitsByType( final String unitType )
	{
		return unitsByTypes.get( unitType );
	}

	public void update( final float delta )
	{
		controller.update( delta );
	}


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

	public Level getLevel() { return level; }
	/**
	 * Equivalent of {@link #getLevel()#getEnvironment()}
	 * @return
	 */
	public Environment getEnvironment() { return level.getEnvironment(); }

	public Unit createUnit( final UnitDef def, final Anchor anchor )
	{
		Unit unit = level.getUnitsFactory().getUnit( gameFactory, level, def, anchor );
		getLevel().addUnit( unit );

		return unit;
	}

	public boolean isEnemy( final Unit unit )
	{
		return this != unit.getFaction();
	}

	public ISensingFilter getEnemyFilter() { return enemyFilter; }

	public Color getColor() { return def.getColor(); }


}
