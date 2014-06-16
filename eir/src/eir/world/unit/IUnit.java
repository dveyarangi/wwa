package eir.world.unit;

import eir.world.environment.spatial.AABB;

public interface IUnit
{
	/**
	 * Unit collision box and anchor point
	 * @return
	 */
	public AABB getArea();

	/**
	 * Unit orientation
	 * @return
	 */
	public float getAngle();


	public Hull getHull();
}
