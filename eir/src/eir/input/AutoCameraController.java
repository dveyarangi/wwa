/**
 * 
 */
package eir.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eir.world.Level;

/**
 * @author dveyarangi
 *
 */
public class AutoCameraController implements ICameraController
{
	private GameInputProcessor inputProcessor;
	
	private Level level;
	
	private OrthographicCamera camera;
	
	private Vector2 lastPosition;
	
	private Vector2 scrollTarget;
	private float zoomTarget;
	
	public AutoCameraController(GameInputProcessor inputProcessor, int w, int h, Level level)
	{
		this.inputProcessor = inputProcessor;
		this.camera = new OrthographicCamera( w, h );
		
		lastPosition = level.getAsteroid(level.getInitialConfig().getAsteroidName())
					.getModel().getNavNode( 
						level.getInitialConfig().getSurfaceIdx()).getPoint().cpy();
		camera.position.x = lastPosition.x;
		camera.position.y = lastPosition.y;
		zoomTarget = camera.zoom = level.getInitialConfig().getZoom();
		this.level = level;
		
		scrollTarget = new Vector2();
		
	}
	
	public void update( float delta )
	{
		Vector2 spiderPos = level.getPlayerSpider().getPosition();
		Vector2 pointerPos = inputProcessor.getCrosshairPosition();
		
		
		scrollTarget.set( pointerPos ).sub( spiderPos );
	/*	float dz = 0.9f;
		float distance = scrollTarget.len();
		if(distance > camera.zoom * camera.viewportHeight * dz)
			zoomTarget /= 0.001f*delta;
		else 
		if(distance < camera.zoom * camera.viewportHeight * 0.1)
			zoomTarget *= 0.001f*delta;*/
		
		scrollTarget.div( 2 ).add( spiderPos );
		
		camera.position.x = scrollTarget.x;
		camera.position.y = scrollTarget.y;
		camera.zoom = zoomTarget;
		
//		System.out.println(distance + " " +  + " " + zoomTarget);
		
		// storing last camera position:
		lastPosition.x = camera.position.x;
		lastPosition.y = camera.position.y;
		
		// calculating matrices:
		camera.update();
		
//		Vector3 tmp = new Vector3(pointerPos.x, pointerPos.y, 0);
//		camera.project( tmp );
		
//		Gdx.input.setCursorPosition( (int)tmp.x, (int)tmp.y );

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

	@Override
	public void setUnderUserControl(boolean b)
	{
		
	}

	@Override
	public void injectLinearImpulse(float x, float y, float z)
	{
		if(z == 0)
			return;
		
		if(z > 0)
			zoomTarget /= 0.9f;
		else
			zoomTarget *= 0.9f;
		
		update(0);
		
	}

	@Override
	public OrthographicCamera getCamera()
	{
		return camera;
	}

}
