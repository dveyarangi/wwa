/**
 * 
 */
package eir.world.unit;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool;

import eir.world.environment.nav.NavNode;
import eir.world.unit.ant.Ant;

import eir.world.unit.structure.Spawner;
import eir.world.unit.wildlings.Birdy;

/**
 * @author dveyarangi
 *
 */
public class UnitsFactory
{
	
	public static final int ANT = 0;
	public static final int ANT_FLYING = 1;
	
	public static final int SPAWNER = 2;

	private static ArrayList <UnitFactory<? extends Unit>> factories = new ArrayList <UnitFactory <? extends Unit>> ();
	
	static {
		factories.add( ANT, new UnitFactory<Ant>() {

			Ant createEmpty() { return new Ant(); }

			Class <Ant>getUnitClass()	{ return Ant.class; }
			
		});
		
		factories.add( ANT_FLYING,new UnitFactory<Birdy>() {

			Birdy createEmpty() { return new Birdy(); }

			Class<Birdy> getUnitClass() { return Birdy.class; }
		});
		
		factories.add( SPAWNER,new UnitFactory<Spawner>() {

			Spawner createEmpty() { return new Spawner(); }

			Class<Spawner> getUnitClass() { return Spawner.class; }
		});
	}
	
	public static <U extends Unit, F extends Faction> U getUnit(int type, NavNode position, Faction faction)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( type );
		U unit = factory.pool.obtain();
		
		unit.init(type, position, faction);
		
		return unit;
	}


	/**
	 * @param ant
	 */
	public static void free(Unit unit)
	{
		UnitFactory <Unit> factory = (UnitFactory <Unit>) factories.get( unit.getType() );
		factory.pool.free( unit );
	}

	
	static abstract class UnitFactory <U>
	{
		Pool <U> pool = new Pool<U> () { protected U newObject() { return createEmpty(); } };
		
		
		abstract U createEmpty();

		abstract Class <U> getUnitClass();
	}


	/**
	 * @param unitType
	 * @return
	 */
	public static Class<?> getUnitClass(int unitType)
	{
		return factories.get( unitType ).getUnitClass();
	}
	
}