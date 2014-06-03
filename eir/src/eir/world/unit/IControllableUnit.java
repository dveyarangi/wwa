package eir.world.unit;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.AABB;

public interface IControllableUnit
{
	public void setShootingTarget(final Vector2 targetPos);
	public void walkDown(final boolean b);
	public void walkCCW(final boolean b);
	public void walkCW(final boolean b);
	public void walkUp(final boolean b);
	public AABB getBody();

}
