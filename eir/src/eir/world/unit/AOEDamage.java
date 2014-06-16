package eir.world.unit;

import eir.world.IRenderer;

public class AOEDamage extends Unit
{

	private float radius;

	public AOEDamage(final float radius)
	{
		this.radius = radius;
	}

	@Override
	public void draw( final IRenderer renderer )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getSize() { return radius; }

	@Override
	public float getMaxSpeed() { return 0; }

}
