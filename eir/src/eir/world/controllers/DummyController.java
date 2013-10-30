package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.Unit;

public class DummyController implements IController 
{

	@Override
	public void init(Faction faction) {
	}

	@Override
	public void yellUnitHit(Unit unit, Unit hitSource) {
	}

	@Override
	public void update(float delta) {
	}

}
