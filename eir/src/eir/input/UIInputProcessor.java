package eir.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import eir.debug.Debug;

/**
 * @author Ni
 * 
 */
public class UIInputProcessor implements InputProcessor
{

	@Override
	public boolean keyDown( int keycode )
	{
		if( keycode == Keys.J )
			Debug.toggleCoordinateGrid();
		if( keycode == Keys.K )
			Debug.toggleNavMesh();
		if( keycode == Keys.L )
			Debug.toggleSpatialGrid();
		if( keycode == Keys.SEMICOLON )
			Debug.toggleFactions();
		if( keycode == Keys.APOSTROPHE )
			Debug.toggleBox2D();

		return false;
	}

	@Override
	public boolean keyUp( int keycode )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped( char character )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown( int screenX, int screenY, int pointer, int button )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp( int screenX, int screenY, int pointer, int button )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged( int screenX, int screenY, int pointer )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved( int screenX, int screenY )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled( int amount )
	{
		// TODO Auto-generated method stub
		return false;
	}

}
