package eir.world.unit;

import eir.world.environment.nav.Route;

public interface IRoutedUnit extends IUnit
{
	public Route getRoute();
}
