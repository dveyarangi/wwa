package eir.world.unit.cannons;

import eir.world.unit.UnitsFactory.UnitFactory;

public class CannonFactory extends UnitFactory <Cannon>
{

	private static final float SENSOR_RANGE = 100;

	public CannonFactory( )
	{

//		behaviors.put( TaskStage.ATTACK, new LinearTargetingBehavior() );
	}

	@Override
	protected Cannon createEmpty()
	{
		return new Cannon();
	}
}
