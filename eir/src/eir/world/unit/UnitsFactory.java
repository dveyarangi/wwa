/**
 *
 */
package eir.world.unit;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pool;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.resources.levels.IUnitDef;
import eir.resources.levels.UnitDef;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.unit.ai.TaskStage;
import eir.world.unit.ant.AntFactory;
import eir.world.unit.cannons.CannonFactory;
import eir.world.unit.structure.SpawnerFactory;
import eir.world.unit.weapon.BulletFactory;
import eir.world.unit.weapon.WeaponFactory;
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
//	public static final String SPIDER = "spider".intern();
	public static final String CANNON = "cannon".intern();
	public static final String WEAPON = "weapon".intern();

	private IdentityMap <String, UnitFactory<? extends Unit>> factories = new IdentityMap <String, UnitFactory <? extends Unit>> ();

	public UnitsFactory(final GameFactory gameFactory)
	{

		factories.put( ANT,  new AntFactory( gameFactory ) );

		factories.put( BIDRY, new BirdyFactory() );

		factories.put( SPAWNER, new SpawnerFactory() );

		factories.put( BULLET, new BulletFactory() );

//		factories.put( SPIDER, new SpiderFactory( this ) );

		factories.put( CANNON, new CannonFactory() );

		factories.put( WEAPON, new WeaponFactory() );
	}

	/**
	 * Creates unit of specified type, with position set to the anchor.
	 * @param type
	 * @param anchor
	 * @param faction
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U extends Unit, F extends Faction> U getUnit(final GameFactory gameFactory, final Level level, final IUnitDef def, final Anchor anchor)
	{
		UnitFactory <U> fctory = (UnitFactory <U>) factories.get( def.getType() );
		U unit = fctory.pool.obtain();

		unit.init(gameFactory, level, def, anchor);


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
	public <U extends Unit, F extends Faction> U getUnit(final GameFactory gameFactory, final Level level, final IUnitDef def, final float x, final float y, final float angle)
	{
		UnitFactory <U> factory = (UnitFactory <U>) factories.get( def.getType() );
		U unit = factory.pool.obtain();

		unit.init(gameFactory, level, def, x, y, angle);

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

			/**
			 * Allows mapping extended unit definitions in the level file.
			 * Unit initialization procedure will rely on this type
			 * @return
			 */
			protected Class <? extends UnitDef> getDefClass()
			{
				return UnitDef.class;
			}
	}


	/**
	 * @param unitType
	 * @return
	 */
	public Class<?> getUnitDefClass(final String unitType)
	{
		return factories.get( unitType ).getDefClass();
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