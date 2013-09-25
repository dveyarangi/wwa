package eir.world.environment;

import com.badlogic.gdx.utils.Pool;


/**
 * route class for CompositeFloydWarshal.
 * @author Ni
 *
 */
public class CompositeFloydWarshalRoute extends Route
{
	public static final Pool<CompositeFloydWarshalRoute> routesPool =
			new Pool<CompositeFloydWarshalRoute>()
			{
				@Override
				protected CompositeFloydWarshalRoute newObject()
				{
					return new CompositeFloydWarshalRoute();
				}
			};
	
	@Override
	public boolean hasNext()
	{
		return false;
	}

	@Override
	public NavNode next()
	{
		return null;
	}

	@Override
	public void reset()
	{
		
	}

	@Override
	public void recycle()
	{
		routesPool.free(this);
	}

}
