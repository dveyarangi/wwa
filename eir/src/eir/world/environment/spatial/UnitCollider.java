/**
 *
 */
package eir.world.environment.spatial;

import java.util.ArrayList;
import java.util.List;

import eir.world.unit.IDamager;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class UnitCollider implements ISpatialSensor<ISpatialObject>
{

	private Unit collidingAnt;

	private List <IDamager> aoeDamages = new ArrayList <IDamager> ();

	public void setAnt(final Unit collidingAnt)
	{
		this.collidingAnt = collidingAnt;
	}

	@Override
	public boolean objectFound(final ISpatialObject object)
	{
		if(! (object instanceof Unit))
			return false;


		Unit ant = (Unit) object;
		boolean sameFaction = ant.getFaction().getOwnerId() == collidingAnt.getFaction().getOwnerId();
		boolean damageDealt = false;
		if(object instanceof IDamager)
		{
			IDamager damager = (IDamager) object;

			if( !sameFaction || damager.dealsFriendlyDamage() )
			{
				if( damager.getDamage().getAOE() != null )
				{
					aoeDamages.add( damager );

				}
				else
				{
					collidingAnt.hit( damager.getDamage(), damager, 1 );

					damageDealt = true;
				}
			}
		}

		if(collidingAnt instanceof IDamager)
		{
			IDamager source = (IDamager)collidingAnt;

			if( !sameFaction || collidingAnt.dealsFriendlyDamage() )
			{
				if( source.getDamage().getAOE() != null )
				{
					aoeDamages.add( source );
				}
				else
				{
					ant.hit( source.getDamage(), source, 1 );

					damageDealt = true;
				}
			}
		}

		return damageDealt;
	}

	@Override
	public void clear()
	{
		aoeDamages.clear();
		collidingAnt = null;
	}

	public List <IDamager> getAOEDamage() { return aoeDamages; }

}
