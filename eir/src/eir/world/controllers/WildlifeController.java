package eir.world.controllers;

import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.AttackingOrder;
import eir.world.unit.ai.GuardingOrder;
import eir.world.unit.weapon.Bullet;

public class WildlifeController implements IController 
{
	
	private Faction faction;
	
	private GuardingOrder guardingOrder;
	
	private float guardingTimeout;
	
	private AttackingOrder attackingOrder;
	
	private float attackTimeout;

	@Override
	public void init(Faction faction) {
		
		this.faction = faction;
		
		guardingOrder = new GuardingOrder(1);
		
		attackingOrder = new AttackingOrder(0);
		
		guardingOrder.setActive(true);
		attackingOrder.setActive(false);
	
		faction.getScheduler().addOrder(UnitsFactory.BIDRY, guardingOrder);
		faction.getScheduler().addOrder(UnitsFactory.BIDRY, attackingOrder);
		
		
		
		
//		guardingOrder = new GuardingOrder( 1, faction.getLevel().getNavMesh() );
		
//		faction.getScheduler().addOrder( "bidry", guardingOrder );
	}
	
	public Unit pickGuardTarget()
	{
		for(Unit unit : faction.getUnits())
		{
			if(unit.getType().equals(UnitsFactory.SPAWNER))
			{
				return unit;
			}
		}
		return null;
	}

	@Override
	public void yellUnitHit(Unit unit, Unit hitSource) 
	{
		if(attackTimeout <= 0 && hitSource.getType() == UnitsFactory.BULLET)
		{
			guardingOrder.setActive(false);
			attackingOrder.setActive(true);
			attackingOrder.setUnit(((Bullet)hitSource).getWeapon().getOwner());
			
			attackTimeout = 20;
		}
	}

	@Override
	public void update(float delta) 
	{
		guardingTimeout -= delta;
//		if(quardingTimeout <= 0)
//		{
			guardingOrder.setTargetNode( pickGuardTarget().anchor );
//		}
		
		attackTimeout -= delta;
		
		if(attackTimeout <= 0)
		{
			guardingOrder.setActive(true);
			attackingOrder.setActive(false);
		}
	}

}
