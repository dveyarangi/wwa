/**
 * 
 */
package eir.world.environment.spatial;

import eir.world.unit.Unit;

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
				collidingAnt.hit( ant );
				ant.hit( collidingAnt );
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void clear()
	{
		collidingAnt = null;
	}

}
