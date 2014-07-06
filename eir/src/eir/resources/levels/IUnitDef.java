package eir.resources.levels;

import eir.resources.AnimationHandle;
import eir.resources.TextureHandle;

public interface IUnitDef
{

	TextureHandle getUnitSprite();

	AnimationHandle getDeathAnimation();

	float getSize();

	UnitAnchorDef getAnchorDef();

	int getFactionId();

	String getType();

}
