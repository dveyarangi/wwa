package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.ai.GuardingOrder;

public class WildlifeController implements IController 
{
	
	private GuardingOrder guardingOrder;

	@Override
	public void init(Faction faction) {
		
//		guardingOrder = new GuardingOrder( 1, faction.getLevel().getNavMesh() );
		
//		faction.getScheduler().addOrder( "bidry", guardingOrder );
	}

	@Override
	public void yellUnitHit(Unit unit, Unit hitSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
