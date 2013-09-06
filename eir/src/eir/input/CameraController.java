package eir.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * controls camera 
 * @author Ni
 *
 */
public class CameraController
{
	public float maxZoomOut = 0.1f;
	public float maxZoomIn = 100;
	public final OrthographicCamera camera;
	
	private Vector3 f = new Vector3(0,0,0); // force
	private Vector3 v = new Vector3(0,0,0); // velocity
	private Vector3 a = new Vector3(0,0,0); // acceleration
	private float b = 15; // Viscosity coefficient
	
	public CameraController( OrthographicCamera camera )
	{
		this.camera = camera;
		maxZoomIn = 0.1f;
		maxZoomOut = 100;
	}
	
	
	public void injectImpulse( float x, float y, float z )
	{
		f.add(x*100*camera.zoom, y*100*camera.zoom, z*100*camera.zoom);
	}
	
	/**
	 * will step the camera +call cam.update()
	 * @param delta
	 */
	public void cameraStep( float delta )
	{
		if( camera.zoom < maxZoomIn )
		{
			injectImpulse(0, 0, maxZoomIn);
			camera.zoom = maxZoomIn;
		}
		else if (camera.zoom > maxZoomOut )
		{
			a.set(0, 0, 0);
			v.set(0, 0, 0);
			camera.zoom = maxZoomOut;
		}
		
		a.set( f.tmp().sub(v.tmp2().mul(b)) );
		v.add( a.tmp().mul(delta) );
		f.set(0, 0, 0);
		
		camera.zoom += v.z*delta + a.z*delta*delta/2;
		camera.position.x += v.x*delta + a.x*delta*delta/2;
		camera.position.y += v.y*delta + a.y*delta*delta/2;
		
		camera.update();
	}
}
