/**
 * 
 */
package eir.world.unit.structure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

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
	
	public void init()
	{
		super.init();
		
		unitType = unitType.intern();

	}
	
	public void update( float delta )
	{
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{
			
			if(faction.units.size() < maxUnits)
				faction.getLevel().addUnit( 
						UnitsFactory.getUnit( unitType, anchor, faction ) );
			
			timeToSpawn = spawnInterval;
		}
	}

	public void hit(Unit source)
	{
		faction.getController().yellUnitHit( this, source );
		return; // TODO: spawner is unbreakable for now
	}

	@Override
	public void draw(SpriteBatch batch)
	{
	}

	@Override
	public float getSize() { return size; }
}
