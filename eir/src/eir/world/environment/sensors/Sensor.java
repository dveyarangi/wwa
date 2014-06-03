package eir.world.environment.sensors;

import java.util.ArrayList;
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

	private Unit baseUnit;


	private List <ISpatialObject> units;

	private boolean isOccluded = false;

	private ISensingFilter filter;

	public Sensor(final float radius, final Unit baseUnit, final ISpatialIndex <ISpatialObject> index, final World world )
	{

		this.units = new ArrayList <ISpatialObject> ();

		this.index = index;
		this.world = world;

		this.radius = radius;
		this.baseUnit = baseUnit;
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
	public List <ISpatialObject> sense( final ISensingFilter filter )
	{
		clear();

		this.filter = filter;

		index.queryRadius( this, baseUnit.getArea().getAnchor().x, baseUnit.getArea().getAnchor().y, radius);

		return units;
	}

	@Override
	public boolean objectFound( final ISpatialObject object )
	{
		if( filter != null && ! filter.accept( object ) )
			return false;

		if(object == this.baseUnit) // TODO: self
			return false;

		isOccluded = false;


		float distanceSquare = object.getArea().getAnchor().dst2( baseUnit.getArea().getAnchor() );

		if(distanceSquare < 0.1f)
			return false;
		world.rayCast( this, baseUnit.getArea().getAnchor(), object.getArea().getAnchor() );

		if(isOccluded)
			return true;

		units.add( object );

		return false;
	}

	@Override
	public void clear()
	{
		isOccluded = false;
		units.clear();

		this.filter = null;
	}

}
