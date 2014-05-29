package eir.world.unit.spider;

import eir.world.unit.UnitsFactory;
import eir.world.unit.UnitsFactory.UnitFactory;

public class SpiderFactory extends UnitFactory <Spider>
{

	private final UnitsFactory unitsFactory;

	public SpiderFactory( final UnitsFactory unitsFactory )
	{
		this.unitsFactory = unitsFactory;
	}

	@Override
	protected Spider createEmpty() { return new Spider( unitsFactory ); }

	@Override
	protected Class <Spider> getUnitClass() {
		return Spider.class;
	}


}
