package eir.world.unit.weapon;

import eir.resources.levels.UnitDef;
import eir.world.unit.UnitsFactory.UnitFactory;

public class WeaponFactory extends UnitFactory <Weapon>
{

	public WeaponFactory( )
	{

//		behaviors.put( TaskStage.ATTACK, new LinearTargetingBehavior() );
	}

	@Override
	protected Weapon createEmpty()
	{
		return new Weapon();
	}

	@Override
	protected Class <? extends UnitDef> getDefClass() {	return WeaponDef.class;	}
}
