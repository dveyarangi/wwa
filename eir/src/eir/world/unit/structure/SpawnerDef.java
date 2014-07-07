package eir.world.unit.structure;

import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;
import eir.resources.levels.UnitDef;

public class SpawnerDef extends UnitDef
{
	private UnitDef spawnedUnit;
	private int maxUnits;
	private float spawnInterval;

	public SpawnerDef(final String type, final int factionId, final float size, final TextureHandle unitSprite,
			final AnimationHandle deathAnimation)
	{
		super( type, factionId, size, unitSprite, deathAnimation );
	}

	public UnitDef getSpawnedUnit() { return spawnedUnit; }

	public int getMaxUnits() { return maxUnits; }

	public float getSpawnInterval() { return spawnInterval;	}


}
