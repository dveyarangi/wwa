package eir.world.unit;

import eir.world.environment.nav.Route;

public interface IRoutedUnit extends IUnit, ITargetedUnit
{
	public Route getRoute();
}
