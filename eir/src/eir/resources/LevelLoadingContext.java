package eir.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.world.Asteroid;
import eir.world.environment.Environment;
import eir.world.environment.nav.FloydWarshal;
import eir.world.environment.nav.NavMesh;
import eir.world.unit.Faction;
import eir.world.unit.UnitsFactory;

public class LevelLoadingContext
{
	public NavMesh navMesh = new FloydWarshal();

	public Map <String, Asteroid> asteroids =
			new HashMap <String, Asteroid> ();

	public Map <String, Animation> animations =
			new HashMap <String, Animation> ();

	public Map <Integer, Faction> factions = new HashMap <Integer, Faction> ();

	public Environment environment;

	public UnitsFactory unitsFactory;

	public LevelLoadingContext()
	{
		environment = new Environment();
		unitsFactory = new UnitsFactory( environment );
	}
	/**
	 * @param asteroidName
	 * @return
	 */
	public Asteroid getAsteroid(final String name)
	{
		if(!asteroids.containsKey( name ))
			throw new IllegalArgumentException("Asteroid " + name + " not defined.");

		return asteroids.get( name );
	}

	/**
	 * @param asteroid
	 */
	public void addAsteroid(final Asteroid asteroid)
	{
		asteroids.put( asteroid.getName(), asteroid );
	}

	public UnitsFactory getUnitsFactory()
	{
		return unitsFactory;
	}

}
