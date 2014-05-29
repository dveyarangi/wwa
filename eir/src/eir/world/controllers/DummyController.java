package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

public class DummyController implements IController
{

	@Override
	public void init(final Faction faction) {
	}

	@Override
	public void yellUnitHit(final Unit unit, final IDamager hitSource) {
	}

	@Override
	public void update(final float delta) {
	}

}
