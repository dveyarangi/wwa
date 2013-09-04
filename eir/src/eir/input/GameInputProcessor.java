package eir.input;

import com.badlogic.gdx.Gdx;
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
	private OrthographicCamera camera;
	
	private int lastx = -1;
	private int lasty = -1;
	
	public GameInputProcessor(OrthographicCamera camera)
	{
		this.camera = camera;
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
		camera.position.x -= (screenX-lastx)*camera.zoom;
		camera.position.y += (screenY-lasty)*camera.zoom;
		camera.update();
		lastx = screenX;
		lasty = screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		System.out.println(camera.zoom);
		camera.zoom += amount*0.1*camera.zoom;
		camera.update();
		
		return true;
	}

}
