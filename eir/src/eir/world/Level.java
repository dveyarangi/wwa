package eir.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.world.environment.FloydWarshal;
import eir.world.environment.NavMesh;
import eir.world.environment.NavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.unit.Ant;
import eir.world.unit.Spider;

public class Level
{
	private String name;

	/**
	 * Level dimensions
	 */
	private int width, height;
	private float halfWidth, halfHeight;
	
	private InitialConfig initialConfig;
	
	/**
	 * Level background texture
	 */
	private Texture backgroundTexture;

	
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
	
	private Spider playerSpider;
	
	private List <Bullet> bullets;

	public Level()
	{
		navMesh = new FloydWarshal();
		ants = new HashSet <Ant> ();
		bullets = new LinkedList <Bullet> ();
	}
	
	public List <Asteroid> getAsteroids() { return asteroids;}

	public List <Web> getWebs() { return webs; }
	
	public Set <Ant> getAnts() { return ants; }
	
	/**
	 * @param factory  
	 */
	public void init()
	{
	
		halfWidth = width / 2;
		halfHeight = height / 2;
		
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
		
		Debug.startTiming("navmesh");
		navMesh.init();
		Debug.stopTiming("navmesh");
		
		
		Asteroid initialAsteroid = getAsteroid( initialConfig.getAsteroidName() );
			
		playerSpider = new Spider( this, initialAsteroid, initialConfig.getSurfaceIdx(), 10, 20 );

		// nav mesh initiated after this point
		////////////////////////////////////////////////////
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
		
		Iterator <Bullet> iterator = bullets.iterator();
		while(iterator.hasNext())
		{
			Bullet bullet = iterator.next();
			bullet.update( delta );
			if(!inWorldBounds(bullet.getArea().getAnchor()))
			{
				spatialIndex.remove( bullet );
				iterator.remove();
			}
			else
			{
				spatialIndex.update( bullet );
			}
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.begin();
		
		batch.draw( getBackgroundTexture(), -getWidth()/2, -getHeight()/2, getWidth(), getHeight() );
		
		// TODO: clipping?
		for(Asteroid asteroid : getAsteroids())
		{
			asteroid.draw( batch );
		}
		
		for( Web web : getWebs() )
		{
			web.draw( batch );
		}
		
		for(Bullet bullet : bullets)
		{
			bullet.draw( batch );
		}
		
		batch.end();		
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
	
	
	public class InitialConfig {
		
		/**
		 * Level initial settings
		 * TODO: extract to LevelInitialConfig classs
		 */
		private float zoom;
		private int nodeIdx;
		private String asteroid;		
		/**
		 * @return
		 */
		public float getZoom() { return zoom; }
		public int getSurfaceIdx() { return nodeIdx; }
		public String getAsteroidName() { return asteroid; }
	}

	public Asteroid getAsteroid(String name)
	{
		for(Asteroid asteroid : asteroids)
		{
			if(asteroid.getName().equals( name ))
				return asteroid;
		}
		
		return null;
	}

	public InitialConfig getInitialConfig()
	{
		return initialConfig;
	}

	/**
	 * @return
	 */
	public Spider getPlayerSpider()
	{
		return playerSpider;
	}

	/**
	 * @param playerSpider2
	 * @param pointerPosition2
	 */
	public void shoot(Spider spider, Vector2 targetPos)
	{
		Bullet bullet = spider.shoot( targetPos );
		bullets.add( bullet );
		spatialIndex.add( bullet );
	}
	
	public boolean inWorldBounds(Vector2 position)
	{
		return position.x < halfWidth  && position.x > -halfWidth
			&& position.y < halfHeight && position.y > -halfHeight;	
	}
}
