package eir.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import eir.resources.Level;

/**
 * controls camera 
 * @author Ni
 *
 */
public class CameraController
{
	public final OrthographicCamera camera;
	
	private final Level level;
	
	// linear
	private final Vector3 f = new Vector3(0,0,0); // force
	private final Vector3 v = new Vector3(0,0,0); // velocity
	private final Vector3 a = new Vector3(0,0,0); // acceleration
	private final float b = 15; // Viscosity coefficient
	private final float maxZoomOut = 2;
	private final float maxZoomIn = 0.1f;
	
	private boolean underUserControl = false;
	private boolean wasOutOfXBounds = false;
	private boolean wasOutOfYBounds = false;
	private boolean wasOutOfZoomOutBounds = false;
	private boolean wasOutOfZoomInBounds = false;
	
	public CameraController( OrthographicCamera camera, Level level )
	{
		this.camera = camera;
		this.level = level;
	}
	
	
	/**
	 * inject linear impulse for camera 
	 * @param x left/right
	 * @param y up/down
	 * @param z zoom in/out
	 */
	public void injectLinearImpulse( float x, float y, float z )
	{
		f.add(x*100*camera.zoom, y*100*camera.zoom, z*100*camera.zoom);
	}
	
	/**
	 * will step the camera +call camera.update()
	 * @param delta
	 */
	public void cameraStep( float delta )
	{
		if( !underUserControl )
		{
			if( camera.zoom < maxZoomIn )
			{
				wasOutOfZoomInBounds = true;
				injectLinearImpulse(0, 0, (maxZoomIn - camera.zoom)*10 );
			}
			else if ( wasOutOfZoomInBounds )
			{
				wasOutOfZoomInBounds = false;
				f.z = 0;
				v.z = 0;
				a.z = 0;
			}
			
			if (camera.zoom > maxZoomOut )
			{
				wasOutOfZoomOutBounds = true;
				injectLinearImpulse(0, 0, (maxZoomOut - camera.zoom) );
			}
			else if ( wasOutOfZoomOutBounds )
			{
				wasOutOfZoomOutBounds = false;
				f.z = 0;
				v.z = 0;
				a.z = 0;
			}
			
			if( camera.position.x<-level.getWidth()/2 )
			{
				wasOutOfXBounds = true;
				injectLinearImpulse( (level.getWidth()/2-camera.position.x), 0, 0);
			}
			else if (camera.position.x> level.getWidth()/2)
			{
				wasOutOfXBounds = true;
				injectLinearImpulse( (level.getWidth()/2-camera.position.x*2), 0, 0);
			}
			else if ( wasOutOfXBounds )
			{
				wasOutOfXBounds = false;
				f.x = 0;
				v.x = 0;
				a.x = 0;
			}
			
			if( camera.position.y<-level.getHeight()/2 )
			{
				wasOutOfYBounds = true;
				injectLinearImpulse( 0, (level.getHeight()/2-camera.position.y), 0);
			}
			else if(camera.position.y>level.getHeight()/2)
			{
				wasOutOfYBounds = true;
				injectLinearImpulse( 0, (level.getHeight()/2-camera.position.y*2), 0);
			}
			else if ( wasOutOfYBounds )
			{
				wasOutOfYBounds = false;
				f.y = 0;
				v.y = 0;
				a.y = 0;
			}
		}
		
		a.set( f.tmp().sub(v.tmp2().mul(b)) );
		v.add( a.tmp().mul(delta) );
		f.set(0, 0, 0);
		
		camera.zoom += v.z*delta + a.z*delta*delta/2;
		camera.position.x += v.x*delta + a.x*delta*delta/2;
		camera.position.y += v.y*delta + a.y*delta*delta/2;
		
		camera.update();
	}
	
	public void setUnderUserControl( boolean under )
	{
		underUserControl = under;
	}
}
