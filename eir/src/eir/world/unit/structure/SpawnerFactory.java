/**
 *
 */
package eir.world.unit.structure;

import eir.resources.levels.UnitDef;
import eir.world.unit.UnitsFactory.UnitFactory;

/**
 * @author dveyarangi
 *
 */
public class SpawnerFactory extends UnitFactory <Spawner>
{

	@Override
	protected Spawner createEmpty() { return new Spawner(); }

	protected Class<Spawner> getUnitClass() { return Spawner.class; }

	@Override
	protected Class <? extends UnitDef> getDefClass() { return SpawnerDef.class; }

}