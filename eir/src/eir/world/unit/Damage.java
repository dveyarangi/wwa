package eir.world.unit;

/**
 * Damage descriptor
 *
 * @author dveyarangi
 */
public class Damage
{
	public static final int KINETIC = 0;
	public static final int THERMAL = 1;
	public static final int ELECTRO_MAGNETIC = 2;
	public static final int VOID = 3;

	public static final int [] TYPES = {KINETIC, THERMAL, ELECTRO_MAGNETIC, VOID };

	private final double [] damage = new double [4];

	public Damage(final double kinetic, final double thermal, final double divine, final double voidd)
	{
		damage[KINETIC] = kinetic;
		damage[THERMAL] = thermal;
		damage[ELECTRO_MAGNETIC] = divine;
		damage[VOID] = voidd;
	}

	public double getDamage(final int type)
	{
		return damage[type];
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append( "damage: ")
			.append(damage[KINETIC]).append(",")
			.append(damage[THERMAL]).append(",")
			.append(damage[ELECTRO_MAGNETIC]).append(",")
			.append(damage[VOID])
			.toString();
	}
}
