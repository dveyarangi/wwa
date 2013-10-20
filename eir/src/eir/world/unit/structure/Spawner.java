/**
 * 
 */
package eir.world.unit.structure;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eir.world.unit.ai.GuardingOrder;
import eir.world.environment.spatial.AABB;
import eir.world.unit.Damage;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

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
		
		getBody().copyFrom( AABB.createSquare( position.getPoint().x, position.getPoint().y, this.size/2 ) );
	
		faction.getScheduler().addOrder( "birdy", new GuardingOrder( 0, position ) );

	}
	
	public void update( float delta )
	{
		timeToSpawn -= delta;
		if(timeToSpawn < 0)
		{
			
			if(faction.units.size() < maxUnits)
				faction.getLevel().addUnit( 
						UnitsFactory.getUnit( unitType, position, faction ) );
			
			timeToSpawn = spawnInterval;
		}
	}

	public void hit(Damage damage)
	{
		return; // spawner is unbreakable for now
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getSize() { return size; }
}
