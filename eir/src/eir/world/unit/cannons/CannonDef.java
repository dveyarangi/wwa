package eir.world.unit.cannons;

import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;
import eir.world.unit.weapon.WeaponDef;

public class CannonDef extends UnitDef
{
	private WeaponDef weaponDef;

	public CannonDef(final String type, final int faction, final float size,
			final TextureHandle unitSprite, final AnimationHandle deathAnimation, final WeaponDef weaponDef)
	{
		super( type, faction, size, unitSprite, deathAnimation );

		this.weaponDef = weaponDef;
	}

	public WeaponDef getWeaponDef() { return weaponDef; }
}
