package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

public interface IController {

	public void unitAdded(Unit unit);

	void init(Faction faction);

	void yellUnitHit(Unit unit, IDamager hitSource);

	void update( float delta );
}
