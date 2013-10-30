package eir.world.controllers;

import java.util.HashMap;
import java.util.Iterator;
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
		Iterator <Map.Entry<Unit, AttackingOrder>> eit = attackingOrders.entrySet().iterator();
		while(eit.hasNext())
		{
			Map.Entry<Unit, AttackingOrder> entry = eit.next();
			Unit spawner = entry.getKey();
			AttackingOrder attackingOrder = entry.getValue();
			
			attackingOrder.timeout -= delta;
			
			if(!attackingOrder.getUnit().isAlive() || attackingOrder.timeout <= 0)
			{
				faction.getScheduler().removeOrder(UnitsFactory.BIDRY, attackingOrder);
				eit.remove();

				GuardingOrder guardingOrder = new GuardingOrder(1);
				guardingOrder.setTargetNode(spawner.anchor);
				guardingOrders.put(spawner, guardingOrder);
				faction.getScheduler().addOrder(UnitsFactory.BIDRY, guardingOrder);
			}

		}

	}

}
