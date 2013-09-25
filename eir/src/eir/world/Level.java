package eir.world;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.world.environment.FloydWarshal;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.unit.Ant;

public class Level
{
	private String name;

	/**
	 * Level dimensions
	 */
	private int width, height;
	
	/**
	 * Level background texture
	 */
	private Texture backgroundTexture;
	
	/**
	 * Level initial settings
	 * TODO: extract to LevelInitialConfig classs
	 */
	private float initialZoom;
	private int initialNodeIdx;
	private NavNode initialNode;
	
	/**
	 * Indexer for ants and bullets.
	 */
	private SpatialHashMap<ISpatialObject> spatialIndex;
	
	private NavMesh navMesh;

	/**
	 * List of asteroids
	 */
	private List <Asteroid> asteroids;
	
	/**
	 * List of webs
	 */
	private List <Web> webs;
	
	/**
	 * List of ants
	 */
	private Set <Ant> ants;

	public Level()
	{
		navMesh = new FloydWarshal();
		ants = new HashSet <Ant> ();
	}
	
	public List <Asteroid> getAsteroids() { return asteroids;}

	public List <Web> getWebs() { return webs; }
	
	public Set <Ant> getAnts() { return ants; }
	
	/**
	 * @param factory  
	 */
	public void init()
	{
		
		spatialIndex = new SpatialHashMap<ISpatialObject>( 
				name+ "-spatial", 
				16f, // size of bucket
				width, height );
		
		for(Asteroid asteroid : asteroids)
		{
			asteroid.init( this );
		}
		
		for( Web web : webs )
		{
			web.init( this );
		}

		// nav mesh initiated after this point
		////////////////////////////////////////////////////
		
		Debug.startTiming("navmesh");
		navMesh.init();
		Debug.stopTiming("navmesh");
		
		initialNode = navMesh.getNode( initialNodeIdx );
	}

	/**
	 * @return
	 */
	public Texture getBackgroundTexture()
	{
		return backgroundTexture;
	}

	/**
	 * @return
	 */
	public float getHeight() { return height; }
	/**
	 * @return
	 */
	public float getWidth() { return width; }

	/**
	 * @return
	 */
	public float getInitialZoom() { return initialZoom; }
	public Vector2 getInitialPoint() { return initialNode.getPoint(); }
	
	private void log(String message)
	{
		Gdx.app.log( name, message);
	}

	/**
	 * @param startingNode
	 */
	public Ant addAnt(NavNode startingNode)
	{
		Ant ant = Ant.getAnt( this, startingNode );
		
		ants.add( ant );
		spatialIndex.add( ant );
		
		return ant;
	}
	
	public void removeAnt(Ant ant)
	{
		Ant.free( ant );
		ants.remove( ant );
		spatialIndex.remove( ant );
	}

	/**
	 * @param delta
	 */
	public void update(float delta)
	{

		for(Ant ant : ants)
		{
			ant.update(delta);
			spatialIndex.update( ant );
		}
	}

	/**
	 * @return
	 */
	public NavMesh getNavMesh()
	{
		return navMesh;
	}

	public int OBJECT_ID = 0;
	/**
	 * @return
	 */
	public int createObjectId()
	{
		return OBJECT_ID ++;
	}

}
