/**
 * 
 */
package eir.world;

import com.badlogic.gdx.utils.Pool.Poolable;

import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;

/**
 * @author dveyarangi
 *
 */
public class Bullet implements Poolable, ISpatialObject
{
	
	private AABB body;
	
	

	
	@Override
	public AABB getArea()
	{
		return body;
	}

	/* (non-Javadoc)
	 * @see eir.world.environment.spatial.ISpatialObject#getId()
	 */
	@Override
	public int getId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}

}
