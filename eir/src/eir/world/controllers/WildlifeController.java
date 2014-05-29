package eir.world.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eir.world.unit.Faction;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.AttackingOrder;
import eir.world.unit.ai.GuardingOrder;
import eir.world.unit.structure.Spawner;
import eir.world.unit.weapon.Bullet;

public class WildlifeController implements IController
{

	private Faction faction;

	private Map <Unit, GuardingOrder> guardingOrders;

	private Map <Unit, AttackingOrder> attackingOrders;

	private static final int ANGER_TIMEOUT = 20;
	private static final int ANGER_BY_HIT = 5;

	private static final float ANGER_SPAWN_MODIFIER = 0.1f;

	public boolean spawnersGuardsInited = false;

	@Override
	public void init(final Faction faction) {

		this.faction = faction;

		guardingOrders = new HashMap <Unit, GuardingOrder> ();

		attackingOrders = new HashMap <Unit, AttackingOrder> ();;


	}


	@Override
	public void yellUnitHit(final Unit unit, final IDamager hitSource)
	{
		if(unit.getType() == UnitsFactory.SPAWNER)
		{
			if(hitSource.getType() != UnitsFactory.BULLET)
				return;

			Unit target = ((Bullet)hitSource).getWeapon().getOwner();
			AttackingOrder order = attackingOrders.get(unit);
			if(order != null )
			{
				order.timeout += ANGER_BY_HIT;
				return;
			}

			faction.getScheduler().removeOrder( UnitsFactory.BIDRY, guardingOrders.get(unit) );
			guardingOrders.remove(unit);

			order = new AttackingOrder(1);
			order.timeout = ANGER_TIMEOUT;
			order.setUnit(target);
			attackingOrders.put(unit, order);
			faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);

			Spawner sp = (Spawner)unit;
			sp.spawnInterval = sp.spawnInterval * ANGER_SPAWN_MODIFIER;
		}
	}

	@Override
	public void update(final float delta)
	{
		if(!spawnersGuardsInited)
		{
			pickDefenseTargets();
			spawnersGuardsInited = true;
		}
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

				Spawner sp = (Spawner)spawner;
				sp.spawnInterval = sp.spawnInterval / ANGER_SPAWN_MODIFIER;
			}

		}

	}


	private void pickDefenseTargets()
	{
		for( Unit unit : faction.getUnitsByType( UnitsFactory.SPAWNER ) )
		{
			GuardingOrder order = new GuardingOrder(1);
			order.setTargetNode(unit.anchor);

			guardingOrders.put(unit, order);

			faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);
		}
	}

}
