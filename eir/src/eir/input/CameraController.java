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
	public float maxZoomOut;
	public float maxZoomIn;
	public final OrthographicCamera camera;
	
	private Vector3 f = new Vector3(0,0,0); // force
	private Vector3 v = new Vector3(0,0,0); // velocity
	private Vector3 a = new Vector3(0,0,0); // acceleration
	
	private float b; // Viscosity coefficient
	
	public CameraController( OrthographicCamera camera )
	{
		this.camera = camera;
		maxZoomIn = 0.1f;
		maxZoomOut = 100;
//		zoomF = 0;
//		zoomV = 0;
//		zoomAcc = 0;
		b = 10;
		//b = 1/camera.zoom;
	}
	
	
	public void injectImpulse( float x, float y, float z )
	{
//		zoomF += z*camera.zoom*100;
		f.z = z*100*camera.zoom;
		//f.add(x, y, z*100);
	}
	
	/**
	 * will step the camera +call cam.update()
	 * @param delta
	 */
	public void cameraStep( float delta )
	{
		if( camera.zoom < maxZoomIn )
		{
			a.set(0, 0, 0);
			v.set(0, 0, 0);
			camera.zoom = maxZoomIn;
		}
		else if (camera.zoom > maxZoomOut )
		{
			a.set(0, 0, 0);
			v.set(0, 0, 0);
			camera.zoom = maxZoomOut;
		}
		
		a.z = f.z - b*v.z;
		v.z += a.z * delta;
//		a.set( f.sub(v.mul(b)) );
//		v.add( a.mul(delta) );
		f.set(0, 0, 0);
		camera.zoom += v.z*delta + a.z*delta*delta/2;
		
		
//		zoomAcc = zoomF - b*zoomV;
//		zoomV   = zoomV + zoomAcc*delta;
//		zoomF = 0;
		
//		zoomAcc = Math.abs(zoomAcc)<0.0000001 ? 0 : zoomF - b*zoomV;
//		zoomV   = Math.abs(zoomAcc)<0.0000001 ? 0 : zoomV + zoomAcc*delta;
		
		//camera.zoom += amount*0.1*camera.zoom;
		//camera.position.x -= amount*camera.zoom*0.1*(lastx - camera.viewportWidth/2);
		//camera.position.y += amount*camera.zoom*0.1*(lasty - camera.viewportHeight/2);
		
		camera.update();
	}
}
