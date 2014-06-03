/**
 *
 */
package eir.world.unit.structure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	}

	@Override
	public void update( final float delta )
	{
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
	public void draw(final SpriteBatch batch)
	{
	}

	@Override
	public float getSize() { return size; }

	@Override
	public float getMaxSpeed() { return 0; }
}
