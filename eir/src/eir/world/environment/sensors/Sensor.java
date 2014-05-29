package eir.world.environment.sensors;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import eir.world.environment.MazeType;
import eir.world.environment.spatial.ISpatialIndex;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.environment.spatial.ISpatialSensor;
import eir.world.unit.Unit;

public class Sensor implements RayCastCallback, ISpatialSensor <ISpatialObject>, ISensor
{

	private final ISpatialIndex <ISpatialObject> index;
	private final World world;

	private final float radius;

	private Vector2 location;


	private List <Unit> units;

	private boolean isOccluded = false;


	public Sensor(final float radius, final Vector2 location, final ISpatialIndex <ISpatialObject> index, final World world)
	{

		this.units = new LinkedList <Unit> ();

		this.index = index;
		this.world = world;

		this.radius = radius;
		this.location = location;
	}

	@Override
	public float reportRayFixture( final Fixture fixture, final Vector2 point,
			final Vector2 normal, final float fraction )
	{

		if( fixture.getUserData() == MazeType.ASTEROID)
		{
			this.isOccluded = true;
			return 0;
		}

		return -1;
	}

	@Override
	public List <Unit> sense()
	{
		clear();
		index.queryRadius( this, location.x, location.y, radius);

		return units;
	}

	@Override
	public boolean objectFound( final ISpatialObject unit )
	{
		if(!(unit instanceof Unit))
			return false;

		if(unit.getArea().getAnchor() == location) // TODO: self
			return false;

		isOccluded = false;

		float distanceSquare = unit.getArea().getAnchor().dst2( location );

		if(distanceSquare < 0.1f)
			return false;
		world.rayCast( this, location, unit.getArea().getAnchor() );

		if(isOccluded)
			return true;

		units.add( (Unit)unit );

		return false;
	}

	@Override
	public void clear()
	{
		isOccluded = false;
		units.clear();
	}

}
