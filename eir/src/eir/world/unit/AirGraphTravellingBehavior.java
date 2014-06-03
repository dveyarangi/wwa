package eir.world.unit;

import eir.debug.Debug;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.Route;
import eir.world.unit.ai.Task;

public class AirGraphTravellingBehavior implements UnitBehavior <IRoutedUnit>
{
	private UnitBehavior travellingBehavior;

	public static final float ARRIVAL_DISTANCE_SQUARE = 50;
	public AirGraphTravellingBehavior(final UnitBehavior travellingBehavior)
	{
		this.travellingBehavior = travellingBehavior;
	}

	@Override
	public void update( final float delta, final Task task, final IRoutedUnit entity )
	{
		Unit unit = (Unit)entity;

		Route route = entity.getRoute();
		if(route == null)
		{
			Debug.log( "Unit " + unit + " has no route." );
			return;
		}

		if( !route.hasNext() )
		{
			Debug.log( "Unit " + unit + " has no route." );
			return;
		}

		if(unit.target == null || unit.getArea().getAnchor().dst2( unit.getArea().getAnchor() )
				< 10 )
		{
			NavNode node = route.next();
			unit.target = node;
		}
		travellingBehavior.update( delta, task, unit );
	}


/*	protected NavNode getNextNode( final INodingUnit unit)
	{
		ISensor sensor = unit.getSensor();

		if( sensor == null)
			throw new IllegalArgumentException("Unit " + unit + " does not have sensor.");

		List <ISpatialObject> objects = sensor.sense( ISensingFilter.AIR_NODES_FILTER);

		AirNavNode nextNode = null;
		float minDistance = Float.MAX_VALUE;
		for( int idx = 0; idx < objects.size(); idx ++)
		{
			AirNavNode node = (AirNavNode) objects.get( idx );

			node.getDistanceTo( unit.getTarget() )
		}
	}*/

}
