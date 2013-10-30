package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.Unit;

public interface IController {


	void init(Faction faction);

	void yellUnitHit(Unit unit, Unit hitSource);
	
	void update( float delta );
}
