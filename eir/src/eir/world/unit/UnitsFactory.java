/**
 *
 */
package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pool;

import eir.debug.Debug;
import eir.world.LevelRenderer;
import eir.world.environment.Environment;
import eir.world.environment.nav.NavNode;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.ant.AntFactory;
import eir.world.unit.cannons.CannonFactory;
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
	public static final String CANNON = "cannon".intern();

	private IdentityMap <String, UnitFactory<? extends Unit>> factories = new IdentityMap <String, UnitFactory <? extends Unit>> ();

	private final Environment environment;

	public UnitsFactory(final Environment environment)
	{
		this.environment = environment;

		factories.put( ANT,  new AntFactory() );

		factories.put( BIDRY, new BirdyFactory() );

		factories.put( SPAWNER, new SpawnerFactory() );

		factories.put( BULLET, new BulletFactory() );

		factories.put( SPIDER, new SpiderFactory( this ) );

		factories.put( CANNON, new CannonFactory( environment ) );
	}

	/**
	 * Creates unit of specified type, with position set to the anchor.
	 * @param type
	 * @param anchor
	 * @param faction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit, F extends Faction> U getUnit(final String type, final NavNode anchor, final Faction faction)
	{
		UnitFactory <U> fctory = (UnitFactory <U>) factories.get( type );
		U unit = fctory.pool.obtain();

		unit.init(type, anchor, faction);

		unit.toggleOverlay( LevelRenderer.INTEGRITY_OID);

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
	public <U extends Unit, F extends Faction> U getUnit(final String type, final float x, final float y, final float angle, final Faction faction)
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
	public void free(final Unit unit)
	{
		unit.dispose();

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
		protected Pool <U> pool = new Pool<U> () { @Override
			protected U newObject() { return createEmpty(); } };

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
	public Class<?> getUnitClass(final String unitType)
	{
		return factories.get( unitType ).getUnitClass();
	}


	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit> UnitBehavior<U> getBehavior(final String unitType, final TaskStage stage)
	{
		UnitBehavior<U> behavior = (UnitBehavior<U>) factories.get( unitType ).behaviors.get(stage);
		if(behavior == null)
		{
			Debug.log( "No behaviour for unit type " + unitType + " stage " + stage );
		}
		return behavior;
	}


}