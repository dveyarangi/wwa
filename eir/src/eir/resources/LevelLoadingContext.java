package eir.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;

import eir.world.Asteroid;
import eir.world.environment.nav.FloydWarshal;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.unit.Faction;

public class LevelLoadingContext
{
	public NavMesh navMesh = new FloydWarshal();
	
	public Map <String, Asteroid> asteroids = 
			new HashMap <String, Asteroid> ();
	
	public Map <String, Animation> animations = 
			new HashMap <String, Animation> ();

	public Map <Integer, Faction> factions = new HashMap <Integer, Faction> ();

	/**
	 * @param asteroidName
	 * @return
	 */
	public Asteroid getAsteroid(String name)
	{
		if(!asteroids.containsKey( name ))
			throw new IllegalArgumentException("Asteroid " + name + " not defined.");

		return asteroids.get( name );
	}

	/**
	 * @param asteroid
	 */
	public void addAsteroid(Asteroid asteroid)
	{
		asteroids.put( asteroid.getName(), asteroid );
	}
	
}
