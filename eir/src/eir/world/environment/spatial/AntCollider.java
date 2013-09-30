/**
 * 
 */
package eir.world.environment.spatial;

import eir.world.unit.Ant;
import eir.world.unit.Bullet;

/**
 * @author dveyarangi
 *
 */
public class AntCollider implements ISpatialSensor<ISpatialObject>
{
	
	private Ant collidingAnt;
	
	public void setAnt(Ant collidingAnt)
	{
		this.collidingAnt = collidingAnt;
	}

	@Override
	public boolean objectFound(ISpatialObject object)
	{
		if(object instanceof Ant)
		{
			Ant ant = (Ant) object;
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
