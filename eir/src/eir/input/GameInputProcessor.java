package eir.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * handles input for game
 * @author Ni
 *
 */
public class GameInputProcessor implements InputProcessor
{
	private final CameraController camController;
	private final OrthographicCamera camera;
	
	private int lastx = -1;
	private int lasty = -1;
	
	public GameInputProcessor(CameraController camController)
	{
		this.camController = camController;
		this.camera = camController.camera;
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
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		camController.injectImpulse((lastx-screenX)*10, (screenY-lasty)*10, 0);
		lastx = screenX;
		lasty = screenY;
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
		camController.injectImpulse(-amount*(lastx - camera.viewportWidth/2), 
									 amount*(lasty - camera.viewportHeight/2), 
									 amount);
		return true;
	}

}
