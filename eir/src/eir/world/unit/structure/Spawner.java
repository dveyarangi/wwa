/**
 *
 */
package eir.world.unit.structure;

import eir.rendering.IRenderer;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.unit.Hull;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Spawner extends Unit
{
	public float timeToSpawn;


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
	public void draw(final IRenderer renderer)
	{
	}

	@Override
	public float getMaxSpeed() { return 0; }


}
