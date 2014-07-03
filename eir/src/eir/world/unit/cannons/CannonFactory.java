package eir.world.unit.cannons;

import eir.world.environment.Environment;
import eir.world.unit.UnitsFactory.UnitFactory;

public class CannonFactory extends UnitFactory <Cannon>
{

	private static final float SENSOR_RANGE = 100;

	private final Environment environment;

	public CannonFactory( final Environment environment )
	{
		this.environment = environment;

//		behaviors.put( TaskStage.ATTACK, new LinearTargetingBehavior() );
	}

	@Override
	protected Cannon createEmpty()
	{
		return new Cannon();
	}

	@Override
	protected Class<Cannon> getUnitClass()
	{
		// TODO Auto-generated method stub
		return Cannon.class;
	}
}
