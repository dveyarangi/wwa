package eir.input;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * controls camera 
 * @author Ni
 *
 */
public class CameraController
{
	public float maxZoomOut;
	public final OrthographicCamera camera;
	
	private float zoomF;
	private float zoomV;
	private float zoomAcc;
	private float b; // Viscosity coefficient
	
	public CameraController( OrthographicCamera camera )
	{
		this.camera = camera;
		zoomF = 0;
		zoomV = 0;
		zoomAcc = 0;
		b = 10;
	}
	
	
	public void injectImpulse( float x, float y, float z )
	{
		zoomF += z*camera.zoom*100;
	}
	
	/**
	 * will step the camera +call cam.update()
	 * @param delta
	 */
	public void cameraStep( float delta )
	{
		System.out.println( "F: "+ zoomF + " acc: "+zoomAcc +" v: "+zoomV+" zoom: "+camera.zoom);
		camera.zoom += zoomV*delta + zoomAcc*delta*delta/2;
		zoomAcc = zoomF - b*zoomV;
		zoomV   = zoomV + zoomAcc*delta;
		b = camera.zoom;
		zoomF = 0;
		
//		zoomAcc = Math.abs(zoomAcc)<0.0000001 ? 0 : zoomF - b*zoomV;
//		zoomV   = Math.abs(zoomAcc)<0.0000001 ? 0 : zoomV + zoomAcc*delta;
		
		//camera.zoom += amount*0.1*camera.zoom;
		//camera.position.x -= amount*camera.zoom*0.1*(lastx - camera.viewportWidth/2);
		//camera.position.y += amount*camera.zoom*0.1*(lasty - camera.viewportHeight/2);
		
		camera.update();
	}
}
