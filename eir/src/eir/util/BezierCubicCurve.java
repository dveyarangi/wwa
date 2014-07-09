package eir.util;

import com.badlogic.gdx.math.Vector2;

public class BezierCubicCurve implements ParametricCurve
{
	private final Vector2 p1, p2, p3, p4;

	private final Vector2 at1 = Vector2.Zero.cpy();
	private final Vector2 at2 = Vector2.Zero.cpy();
	private final Vector2 at3 = Vector2.Zero.cpy();
	private final Vector2 at4 = Vector2.Zero.cpy();

	public BezierCubicCurve(final Vector2 p1, final Vector2 p2, final Vector2 p3, final Vector2 p4)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}


	@Override
	public Vector2 at(final Vector2 target, final float t)
	{
		float f = 1 - t;

		return at1.set(p1).mul(f*f*f).add(
			   at2.set(p2).mul(3*f*f*t)).add(
			   at3.set(p3).mul(3*f*t*t)).add(
			   at4.set(p4).mul(t*t*t));
	}

	public Vector2 p1() { return p1; }
	public Vector2 p2() { return p2; }
	public Vector2 p3() { return p3; }
	public Vector2 p4() { return p4; }
}
