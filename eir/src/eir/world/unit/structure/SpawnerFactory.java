/**
 * 
 */
package eir.world.unit.structure;

import eir.world.unit.UnitsFactory.UnitFactory;

/**
 * @author dveyarangi
 *
 */
public class SpawnerFactory extends UnitFactory <Spawner>
{

	protected Spawner createEmpty() { return new Spawner(); }

	protected Class<Spawner> getUnitClass() { return Spawner.class; }
}