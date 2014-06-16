package eir.world.unit;

import java.util.Arrays;

public class Hull implements Cloneable
{
	private float maxHitPoints;

	private float [] resistances;
	private float armor;

	private float hitPoints;

	public static Hull DUMMY() { return new Hull( 0, 0, new float [] {0,0,0,0} ); }


	public Hull(final float maxHitPoints, final float armor, final float [] resistances)
	{
		this.maxHitPoints = maxHitPoints;
		this.hitPoints = maxHitPoints;
		this.resistances = resistances;
		this.armor = armor;
	}

	public float hit(final Damage damage, final float damageCoef)
	{

		float totalDamage = 0;
		for(int type : Damage.TYPES)
		{
//			System.out.println(damage);

			double dam = damage.getDamage(type) * (1-resistances[type]);
			if (dam > armor)
			{
				totalDamage += (dam - armor) * damageCoef;
			}
		}

		hitPoints -= totalDamage;
		if ( this.hitPoints < 0 )
		{
			this.hitPoints = 0;
		}
		return totalDamage;
	}

	public void recover(final float hitPoints)
	{
		this.hitPoints += hitPoints;
		if (this.hitPoints > maxHitPoints )
		{
			this.hitPoints = maxHitPoints;
		}
	}

	public float getMaxHitPoints()
	{
		return maxHitPoints;
	}

	public float getHitPoints() {
		return hitPoints;
	}

	public boolean isBreached() { return hitPoints <= 0; }

	@Override
	public Hull clone()
	{
		return new Hull(maxHitPoints, armor, Arrays.copyOf( resistances, resistances.length ));
	}
}
