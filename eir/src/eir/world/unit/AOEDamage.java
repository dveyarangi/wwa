package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AOEDamage extends Unit
{

	private float radius;

	public AOEDamage(final float radius)
	{
		this.radius = radius;
	}

	@Override
	public void draw( final SpriteBatch batch )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getSize() { return radius; }

	@Override
	public float getMaxSpeed() { return 0; }

}
