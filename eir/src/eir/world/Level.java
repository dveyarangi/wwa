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
import com.badlogic.gdx.physics.box2d.World;

import eir.debug.Debug;
import eir.resources.LevelLoader.LoadingContext;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavMeshGenerator;
import eir.world.environment.nav.NavNode;
import eir.world.environment.parallax.Background;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.environment.spatial.UnitCollider;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.RandomTravelingOrder;

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
	
	private NavMesh groundMesh;
	private NavMesh airMesh;
	
	
	/**
	 * List of ants
	 * TODO: swap to identity set
	 */
	private Set <Unit> units;

	private Unit playerSpider;
	
	private List <Effect> effects;
	
	private UnitCollider collider;
	
	private World world;

	private Queue <Unit> unitsToAdd = new LinkedList <Unit> ();

	public Level()
	{
		units = new HashSet <Unit> ();
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
		
		groundMesh = context.navMesh;
		
		halfWidth = width / 2;
		halfHeight = height / 2;
		
		world = new World(new Vector2(0, 0), true); 
		
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
			web.init( this.getGroundNavMesh() );
		}
		
		
		for(Unit unit : units)
		{
			addUnit( unit );
		}
		
		addUnit(playerSpider);
		
		
		for(Faction faction : factions)
		{
			faction.init( this );

			faction.getScheduler().addOrder( "ant", new RandomTravelingOrder( groundMesh, 0 ) );
		}

		
		NavMeshGenerator generator = new NavMeshGenerator();
		
		Debug.startTiming("air nav mesh generation");
		airMesh = generator.generateMesh(getAsteroids(), new Vector2(-halfWidth+1, -halfHeight+1), new Vector2(halfWidth-1, halfHeight-1));
		Debug.stopTiming("air nav mesh generation");
		
		Debug.startTiming("navmesh calculation");
		groundMesh.init();
		Debug.stopTiming("navmesh calculation");
		
		for(int idx = 0; idx < groundMesh.getNodesNum(); idx ++)
		{
			groundMesh.getNode( idx ).init( this );
			spatialIndex.add( groundMesh.getNode( idx ) );
		}
		
		for(int idx = 0; idx < airMesh.getNodesNum(); idx ++)
		{
			airMesh.getNode( idx ).init( this );
			spatialIndex.add( airMesh.getNode( idx ) );
		}

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

	
	private void debug(String message)
	{
		Gdx.app.debug( name, message);
	}

	/**
	 * @param startingNode
	 */
	public Unit addUnit(Unit unit)
	{
		debug("Unit added: " + unit);
		unitsToAdd.add(unit);
		unit.getFaction().addUnit( unit );
		
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

		
		while(!unitsToAdd.isEmpty())
		{
			Unit unit = unitsToAdd.poll();
			units.add( unit );
			spatialIndex.add( unit );
		}
		
		for(Faction faction : factions)
			faction.update( delta );
				
		Iterator <Unit> unIt = units.iterator();
		while(unIt.hasNext())
		{
			Unit unit = unIt.next();
			
			if(unit.isAlive()) // unit may already be dead from previous hits
			{
				// updating position:
				unit.update(delta);
				spatialIndex.update( unit );
				
				// colliding:
				collider.setAnt( unit );
				spatialIndex.queryAABB(collider, unit.getArea() );
			}
			
			if(!unit.isAlive() ||
				!inWorldBounds(unit.getArea().getAnchor())) // unit may be dead from the collision
			{
				unIt.remove();
				spatialIndex.remove( unit );
				Effect hitEffect = unit.getDeathEffect();
				if(hitEffect != null)
					effects.add( hitEffect );
				
				// dat questionable construct:
				unit.getFaction().removeUnit( unit );
				
				UnitsFactory.free( unit );
				debug("Unit removed: " + unit);
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
		
		for(Unit unit : getUnits())
		{
			unit.draw( batch );
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
	public NavMesh getGroundNavMesh()
	{
		return groundMesh;
	}
	/**
	 * @return
	 */
	public NavMesh getAirNavMesh()
	{
		return airMesh;
	}
	public static int OBJECT_ID = 0;
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

	/**
	 * @return
	 */
	public Unit getControlledUnit()
	{
		return playerSpider;
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
		
		NavEdge edge = getGroundNavMesh().getEdge( sourceNode, targetNode );
		
		if(edge == null)
		{
			Web web = new Web(sourceNode, targetNode, 
  			"models/web_thread_01.png",
			"models/web_source_01.png",
			"models/web_target_01.png"	
			);
			
			webs.add( web );
			web.init( this.getGroundNavMesh() );
			
			
			
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
			
			getGroundNavMesh().unlinkNodes( sourceNode, targetNode );
		}
		
		
		Debug.startTiming("navmesh calculation");
		getGroundNavMesh().update();
		Debug.stopTiming("navmesh calculation");

	}


	public Background getBackground() { return background; }

	public World getWorld() { return world; }
}
