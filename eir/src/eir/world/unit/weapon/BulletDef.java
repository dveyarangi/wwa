package eir.world.unit.weapon;

import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;

public class BulletDef extends UnitDef
{

	public BulletDef(final String type, final int factionId, final int size, final TextureHandle spriteTxr)
	{
		super( type, factionId, size, spriteTxr, null );
	}

}
