/**
 * 
 */
package eir.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

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
	public float zoomTarget;
	
	public AutoCameraController(OrthographicCamera camera, GameInputProcessor inputProcessor, Level level)
	{
		this.camera = camera;
		this.inputProcessor = inputProcessor;
		
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
		
		updateCameraPosition( spiderPos, pointerPos ); 

		// calculating matrices:
		camera.update();


	}
	
	private void updateCameraPosition(Vector2 playerPos, Vector2 pointerPos)
	{
		
		scrollTarget.set( pointerPos ).sub( playerPos );
		
		scrollTarget.div( 2 ).add( playerPos );
		
		camera.position.x += (scrollTarget.x - camera.position.x) / 2;
		camera.position.y += (scrollTarget.y - camera.position.y) / 2;
		camera.zoom += (zoomTarget - camera.zoom) / 10;

		// storing last camera position:
		lastPosition.x = camera.position.x;
		lastPosition.y = camera.position.y;
		
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
		
		// move cursor to the spider to avoid the initial camera jump:
		Gdx.input.setCursorPosition( (int)camera.viewportWidth/2, (int)camera.viewportHeight/2 );
		updateCameraPosition( lastPosition, lastPosition );
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
		
		// bounding zoom
		// TODO: to level config
		if(zoomTarget > 1.5f)
			zoomTarget = 1.5f;
		else
		if(zoomTarget < 0.05f)
			zoomTarget = 0.05f;		
	}

	@Override
	public OrthographicCamera getCamera()
	{
		return camera;
	}

}
