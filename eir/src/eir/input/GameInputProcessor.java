package eir.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import eir.resources.Level;

/**
 * handles input for game
 * @author Ni
 *
 */
public class GameInputProcessor implements InputProcessor
{
	private final CameraController camController;
	private final OrthographicCamera camera;
	private final Level level;
	
	private int lastx = -1;
	private int lasty = -1;
	
	private boolean dragging = false;
	
	public GameInputProcessor(CameraController camController, Level level)
	{
		this.camController = camController;
		this.camera = camController.camera;
		this.level = level;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if( button==Input.Buttons.LEFT )
		{
			lastx = screenX;
			lasty = screenY;
			dragging = true;
			camController.setUnderUserControl(true);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		dragging = false;
		camController.setUnderUserControl(false);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if( dragging )
		{
			camController.injectLinearImpulse((lastx-screenX)*10, (screenY-lasty)*10, 0);
			lastx = screenX;
			lasty = screenY;
		}
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		lastx = screenX;
		lasty = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		if( !dragging )
		{
			camController.injectLinearImpulse(-amount*(lastx - camera.viewportWidth/2)*2, 
										 	   amount*(lasty - camera.viewportHeight/2)*2, 
										 	   amount*1.2f);
			return true;
		}
		return false;
	}
	
	public OrthographicCamera getCamera() { return camera; }

}
