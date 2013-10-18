/**
 * 
 */
package eir.world.environment.spatial;

import eir.world.unit.Bullet;
import eir.world.unit.Unit;
import eir.world.unit.ant.Ant;

/**
 * @author dveyarangi
 *
 */
public class UnitCollider implements ISpatialSensor<ISpatialObject>
{
	
	private Unit collidingAnt;
	
	public void setAnt(Unit collidingAnt)
	{
		this.collidingAnt = collidingAnt;
	}

	@Override
	public boolean objectFound(ISpatialObject object)
	{
		if(object instanceof Unit)
		{
			Unit ant = (Unit) object;
			if(ant.getFaction().getOwnerId() != collidingAnt.getFaction().getOwnerId())
			{
				collidingAnt.hit(ant.getDamage());
				ant.hit(collidingAnt.getDamage());
				return true;
			}
		}
		if(object instanceof Bullet)
		{
			Bullet bullet = (Bullet) object;
			if(bullet.weapon.getOwnerId() != collidingAnt.getFaction().getOwnerId())
				collidingAnt.hit(bullet.getDamage());
			
			return true;
		}
		
		return false;
	}

	@Override
	public void clear()
	{
		collidingAnt = null;
	}

}
