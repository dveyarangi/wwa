/**
 * 
 */
package eir.world;

import eir.resources.PolygonalModel;

/**
 * @author dveyarangi
 *
 */
public class Asteroid
{
	private String id;
	
	private float x;
	private float y;
	private float z;
	
	private float a;
	
	private PolygonalModel model;

	/**
	 * @return
	 */
	public PolygonalModel getModel()
	{
		return model;
	}

	/**
	 * @return
	 */
	public float getAngle()	{ return a;	}
	public float getX()	{ return x;	}
	public float getY()	{ return y;	}
	

}
