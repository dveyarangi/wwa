package eir.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.resources.LevelInitialSettings;
import eir.resources.levels.AsteroidDef;
import eir.resources.levels.FactionDef;
import eir.resources.levels.IUnitDef;
import eir.resources.levels.LevelDef;
import eir.resources.levels.UnitAnchorDef;
import eir.world.environment.Environment;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.parallax.Background;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.AttackingOrder;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Level
{
	////////////////////////////////////////////////////////////////
	// level physical environment:

	private LevelDef def;

	private float halfWidth, halfHeight;

	/**
	 * Level spatial and navigational environment
	 */
	private Environment environment;

	////////////////////////////////////////////////////////////////
	// renderables
	/**
	 * Level background images
	 */
	private Background background;
	////////////////////////////////////////////////////////////////

	/**
	 * List of asteroids
	 */
	private Map <String, Asteroid> asteroids;

	/**
	 * Participating factions
	 */
	private TIntObjectHashMap<Faction> factions;

	/**
	 * Set of units in game
	 * TODO: swap to identity set?
	 */
	private final Set <Unit> units;

	/**
	 * List of webs
	 */
	private List <Web> webs;

	////////////////////////////////////////////////////////////////

	/**
	 * Units to add queue
	 */
	private final Queue <Unit> unitsToAdd = new LinkedList <Unit> ();

	private final Queue <Unit> unitsToRemove = new LinkedList <Unit> ();

	private UnitsFactory unitsFactory;

	public Level( final UnitsFactory unitsFactory )
	{
		this.unitsFactory = unitsFactory;
		asteroids = new HashMap <String, Asteroid> ();
		units = new HashSet <Unit> ();
		webs = new ArrayList <Web> ();
		factions = new TIntObjectHashMap<Faction> ();
	}



	public Collection <Asteroid> getAsteroids() { return asteroids.values(); }
	public Asteroid getAsteroid( final String name ) { return asteroids.get( name ); }

	public List <Web> getWebs() { return webs; }

	public Set <Unit> getUnits() { return units; }

	/**
	 * @return
	 */
	public float getHeight() { return def.getHeight(); }
	/**
	 * @return
	 */
	public float getWidth() { return def.getWidth(); }

	/**
	 * @return
	 */
	public float getHalfHeight() { return halfHeight; }
	/**
	 * @return
	 */
	public float getHalfWidth() { return halfWidth; }

	/**
	 * @param startingNode
	 */
	public Unit addUnit(final Unit unit)
	{
//		debug("Unit added: " + unit);
		unitsToAdd.add(unit);
		unit.getFaction().addUnit( unit );

		return unit;
	}

	/**
	 * @param context
	 * @param factory
	 */
	public void init(final LevelDef def, final GameFactory gameFactory )
	{
		this.def = def;



		halfWidth = def.getWidth() / 2;
		halfHeight = def.getHeight() / 2;

		this.environment = new Environment();

		this.unitsFactory = new UnitsFactory( gameFactory );

		this.background = def.getBackgroundDef();

		for( AsteroidDef asteroidDef : def.getAsteroidDefs() )
		{
			Asteroid asteroid = new Asteroid();

			asteroid.init( gameFactory, this, asteroidDef );

			asteroids.put( asteroidDef.getName(), asteroid );
		}

		environment.init( this );

		for( FactionDef factionDef : def.getFactionDefs() )
		{
			Faction faction = new Faction();
			faction.init( gameFactory, this, factionDef );

//			faction.getScheduler().addOrder( UnitsFactory.ANT, new RandomTravelingOrder( environment, 0 ) );
			faction.getScheduler().addOrder( UnitsFactory.CANNON, new AttackingOrder( 0 ) );

			factions.put( factionDef.getOwnerId(), faction );
		}


		for( Web web : webs )
		{
			web.init( environment.getGroundMesh() );
		}

		for(IUnitDef unitDef : def.getUnitDefs())
		{
			UnitAnchorDef anchorDef = unitDef.getAnchorDef();
			Asteroid asteroid = getAsteroid( anchorDef.getAsteroidName() );

			SurfaceNavNode anchor = asteroid.getModel().getNavNode( anchorDef.getNodeIdx() );

			Faction faction = factions.get( unitDef.getFactionId() );

			Unit unit = getUnitsFactory().getUnit( gameFactory, this, unitDef, anchor, faction );

			addUnit(unit);
		}

//		this.playerUnit = new Spider( unitsFactory );

		//		addUnit(playerUnit);



		// nav mesh initiated after this point
		////////////////////////////////////////////////////

	}




	/**
	 * @param delta
	 */
	public void update(final float delta)
	{


		reassesUnits();

		for(int fid : factions.keys())
		{
			Faction faction = factions.get( fid );
			faction.update( delta );
		}

		Iterator <Unit> unIt = units.iterator();
		while(unIt.hasNext())
		{
			Unit unit = unIt.next();

			if(unit.isAlive()) // unit may already be dead from previous hits
			{
				// updating position:
				unit.update(delta);
				environment.update( unit );
			}

			if(!unit.isAlive() ||
					!inWorldBounds(unit.getArea().getAnchor())) // unit may be dead from the collision
			{
				unitsToRemove.add ( unit );
			}

		}

		environment.update( delta );


	}

	public void draw(final SpriteBatch batch)
	{


	}

	public boolean inWorldBounds(final Vector2 position)
	{
		return position.x < halfWidth  && position.x > -halfWidth
				&& position.y < halfHeight && position.y > -halfHeight;
	}

	/**
	 * @param sourceNode
	 * @param targetNode
	 */
	public void toggleWeb(final SurfaceNavNode sourceNode, final SurfaceNavNode targetNode)
	{

		if(sourceNode.getDescriptor().getObject() == targetNode.getDescriptor().getObject())
			return;

		NavEdge <SurfaceNavNode> edge = environment.getGroundMesh().getEdge( sourceNode, targetNode );

		if(edge == null)
		{
			Web web = new Web(sourceNode, targetNode,
					"models/web_thread_01.png",
					"models/web_source_01.png",
					"models/web_target_01.png"
					);

			webs.add( web );
			web.init( environment.getGroundMesh() );



		}
		else
		{
			System.out.println("removing web: " + sourceNode + " <---> " + targetNode);
			Iterator <Web> webIt = webs.iterator();
			while(webIt.hasNext())
			{
				Web web = webIt.next();
				if(web.getSource() == sourceNode && web.getTarget() == targetNode
						|| web.getTarget() == sourceNode && web.getSource() == targetNode )
				{
					webIt.remove();
				}
			}

			environment.getGroundMesh().unlinkNodes( sourceNode, targetNode );
		}


		Debug.startTiming("navmesh calculation");
		environment.getGroundMesh().update();
		Debug.stopTiming("navmesh calculation");

	}


	public Background getBackground() { return background; }

	public String getName() { return null; }

	public Environment getEnvironment() { return environment; }

	/**
	 * Add and remove pending units
	 */
	protected void reassesUnits()
	{
		while(!unitsToAdd.isEmpty())
		{
			Unit unit = unitsToAdd.poll();
			units.add( unit );
			environment.add( unit );
		}

		while(!unitsToRemove.isEmpty())
		{
			Unit unit = unitsToRemove.poll();

			units.remove( unit );
			environment.remove( unit );
			// dat questionable construct:
			unit.getFaction().removeUnit( unit );

			unitsFactory.free( unit );
//			debug("Unit removed: " + unit);
		}
	}



	private void debug(final String message)
	{
		Gdx.app.debug( def.getName(), message);
	}

	public UnitsFactory getUnitsFactory() {	return unitsFactory; }

	public Faction getFaction( final int factionId) { return factions.get( factionId ); }


	/**
	 * Get misc parameters for game beginning
	 * @return
	 */
	public LevelInitialSettings getInitialSettings() { return def.getInitialSettings(); }

}
