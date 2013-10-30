package eir.world.controllers;

import java.util.HashMap;
import java.util.Map;

import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.AttackingOrder;
import eir.world.unit.ai.GuardingOrder;
import eir.world.unit.weapon.Bullet;

public class WildlifeController implements IController 
{
	
	private Faction faction;
	
	private Map <Unit, GuardingOrder> guardingOrders;
	
	private Map <Unit, AttackingOrder> attackingOrders;
	

	@Override
	public void init(Faction faction) {
		
		this.faction = faction;
		
		guardingOrders = new HashMap <Unit, GuardingOrder> ();
		
		attackingOrders = new HashMap <Unit, AttackingOrder> ();;
	
		for(Unit unit : faction.getUnits())
		{
			if(unit.getType() == UnitsFactory.SPAWNER)
			{
				GuardingOrder order = new GuardingOrder(1);
				order.setTargetNode(unit.anchor);
				guardingOrders.put(unit, order);
				faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);
			}
		}
		
	}


	@Override
	public void yellUnitHit(Unit unit, Unit hitSource) 
	{
		if(unit.getType() == UnitsFactory.SPAWNER)
		{
			if(hitSource instanceof Bullet)
			{
				Unit target = ((Bullet)hitSource).getWeapon().getOwner();
				AttackingOrder order = attackingOrders.get(target);
				if(order != null || hitSource.getType() != UnitsFactory.BULLET)
				{
					return;
				}
				
				faction.getScheduler().removeOrder( UnitsFactory.BIDRY, guardingOrders.get(unit) );
				guardingOrders.remove(unit);
				
				order = new AttackingOrder(1);
				order.setUnit(target);
				attackingOrders.put(unit, order);
				faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);
			}
		}
	}

	@Override
	public void update(float delta) 
	{
		Unit reguardUnit = null;
		for(Unit attacker : attackingOrders.keySet())
		{
			AttackingOrder order = attackingOrders.get(attacker);
			
			order.timeout -= delta;
			
			if(!order.getUnit().isAlive() || order.timeout <= 0)
			{
				faction.getScheduler().removeOrder(UnitsFactory.BIDRY, order);
				reguardUnit = attacker;
				continue;
			}

		}

		if(reguardUnit != null)
		{
			GuardingOrder order = new GuardingOrder(1);
			order.setTargetNode(reguardUnit.anchor);
			guardingOrders.put(reguardUnit, order);
			faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);
			
			attackingOrders.remove(reguardUnit);
		}
	}

}
