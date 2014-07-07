package eir.world.unit.cannons;

import eir.resources.levels.UnitDef;
import eir.world.unit.UnitsFactory.UnitFactory;

public class CannonFactory extends UnitFactory <Cannon>
{

	public CannonFactory( )
	{

//		behaviors.put( TaskStage.ATTACK, new LinearTargetingBehavior() );
	}

	@Override
	protected Cannon createEmpty()
	{
		return new Cannon();
	}

	@Override
	protected Class <? extends UnitDef> getDefClass() {	return CannonDef.class;	}
}
