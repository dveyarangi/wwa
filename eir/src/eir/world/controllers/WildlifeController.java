package eir.world.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eir.world.Asteroid;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.Faction;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.AttackingOrder;
import eir.world.unit.ai.PolygonGuardingOrder;
import gnu.trove.map.hash.TObjectFloatHashMap;

public class WildlifeController implements IController
{

	private Faction faction;

	private Map <Object, PolygonGuardingOrder> guardingOrders;

	private Map <Unit, AttackingOrder> attackingOrders;

	private TObjectFloatHashMap <Unit> irritants;

	private static final float ANGER_SPAWN_MODIFIER = 0.1f;

	public boolean spawnersGuardsInited = false;

	private static final float BIRDY_HIT_IRRITATION = 1;
	private static final float SPAWNER_HIT_IRRITATION = 10;
	private static final float ATTACK_IRRITATION_TRESHOLD = 100;
	private static final float IRRITATION_DECAY = 0.01f;
	Set <Unit> irritantsToRemove = new HashSet <Unit> ();
	@Override
	public void init(final Faction faction) {

		this.faction = faction;

		guardingOrders = new HashMap <Object, PolygonGuardingOrder> ();

		attackingOrders = new HashMap <Unit, AttackingOrder> ();;

		irritants = new TObjectFloatHashMap <Unit> ();


	}


	@Override
	public void yellUnitHit(final Unit unit, final IDamager hitSource)
	{

		Unit source = hitSource.getSource();

		float irritation = irritants.get( source );
		float irritationMod = 0;

		if(unit.getType() == UnitsFactory.SPAWNER)
		{
			irritationMod = SPAWNER_HIT_IRRITATION;

		}
		else
		if(unit.getType() == UnitsFactory.BIDRY)
		{
			irritationMod += BIRDY_HIT_IRRITATION;
		}

		if(irritation == 0 && irritationMod == 0)
			return;


		irritation += irritationMod;

		irritants.adjustOrPutValue( source, irritation, irritation );


	}

	@Override
	public void update(final float delta)
	{
		if(!spawnersGuardsInited)
		{
			pickDefenseTargets();
			spawnersGuardsInited = true;
		}

		irritantsToRemove.clear();
 		for(Unit irritant : irritants.keySet())
		{
			if(!irritant.isAlive())
			{
				irritantsToRemove.add( irritant );
				AttackingOrder order = attackingOrders.remove( irritant );
				if(order != null)
				{
					faction.getScheduler().removeOrder( UnitsFactory.BIDRY, order );
				}
				continue;
			}

			float irritation = irritants.get( irritant );
			if( irritation <= 0 )
			{
				irritantsToRemove.add( irritant );
				irritation = 0;
				continue;
			}

			irritation -= IRRITATION_DECAY * delta;

			irritants.adjustValue( irritant, irritation );

			if(irritation < ATTACK_IRRITATION_TRESHOLD)
			{

				continue;
			}

			if( attackingOrders.containsKey( irritant ) )
			{
				continue;
			}

			if(! attackingOrders.containsKey( irritant ))
			{
				AttackingOrder order = new AttackingOrder( 1, irritant );

				attackingOrders.put( irritant, order );

				faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);
			}
		}

 		for(Unit irritant : irritantsToRemove)
		{
			irritants.remove( irritant );
		}
	}



	private void pickDefenseTargets()
	{
		Set <Asteroid> guardTargets = new HashSet <Asteroid> ();
		for( Unit unit : faction.getUnitsByType( UnitsFactory.SPAWNER ) )
		{
			SurfaceNavNode anchor = (SurfaceNavNode)unit.getAnchor();
			Asteroid asteroid = (Asteroid) anchor.getDescriptor().getObject();

			guardTargets.add( asteroid );

		}
		for(Asteroid asteroid : guardTargets)
		{
			PolygonGuardingOrder order = new PolygonGuardingOrder(1, asteroid.getModel());

			guardingOrders.put(asteroid, order);

			faction.getScheduler().addOrder(UnitsFactory.BIDRY, order);
		}
	}

}
