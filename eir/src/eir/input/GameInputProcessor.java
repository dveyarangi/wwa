package eir.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.unit.Spider;

/**
 * handles input for game
 * @author Ni
 *
 */
public class GameInputProcessor implements InputProcessor
{
	private InputMultiplexer inputMultiplexer;
	
	private final ICameraController camController;
	
	private final Level level;
	
	private final Spider playerSpider;
	
	private int lastx, lasty;
	private Vector3 pointerPosition3 = new Vector3();
	private Vector2 pointerPosition2 = new Vector2();
	
	private boolean dragging = false;
	
	private Texture crosshair;
	
	public GameInputProcessor(Level level)
	{

		
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

//		camController = new CameraController(w, h, level);		
		camController = new AutoCameraController(this, w, h, level);		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor( new UIInputProcessor() );
		inputMultiplexer.addProcessor( new GestureDetector(new GameGestureListener(camController)) );
		inputMultiplexer.addProcessor( this );

		this.level = level;
		
		this.playerSpider = level.getPlayerSpider();
		
		this.crosshair = GameFactory.loadTexture( "skins/crosshair.png" );
		lastx = (int) camController.getCamera().viewportWidth/2;
		lasty = (int) camController.getCamera().viewportHeight/2;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		switch(keycode)
		{
		case Input.Keys.A:
			playerSpider.walkCCW(true);
			break;
		case Input.Keys.D:
			playerSpider.walkCW(true);
			break;
		case Input.Keys.W:
			playerSpider.walkUp(true);
			break;
		case Input.Keys.S:
			playerSpider.walkDown(true);
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch(keycode)
		{
		case Input.Keys.A:
			playerSpider.walkCCW(false);
			break;
		case Input.Keys.D:
			playerSpider.walkCW(false);
			break;
		case Input.Keys.W:
			playerSpider.walkUp(false);
			break;
		case Input.Keys.S:
			playerSpider.walkDown(false);
			break;
		default:
			return false;
		}
		return true;	}

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
		
		playerSpider.setShootingTarget( pointerPosition2 );

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		dragging = false;
		camController.setUnderUserControl(false);
		playerSpider.setShootingTarget( null );
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
			camController.injectLinearImpulse(-amount*(lastx - camController.getCamera().viewportWidth/2)*2, 
										 	   amount*(lasty - camController.getCamera().viewportHeight/2)*2, 
										 	   amount*1.2f);
			return true;
		}
		return false;
	}
	
	public OrthographicCamera getCamera() { return camController.getCamera(); }

	/**
	 * 
	 */
	public void show()
	{
		Gdx.input.setInputProcessor( inputMultiplexer );
	}

	/**
	 * @param delta
	 */
	public void update(float delta)
	{
		pointerPosition3.x = lastx;
		pointerPosition3.y = lasty;
	    camController.getCamera().unproject( pointerPosition3 );
	    pointerPosition2.x = pointerPosition3.x;
	    
	    pointerPosition2.y = pointerPosition3.y;		
	    camController.update( delta );
	}
	
	public void draw(SpriteBatch batch, ShapeRenderer renderer)
	{
		batch.begin();
		batch.draw( crosshair, pointerPosition2.x-2, pointerPosition2.y-2, 4, 4 );
		batch.end();
		
		renderer.setColor( 0, 1, 0, 0.1f );
		renderer.begin( ShapeType.Line );
		renderer.line( playerSpider.getPosition().x, playerSpider.getPosition().y, 
					pointerPosition2.x, pointerPosition2.y );
		renderer.end();
	}

	/**
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height)
	{
		camController.resize(width, height);
	}
	
	public Vector2 getCrosshairPosition()
	{
		return pointerPosition2;
	}
}
