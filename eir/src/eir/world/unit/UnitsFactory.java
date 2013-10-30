/**
 * 
 */
package eir.world.unit;

import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pool;

import eir.world.environment.nav.NavNode;
import eir.world.unit.ant.AntFactory;
import eir.world.unit.spider.SpiderFactory;
import eir.world.unit.structure.SpawnerFactory;
import eir.world.unit.weapon.BulletFactory;
import eir.world.unit.wildlings.BirdyFactory;

/**
 * @author dveyarangi
 *
 */
public class UnitsFactory
{
	
	public static final String ANT = "ant".intern();
	public static final String BIDRY = "birdy".intern();
	
	public static final String SPAWNER = "spawner".intern();
	public static final String BULLET = "bullet".intern();
	public static final String SPIDER = "spider".intern();

	private static IdentityMap <String, UnitFactory<? extends Unit>> factories = new IdentityMap <String, UnitFactory <? extends Unit>> ();
	
	static {
		factories.put( ANT,  new AntFactory() );
		
		factories.put( BIDRY, new BirdyFactory() );
		
		factories.put( SPAWNER, new SpawnerFactory() );

		factories.put( BULLET, new BulletFactory() );

		factories.put( SPIDER, new SpiderFactory() );

	}
	
	public static <U extends Unit, F extends Faction> U getUnit(String type, NavNode position, Faction faction)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( type );
		U unit = factory.pool.obtain();
		
		unit.init(type, position, faction);
		
		return unit;
	}
	
	public static <U extends Unit, F extends Faction> U getUnit(String type, float x, float y, float angle, Faction faction)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( type );
		U unit = factory.pool.obtain();
		
		unit.init(type, x, y, angle, faction);
		
		unit.angle = angle;
		
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

	
	public static abstract class UnitFactory <U extends Unit>
	{
		protected Pool <U> pool = new Pool<U> () { protected U newObject() { return createEmpty(); } };
		
		protected BehaviorFactory <U> behaviors = new BehaviorFactory <U> ();
		
		protected abstract U createEmpty();

		protected abstract Class <U> getUnitClass();
	}


	/**
	 * @param unitType
	 * @return
	 */
	public static Class<?> getUnitClass(String unitType)
	{
		return factories.get( unitType ).getUnitClass();
	}


	/**
	 * @return
	 */
	public static <U extends Unit> BehaviorFactory<U> getBehaviorFactory(String unitType)
	{
		return (BehaviorFactory<U>) factories.get( unitType ).behaviors;
	}
	
}