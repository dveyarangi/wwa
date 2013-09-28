package eir.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eir.world.Level;

/**
 * controls camera 
 * @author Ni
 *
 */
public class CameraController
{
	public final OrthographicCamera camera;
	
	private final Level level;
	
	private final Vector3 v = new Vector3(0,0,0); // velocity
	private final Vector3 a = new Vector3(0,0,0); // acceleration
	private final float b = 7.5f; // Viscosity coefficient
	private final float maxZoomOut = 2;
	private final float maxZoomIn = 0.1f;
	
	private boolean underUserControl = false;
	
	private Vector2 lastPosition;
	
	public CameraController(int w, int h, Level level)
	{
		
		this.camera = new OrthographicCamera( w, h );
		
		lastPosition = level.getAsteroid(level.getInitialConfig().getAsteroidName())
					.getModel().getNavNode( 
						level.getInitialConfig().getSurfaceIdx()).getPoint().cpy();
		camera.position.x = lastPosition.x;
		camera.position.y = lastPosition.y;
		camera.zoom = level.getInitialConfig().getZoom();
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
		v.add(x*camera.zoom, y*camera.zoom, z*camera.zoom);
	}
	
	/**
	 * will step the camera +call camera.update()
	 * @param delta
	 */
	public void update( float delta )
	{	
		if( !underUserControl )
		{
			if( camera.zoom < maxZoomIn )
			{
				injectLinearImpulse(0, 0, (maxZoomIn - camera.zoom)*100 );
			}
			else if( camera.zoom > maxZoomOut )
			{
				injectLinearImpulse(0, 0, (maxZoomOut - camera.zoom) );
			}
			
			if( camera.position.x < -level.getWidth()/2 )
			{
				injectLinearImpulse( (level.getWidth()/2-camera.position.x), 0, 0);
			}
			else if ( camera.position.x > level.getWidth()/2 )
			{
				injectLinearImpulse( (level.getWidth()/2-camera.position.x*2), 0, 0);
			}
			
			if( camera.position.y<-level.getHeight()/2 )
			{
				injectLinearImpulse( 0, (level.getHeight()/2-camera.position.y), 0);
			}
			else if( camera.position.y>level.getHeight()/2 )
			{
				injectLinearImpulse( 0, (level.getHeight()/2-camera.position.y*2), 0);
			}
		}
		
		a.set( Vector3.tmp.set(0,0,0).sub(v.tmp2().mul(b)) );
		v.add( a.tmp().mul(delta) );
		
		float nextzoom = camera.zoom + v.z*delta + a.z*delta*delta/2;
		float nextx = camera.position.x + v.x*delta + a.x*delta*delta/2;
		float nexty = camera.position.y + v.y*delta + a.y*delta*delta/2;
		
		if( nextzoom < maxZoomOut && camera.zoom > maxZoomOut )
		{
			camera.zoom = maxZoomOut;
			v.z = 0;
			a.z = 0;
		}
		else if ( nextzoom > maxZoomIn && camera.zoom < maxZoomIn || nextzoom<0 )
		{
			camera.zoom = maxZoomIn;
			v.z = 0;
			a.z = 0;
		}
		else
			camera.zoom = nextzoom;
		
		
		if( nextx < level.getWidth()/2 && camera.position.x > level.getWidth()/2 && !underUserControl )
		{
			camera.position.x = level.getWidth()/2;
			v.x = 0;
			a.x = 0;
		}
		else if( nextx > -level.getWidth()/2 && camera.position.x < -level.getWidth()/2 && !underUserControl )
		{
			camera.position.x = -level.getWidth()/2;
			v.x = 0;
			a.x = 0;
		}
		else
			camera.position.x = nextx;
		
		
		if( nexty > -level.getHeight()/2 && camera.position.y < -level.getHeight()/2 && !underUserControl )
		{
			camera.position.y = -level.getHeight()/2;
			v.y = 0;
			a.y = 0;
		}
		else if( nexty < level.getHeight()/2 && camera.position.y > level.getHeight()/2 && !underUserControl )
		{
			camera.position.y = level.getHeight()/2;
			v.y = 0;
			a.y = 0;
		}
		else
			camera.position.y = nexty;
		
		// storing last camera position:
		lastPosition.x = camera.position.x;
		lastPosition.y = camera.position.y;
		
		// calculating matrices:
		camera.update();
	}
	
	public void setUnderUserControl( boolean under )
	{
		underUserControl = under;
		if(under==true)
		{
			v.set(0,0,0);
			a.set(0,0,0);
		}
	}


	/**
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height)
	{
		camera.setToOrtho( false, width, height );
		camera.position.x = lastPosition.x;
		camera.position.y = lastPosition.y;
	}
}
