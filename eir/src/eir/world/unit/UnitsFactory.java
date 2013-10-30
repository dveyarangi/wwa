/**
 * 
 */
package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pool;

import eir.world.environment.nav.NavNode;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.ant.AntFactory;
import eir.world.unit.spider.SpiderFactory;
import eir.world.unit.structure.SpawnerFactory;
import eir.world.unit.weapon.BulletFactory;
import eir.world.unit.wildlings.BirdyFactory;

/**
 * Manages unit instantiation and pooling.
 * 
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
	
	/**
	 * Creates unit of specified type, with position set to the anchor.
	 * @param type
	 * @param anchor
	 * @param faction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <U extends Unit, F extends Faction> U getUnit(String type, NavNode anchor, Faction faction)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( type );
		U unit = factory.pool.obtain();

		unit.init(type, anchor, faction);
		
		return unit;
	}
	
	/**
	 * Creates unit of specified type, with position set at (x,y) and null anchor.
	 * @param type
	 * @param anchor
	 * @param faction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <U extends Unit, F extends Faction> U getUnit(String type, float x, float y, float angle, Faction faction)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( type );
		U unit = factory.pool.obtain();
		
		unit.init(type, x, y, angle, faction);
		
		unit.angle = angle;
		
		return unit;
	}


	/**
	 * Returns unit to pool.
	 * 
	 * @param ant
	 */
	@SuppressWarnings("unchecked")
	public static void free(Unit unit)
	{
		UnitFactory <Unit> factory = (UnitFactory <Unit>) factories.get( unit.getType() );
		factory.pool.free( unit );
	}

	/**
	 * Implementation of specific unit factory should add {@link TaskStage} behaviors by calling 
	 * {@link BehaviorFactory#registerBehavior(eir.world.unit.ai.TaskStage, UnitBehavior)}
	 * @author Fima
	 *
	 * @param <U>
	 */
	public static abstract class UnitFactory <U extends Unit>
	{
		/**
		 * Pool of units of this type
		 */
		protected Pool <U> pool = new Pool<U> () { protected U newObject() { return createEmpty(); } };
		
		/**
		 * TaskStage -> TaskBehavior mapping
		 */
		protected Map <TaskStage, UnitBehavior <U>> behaviors = new HashMap <TaskStage, UnitBehavior<U>> ();
		
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
	@SuppressWarnings("unchecked")
	public static <U extends Unit> UnitBehavior<U> getBehavior(String unitType, TaskStage stage)
	{
		return (UnitBehavior<U>) factories.get( unitType ).behaviors.get(stage);
	}
	
}