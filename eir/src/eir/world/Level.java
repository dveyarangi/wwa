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
import eir.resources.LevelLoadingContext;
import eir.world.environment.Environment;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.parallax.Background;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.RandomTravelingOrder;

public class Level
{
	////////////////////////////////////////////////////////////////
	// level physical environment:
	
	private String name;

	/**
	 * Level dimensions
	 */
	private int width, height;
	private float halfWidth, halfHeight;

	/**
	 * Level spatial and navigational environment
	 */
	private Environment environment;
	
	/** 
	 * Physical world, should be moved into Environment
	 */
	private World world;

	////////////////////////////////////////////////////////////////
	// renderables
	/**
	 * Level background images
	 */
	private Background background;

	
	private final List <Effect> effects;

	////////////////////////////////////////////////////////////////
	
	
	/**
	 * List of asteroids
	 */
	private List <Asteroid> asteroids;
	
	/**
	 * Participating factions
	 */
	private Faction [] factions;
	
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

	private Unit playerUnit;

	/**
	 * Units to add queue
	 */
	private final Queue <Unit> unitsToAdd = new LinkedList <Unit> ();
	
	private final Queue <Unit> unitsToRemove = new LinkedList <Unit> ();

	public Level()
	{
		units = new HashSet <Unit> ();
		effects = new LinkedList <Effect> ();
	}
	
	public List <Asteroid> getAsteroids() { return asteroids;}

	public List <Web> getWebs() { return webs; }
	
	public Set <Unit> getUnits() { return units; }

	/**
	 * @return
	 */
	public float getHeight() { return height; }
	/**
	 * @return
	 */
	public float getWidth() { return width; }
	
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
	 * @param context 
	 * @param factory  
	 */
	public void init(LevelLoadingContext context)
	{
		
		halfWidth = width / 2;
		halfHeight = height / 2;
		
		world = new World(new Vector2(0, 0), true); 
		
		
		environment = new Environment( context, this );
	
		
		for(Faction faction : factions)
		{
			faction.init( this );

			faction.getScheduler().addOrder( "ant", new RandomTravelingOrder( environment, 0 ) );
		}		
				
		for(Asteroid asteroid : asteroids)
		{
			asteroid.init( this );
		}
		
		for( Web web : webs )
		{
			web.init( environment.getGroundMesh() );
		}
		
		

		for(Unit unit : units)
		{
			addUnit( unit );
		}
		
		addUnit(playerUnit);



		// nav mesh initiated after this point
		////////////////////////////////////////////////////
		
	}




	/**
	 * @param delta
	 */
	public void update(float delta)
	{

		
		reassesUnits();
		
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
				environment.update( unit );
			}
			
			if(!unit.isAlive() ||
				!inWorldBounds(unit.getArea().getAnchor())) // unit may be dead from the collision
			{
				unitsToRemove.add ( unit );
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
	public Unit getControlledUnit()
	{
		return playerUnit;
	}
	
	public boolean inWorldBounds(Vector2 position)
	{
		return position.x < halfWidth  && position.x > -halfWidth
			&& position.y < halfHeight && position.y > -halfHeight;	
	}

	/**
	 * @param sourceNode
	 * @param targetNode
	 */
	public void toggleWeb(SurfaceNavNode sourceNode, SurfaceNavNode targetNode)
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
				if(( web.getSource() == sourceNode ) && ( web.getTarget() == targetNode )
				|| ( web.getTarget() == sourceNode ) && ( web.getSource() == targetNode ) )
					webIt.remove();
			}
			
			environment.getGroundMesh().unlinkNodes( sourceNode, targetNode );
		}
		
		
		Debug.startTiming("navmesh calculation");
		environment.getGroundMesh().update();
		Debug.stopTiming("navmesh calculation");

	}


	public Background getBackground() { return background; }

	public World getWorld() { return world; }

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
			Effect hitEffect = unit.getDeathEffect();
			if(hitEffect != null)
				effects.add( hitEffect );
			
			// dat questionable construct:
			unit.getFaction().removeUnit( unit );
			
			UnitsFactory.free( unit );
			debug("Unit removed: " + unit);
		}
	}
	

	
	private void debug(String message)
	{
		Gdx.app.debug( name, message);
	}

}
