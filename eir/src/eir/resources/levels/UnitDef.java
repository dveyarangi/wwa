package eir.resources.levels;

import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;



public class UnitDef implements IUnitDef
{

	private String type;

	private float size;

	private int faction;

	private UnitAnchorDef anchor;

	private TextureHandle spriteTexture;

	private AnimationHandle deathAnimation;

	private UnitDef() {}

	public UnitDef(final String type, final int faction, final float size, final TextureHandle unitSprite, final AnimationHandle deathAnimation)
	{
		this.type = type;
		this.size = size;
		this.faction = faction;

		this.spriteTexture = unitSprite;
		this.deathAnimation = deathAnimation;
	}

	@Override
	public String getType() { return type; }

	@Override
	public int getFactionId() { return faction; }

	@Override
	public float getSize() { return size; }

	@Override
	public AnimationHandle getDeathAnimation() { return deathAnimation; }

	@Override
	public UnitAnchorDef getAnchorDef() { return anchor; }

	@Override
	public TextureHandle getUnitSprite() { return spriteTexture; }
}
