package eir.world.environment;

import com.badlogic.gdx.math.Vector2;

import eir.world.unit.Unit;

public class RelativeAnchor implements Anchor
{

	private Unit unit;

	public RelativeAnchor(final Unit unit)
	{
		this.unit = unit;
	}

	@Override
	public Vector2 getPoint()
	{
		return unit.getArea().getAnchor();
	}

	@Override
	public float getAngle()
	{
		return unit.getAngle();
	}

	@Override
	public Object getParent() { return unit; }

}
