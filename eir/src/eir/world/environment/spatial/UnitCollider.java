/**
 *
 */
package eir.world.environment.spatial;

import eir.world.unit.IDamager;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class UnitCollider implements ISpatialSensor<ISpatialObject>
{

	private Unit collidingAnt;

	public void setAnt(final Unit collidingAnt)
	{
		this.collidingAnt = collidingAnt;
	}

	@Override
	public boolean objectFound(final ISpatialObject object)
	{
		if(object instanceof IDamager)
		{
			IDamager ant = (IDamager) object;
			if(ant.getFaction().getOwnerId() != collidingAnt.getFaction().getOwnerId())
			{
				collidingAnt.hit( ant.getDamage(), ant );

				if(collidingAnt instanceof IDamager && ant instanceof Unit)
				{
					IDamager source = ant;
					((Unit)ant).hit( source.getDamage(), source );
				}
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
