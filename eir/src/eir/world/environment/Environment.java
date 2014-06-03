package eir.world.environment;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import eir.debug.Debug;
import eir.resources.LevelLoadingContext;
import eir.world.Level;
import eir.world.environment.nav.AirNavNode;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavMeshGenerator;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.sensors.ISensor;
import eir.world.environment.sensors.Sensor;
import eir.world.environment.spatial.AOECollider;
import eir.world.environment.spatial.ISpatialFilter;
import eir.world.environment.spatial.ISpatialIndex;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.SpatialHashMap;
import eir.world.environment.spatial.UnitCollider;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

public class Environment
{
	private NavMesh <SurfaceNavNode> groundMesh;
	private NavMesh <AirNavNode> airMesh;

	private ISpatialIndex <ISpatialObject> index;

	private final UnitCollider collider;

	private final AOECollider aoeCollider;

	/**
	 * Physical world, should be moved into Environment
	 */
	private final World world;


	public static int OBJECT_ID = 0;
	public static int createObjectId()
	{
		return OBJECT_ID ++;
	}


	public Environment( )
	{
		world = new World(new Vector2(0, 0), true);

		collider = new UnitCollider();

		aoeCollider = new AOECollider();
		// TODO Auto-generated constructor stub
	}

	public void init(final LevelLoadingContext context, final Level level)
	{

		groundMesh = context.navMesh;
		index = new SpatialHashMap<ISpatialObject>(
				"spatial",
				16f, // size of bucket
				level.getWidth(), level.getHeight() );

		NavMeshGenerator generator = new NavMeshGenerator();

		Debug.startTiming("air nav mesh generation");
		airMesh = generator.generateMesh(level.getAsteroids(),
				new Vector2(-level.getWidth()/2+1, -level.getHeight()/2+1),
				new Vector2( level.getWidth()-1,    level.getHeight()/2-1));
		Debug.stopTiming("air nav mesh generation");

		Debug.startTiming("navmesh calculation");
		groundMesh.init();
		Debug.stopTiming("navmesh calculation");

		for(int idx = 0; idx < groundMesh.getNodesNum(); idx ++)
		{
			groundMesh.getNode( idx ).init( );
			index.add( groundMesh.getNode( idx ) );
		}

		for(int idx = 0; idx < airMesh.getNodesNum(); idx ++)
		{
			airMesh.getNode( idx ).init();
			index.add( airMesh.getNode( idx ) );
		}

	}

	public World getPhyisics() { return world; }

	public NavMesh <SurfaceNavNode> getGroundMesh() { return groundMesh; }
	public NavMesh <AirNavNode> getAirMesh() { return airMesh; }

	public ISpatialIndex <ISpatialObject> getIndex() { return index; }




	public void add( final Unit unit ) { index.add( unit ); }
	public void update( final Unit unit )
	{
		index.update( unit );

		// colliding:
		collider.setAnt( unit );
		index.queryAABB(collider, unit.getArea() );


	}
	public void update( final float delta )
	{
		for( int idx = 0; idx < collider.getAOEDamage().size(); idx ++ )
		{
			IDamager damager = collider.getAOEDamage().get( idx );
			aoeCollider.setDamager( damager );
			Unit damagingUnit = (Unit) damager;
			index.queryRadius( aoeCollider,
					damagingUnit.getArea().getAnchor().x,
					damagingUnit.getArea().getAnchor().y, damager.getSize() );
			aoeCollider.clear();
		}

		collider.clear();
	}

	public void remove( final Unit unit ) { index.remove( unit ); }


	public AirNavNode getClosestAirNode( final Vector2 anchor )
	{
		return (AirNavNode) index.findClosest( new AirNavNodeFilter(), anchor.x, anchor.y );
	}
	public SurfaceNavNode getClosestSurfaceNode( final Vector2 anchor )
	{
		return (SurfaceNavNode) index.findClosest( new SurfaceNavNodeFilter(), anchor.x, anchor.y );
	}


	private static class AirNavNodeFilter implements ISpatialFilter <ISpatialObject>
	{

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof AirNavNode;
		}

	}
	private static class SurfaceNavNodeFilter implements ISpatialFilter <ISpatialObject>
	{

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof SurfaceNavNode;
		}

	}

	public ISensor createSensor( final Unit unit, final float radius )
	{
		return new Sensor( radius, unit, index, world );
	}
}
