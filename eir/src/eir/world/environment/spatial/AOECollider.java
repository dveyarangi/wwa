package eir.world.environment.spatial;

import eir.world.unit.IDamager;
import eir.world.unit.Unit;

public class AOECollider implements ISpatialSensor<ISpatialObject>
{
	private IDamager damager;

	public void setDamager(final IDamager damager)
	{
		this.damager = damager;
	}

	@Override
	public boolean objectFound( final ISpatialObject object )
	{
		if( !( object instanceof Unit) )
			return false;

		Unit target = (Unit) object;

		boolean sameFaction = target.getFaction().getOwnerId() == damager.getFaction().getOwnerId();

		if( !sameFaction || damager.dealsFriendlyDamage() )
		{
			float damageCoef = damager.getDamage().getAOE().getDamageReduction(
					damager.getArea().getAnchor(),
					target.getArea().getAnchor()
					);


			target.hit( damager.getDamage(), damager, damageCoef );

		}
		return false;
	}

	@Override
	public void clear()
	{
	}

}
