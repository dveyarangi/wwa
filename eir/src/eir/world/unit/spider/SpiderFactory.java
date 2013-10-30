package eir.world.unit.spider;

import eir.world.unit.UnitsFactory.UnitFactory;

public class SpiderFactory extends UnitFactory <Spider>
{

	@Override
	protected Spider createEmpty() { return new Spider(); }

	@Override
	protected Class <Spider> getUnitClass() {
		return Spider.class;
	}


}
