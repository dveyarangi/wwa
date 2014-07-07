/**
 *
 */
package eir.world.unit.structure;

import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Spawner extends Unit implements IDamager
{
	public float timeToSpawn;

	public  Damage damage = new Damage(100, 100, 100, 100);


	@Override
	protected void reset( final GameFactory gameFactory, final Level level )
	{
		super.reset( gameFactory, level );

		this.hull = new Hull(10000f, 0f, new float [] {0f,0f,0f,0f});
	}

	@Override
	public void update( final float delta )
	{
		super.update( delta );
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{
			SpawnerDef sdef = (SpawnerDef) def;

			if(faction.units.size() < sdef.getMaxUnits())
			{
				faction.createUnit( sdef.getSpawnedUnit(), anchor );
			}

			timeToSpawn = sdef.getSpawnInterval();
		}
	}



	@Override
	public float getMaxSpeed() { return 0; }

	@Override
	public Damage getDamage() { return damage; }

	@Override
	public Unit getSource()	{ return this; }


}
