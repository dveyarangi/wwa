/**
 * 
 */
package eir.world.unit.structure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.world.unit.Damage;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.ai.GuardingOrder;

/**
 * @author dveyarangi
 *
 */
public class Spawner extends Unit
{
	private float spawnInterval;
	
	private float timeToSpawn;
	
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
	
		faction.getScheduler().addOrder( "birdy", new GuardingOrder( 0, anchor ) );

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

	public void hit(Damage damage)
	{
		return; // spawner is unbreakable for now
	}

	@Override
	public void draw(SpriteBatch batch, ShapeRenderer shape)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getSize() { return size; }
}
