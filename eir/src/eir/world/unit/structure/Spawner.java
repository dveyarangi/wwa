/**
 *
 */
package eir.world.unit.structure;

import eir.world.IRenderer;
import eir.world.unit.Hull;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Spawner extends Unit
{
	public float spawnInterval;

	public float timeToSpawn;

	/**
	 * Max units to spawn
	 */
	private int maxUnits = 1;

	private String unitType;

	private float size = 10;

	@Override
	protected void init()
	{
		super.init();

		unitType = unitType.intern();

		this.hull = new Hull(10000f, 0f, new float [] {0f,0f,0f,0f});


	}

	@Override
	public void update( final float delta )
	{
		super.update( delta );
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{

			if(faction.units.size() < maxUnits)
			{
				faction.createUnit( unitType, anchor );
			}

			timeToSpawn = spawnInterval;
		}
	}


	@Override
	public void draw(final IRenderer renderer)
	{
	}

	@Override
	public float getSize() { return size; }

	@Override
	public float getMaxSpeed() { return 0; }
}
