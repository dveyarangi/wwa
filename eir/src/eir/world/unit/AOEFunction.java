package eir.world.unit;

import com.badlogic.gdx.math.Vector2;

public interface AOEFunction
{
	public static AOEFunction QUADRITIC_DECAY = new AOEFunction()
	{

		@Override
		public float getDamageReduction( final Vector2 damager, final Vector2 target )
		{
			return 1f/ target.dst2( damager );
		}

	};
	public static AOEFunction LINEAR_DECAY = new AOEFunction()
	{

		@Override
		public float getDamageReduction( final Vector2 damager, final Vector2 target )
		{
			return 1f/ target.dst( damager );
		}

	};


	public float getDamageReduction(Vector2 damager, Vector2 target);


}
