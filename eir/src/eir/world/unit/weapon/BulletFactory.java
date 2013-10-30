package eir.world.unit.weapon;

import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory.UnitFactory;

public class BulletFactory extends UnitFactory <Bullet>
{

	@Override
	protected Bullet createEmpty() {
		return new Bullet();
	}

	@Override
	protected Class <Bullet> getUnitClass() {
		return Bullet.class;
	}

}
