package eir.world.unit;

import java.util.Arrays;

public class Hull implements Cloneable
{
	private double maxHitPoints;
	private double hitPoints;

	private double [] resistances;
	private double armor;

	public static Hull DUMMY() { return new Hull( 0, 0, new double [] {0,0,0,0} ); }


	public Hull(final double maxHitPoints, final double armor, final double [] resistances)
	{
		this.maxHitPoints = maxHitPoints;
		this.hitPoints = maxHitPoints;
		this.resistances = resistances;
		this.armor = armor;
	}

	public double hit(final Damage damage)
	{

		double totalDamage = 0;
		for(int type : Damage.TYPES)
		{
//			System.out.println(damage);

			double dam = damage.getDamage(type) * (1-resistances[type]);
			if (dam > armor)
			{
				totalDamage += dam - armor;
			}
		}

		hitPoints -= totalDamage;
		if ( this.hitPoints < 0 )
		{
			this.hitPoints = 0;
		}
		return totalDamage;
	}

	public void recover(final double hitPoints)
	{
		this.hitPoints += hitPoints;
		if (this.hitPoints > maxHitPoints )
		{
			this.hitPoints = maxHitPoints;
		}
	}

	public double getMaxHitPoints()
	{
		return maxHitPoints;
	}

	public double getHitPoints() {
		return hitPoints;
	}

	public boolean isBreached() { return hitPoints <= 0; }

	@Override
	public Hull clone()
	{
		return new Hull(maxHitPoints, armor, Arrays.copyOf( resistances, resistances.length ));
	}
}
