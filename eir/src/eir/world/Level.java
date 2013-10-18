package eir.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.resources.LevelLoader.LoadingContext;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.parallax.Background;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.environment.spatial.UnitCollider;
import eir.world.unit.UnitsFactory;
import eir.world.unit.Bullet;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.spider.Spider;

public class Level
{
	////////////////////////////////////////////////////////////////
	// loaded by json:
	
	private String name;

	/**
	 * Level dimensions
	 */
	private int width, height;
	private float halfWidth, halfHeight;
	
	private InitialConfig initialConfig;
	
	private Background background;


	/**
	 * List of asteroids
	 */
	private List <Asteroid> asteroids;
	
	/**
	 * List of webs
	 */
	private List <Web> webs;
	
	
	private Faction [] factions;
	
	////////////////////////////////////////////////////////////////
	
	/**
	 * Indexer for ants and bullets.
	 */
	private SpatialHashMap<ISpatialObject> spatialIndex;
	
	private NavMesh navMesh;
	
	
	/**
	 * List of ants
	 * TODO: swap to identity set
	 */
	private Set <Unit> units;
	
	private static final int PLAYER_ID = 1;
	private static final int ENEMY_ID = 2;
	private Spider playerSpider;
	
	private List <Bullet> bullets;
	
	
	private List <Effect> effects;
	
	private UnitCollider collider;

	private Queue <Unit> unitsToAdd = new LinkedList <Unit> ();

	public Level()
	{
		units = new HashSet <Unit> ();
		bullets = new LinkedList <Bullet> ();
		effects = new LinkedList <Effect> ();
		collider = new UnitCollider();
	}
	
	public List <Asteroid> getAsteroids() { return asteroids;}

	public List <Web> getWebs() { return webs; }
	
	public Set <Unit> getUnits() { return units; }
	
	/**
	 * @param context 
	 * @param factory  
	 */
	public void init(LoadingContext context)
	{
		navMesh = context.navMesh;
	
		halfWidth = width / 2;
		halfHeight = height / 2;
		
		spatialIndex = new SpatialHashMap<ISpatialObject>( 
				name+ "-spatial", 
				16f, // size of bucket
				width, height );
		
		
/*		for(Asteroid asteroid : asteroids)
		{
			asteroid.init();
		}*/
		
		for( Web web : webs )
		{
			web.init( this.getNavMesh() );
		}
		
		for(Faction faction : factions)
		{
			faction.init( this );
		}
		
		for(Unit unit : units)
		{
			addUnit( unit );
		}
		
		Debug.startTiming("navmesh calculation");
		navMesh.init();
		Debug.stopTiming("navmesh calculation");
		
		for(int idx = 0; idx < navMesh.getNodesNum(); idx ++)
		{
			navMesh.getNode( idx ).init( this );
			spatialIndex.add( navMesh.getNode( idx ) );
		}
		Asteroid initialAsteroid = getAsteroid( initialConfig.getAsteroidName() );
			
		playerSpider = new Spider( PLAYER_ID, this, initialAsteroid, initialConfig.getSurfaceIdx(), 10, 80 );

		// nav mesh initiated after this point
		////////////////////////////////////////////////////
		

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
	public Unit addUnit(Unit unit)
	{
		unitsToAdd.add(unit);
		
		return unit;
	}

	
	public void addEffect(Effect effect)
	{
		effects.add( effect );
	}

	/**
	 * @param delta
	 */
	public void update(float delta)
	{

		for(Faction faction : factions)
			faction.update( delta );
		
		while(!unitsToAdd.isEmpty())
		{
			Unit unit = unitsToAdd.poll();
			units.add( unit );
			spatialIndex.add( unit );
		}
				
		Iterator <Unit> unIt = units.iterator();
		while(unIt.hasNext())
		{
			Unit unit = unIt.next();
			unit.update(delta);
			
			spatialIndex.update( unit );
			
			collider.setAnt( unit );
			
			spatialIndex.queryAABB(collider, unit.getArea() );
			if(!unit.isAlive())
			{
				unIt.remove();
				spatialIndex.remove( unit );
				Effect hitEffect = unit.getDeathEffect();
				if(hitEffect != null)
					effects.add( hitEffect );
				
				// dat questionable construct:
				unit.getFaction().removeUnit( unit );
				
				UnitsFactory.free( unit );
			}
			
		}

		Iterator <Bullet> bulletIt = bullets.iterator();
		while(bulletIt.hasNext())
		{
			Bullet bullet = bulletIt.next();
			if(bullet.isAlive())
				bullet.update( delta );
			if(!inWorldBounds(bullet.getArea().getAnchor()) 
			|| !bullet.isAlive())
			{
				spatialIndex.remove( bullet );
				bulletIt.remove();
				Bullet.free( bullet );
				
				Effect hitEffect = bullet.weapon.createHitEffect( bullet );
				if(hitEffect != null)
					effects.add( hitEffect );
			}
			else
			{
				spatialIndex.update( bullet );
			}
		}
		Iterator <Effect> effectIt = effects.iterator();
		while(effectIt.hasNext())
		{
			Effect effect = effectIt.next();
			effect.update( delta );
			if(!effect.isAlive())
			{

				effectIt.remove();
				Effect.free( effect );
			}

		}	
		
		
	}
	
	public void draw(SpriteBatch batch)
	{

		
		batch.begin();
		
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
		
		for(Effect effect : effects)
		{
			effect.draw( batch );
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

	public static int OBJECT_ID = 0;
	/**
	 * @return
	 */
	public static int createObjectId()
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
	public void shoot(Spider spider, Bullet bullet)
	{
		bullets.add( bullet );
		spatialIndex.add( bullet );
	}
	
	public boolean inWorldBounds(Vector2 position)
	{
		return position.x < halfWidth  && position.x > -halfWidth
			&& position.y < halfHeight && position.y > -halfHeight;	
	}

	/**
	 * @return
	 */
	public SpatialHashMap <ISpatialObject> getSpatialIndex()
	{
		return spatialIndex;
	}

	/**
	 * @param sourceNode
	 * @param targetNode
	 */
	public void toggleWeb(NavNode sourceNode, NavNode targetNode)
	{
		
		if(sourceNode.getDescriptor().getObject() == targetNode.getDescriptor().getObject())
			return;
		
		NavEdge edge = getNavMesh().getEdge( sourceNode, targetNode );
		
		if(edge == null)
		{
			Web web = new Web(sourceNode, targetNode, 
  			"models/web_thread_01.png",
			"models/web_source_01.png",
			"models/web_target_01.png"	
			);
			
			webs.add( web );
			web.init( this.getNavMesh() );
			
			
			
		}
		else
		{
			System.out.println("removing web: " + sourceNode + " <---> " + targetNode);
			Iterator <Web> webIt = webs.iterator();
			while(webIt.hasNext())
			{
				Web web = webIt.next();
				if(( web.getSource() == sourceNode ) && ( web.getTarget() == targetNode )
				|| ( web.getTarget() == sourceNode ) && ( web.getSource() == targetNode ) )
					webIt.remove();
			}
			
			getNavMesh().unlinkNodes( sourceNode, targetNode );
		}
		
		
		Debug.startTiming("navmesh calculation");
		navMesh.update();
		Debug.stopTiming("navmesh calculation");

	}


	public Background getBackground() { return background; }
}
