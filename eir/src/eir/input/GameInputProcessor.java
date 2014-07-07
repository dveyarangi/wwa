package eir.input;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eir.debug.Debug;
import eir.rendering.IRenderer;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;

/**
 * handles input for game
 * @author Ni
 *
 */
public class GameInputProcessor implements InputProcessor
{
	private final InputMultiplexer inputMultiplexer;
	private final FreeCameraController freeController;
	private ICameraController camController;

	private final Level level;

	private int lastx, lasty;
	private final Vector3 pointerPosition3 = new Vector3();
	private final Vector2 pointerPosition2 = new Vector2();

	private boolean dragging = false;

	private float lifeTime = 0;

	private final IControlMode [] controlModes;
	private int controlModeIdx;

	private ISpatialObject pickedObject;

	private TimeController timeController;

	private UIInputProcessor uiProcessor;

	public GameInputProcessor(final GameFactory factory, final Level level)
	{


		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		OrthographicCamera camera = new OrthographicCamera( w, h );

		freeController = new FreeCameraController(camera, level);

		camController = freeController;

		inputMultiplexer = new InputMultiplexer();


		this.uiProcessor = new UIInputProcessor();

		Debug.registerDebugActions( uiProcessor );

		this.timeController = new TimeController();

		uiProcessor.registerAction( Keys.PLUS, new InputAction() {
			@Override
			public void execute( final InputContext context )
			{
				timeController.setTarget( timeController.getTargetModifier() * 2f);
			}
		});
		uiProcessor.registerAction( Keys.MINUS, new InputAction() {
			@Override
			public void execute( final InputContext context )
			{
				timeController.setTarget( timeController.getTargetModifier() / 2f);
			}
		});


		controlModes = new IControlMode [] {
				new BuildingControlMode( factory, level ),
				new OrderingControlMode()
		};

		controlModeIdx = 0;

		inputMultiplexer.addProcessor( uiProcessor );
		inputMultiplexer.addProcessor( new GestureDetector(new GameGestureListener(camController)) );
		inputMultiplexer.addProcessor( this );

		this.level = level;

		lastx = (int) camController.getCamera().viewportWidth/2;
		lasty = (int) camController.getCamera().viewportHeight/2;

	}

	@Override
	public boolean keyDown(final int keycode)
	{
		switch(keycode)
		{

		case Input.Keys.M:
			controlModeIdx = (controlModeIdx + 1) % controlModes.length;
			controlModes[controlModeIdx].reset();


			break;

/*		case Input.Keys.SPACE:
			if(camController == freeController)
			{
				autoController.zoomTarget = camController.getCamera().zoom;
				camController = autoController;
			}
			else
			{
				camController = freeController;
			}
			break;*/
		default:

			controlModes[controlModeIdx].keyDown( keycode );
			return false;
		}

		return false;
	}

	@Override
	public boolean keyUp(final int keycode)
	{
		switch(keycode)
		{
		default:
			return false;
		}
//		return false;
	}

	@Override
	public boolean keyTyped(final char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
	{
		if( button==Input.Buttons.RIGHT )
		{
			lastx = screenX;
			lasty = screenY;
			camController.setUnderUserControl(true);
			dragging = true;

			if( pickedObject != null )
			{
				controlModes[controlModeIdx].touchUnit( pickedObject );
			}
		}

		return true;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
	{
		if(button == Input.Buttons.RIGHT)
		{
			dragging = false;
			camController.setUnderUserControl(false);
		}
		return true;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer)
	{
		if(dragging)
		{
//			camController.injectLinearImpulse((lastx-screenX)*10, (screenY-lasty)*10, 0);
		}
		lastx = screenX;
		lasty = screenY;

		return true;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY)
	{
		lastx = screenX;
		lasty = screenY;

		return true;
	}

	@Override
	public boolean scrolled(final int amount)
	{
		camController.zoomTo( lastx, lasty, amount*1.2f );
		return true;
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
	 public void update(final float delta)
	 {
		 pointerPosition3.x = lastx;
		 pointerPosition3.y = lasty;
		 camController.getCamera().unproject( pointerPosition3 );
		 pointerPosition2.x = pointerPosition3.x;

		 pointerPosition2.y = pointerPosition3.y;
		 camController.update( delta );
		 lifeTime += delta;

		 IControlMode mode = controlModes[controlModeIdx];
		 PickingSensor sensor = mode.getPickingSensor();
		 sensor.clear();
		 level.getEnvironment().getIndex().queryAABB( sensor,
				 pointerPosition2.x,
				 pointerPosition2.y, 3, 3 );


		 List <ISpatialObject> pickedObjects = sensor.getPickedObjects();
		 ISpatialObject newPickedObject = null;
		 if(pickedObjects != null)
		 {
			 newPickedObject = mode.objectPicked( pickedObjects );
		 }


		 if(newPickedObject != pickedObject)
		 {
			 mode.objectUnpicked( pickedObject );
			 pickedObject = newPickedObject;
		 }




		 timeController.update( delta );
	 }

	 public void draw( final IRenderer renderer )
	 {

		 final SpriteBatch batch = renderer.getSpriteBatch();
		 final ShapeRenderer shape = renderer.getShapeRenderer();

/*		 batch.begin();
		 TextureRegion crossHairregion = crosshair.getKeyFrame( lifeTime, true );
		 batch.draw( crossHairregion,
				 pointerPosition2.x-crossHairregion.getRegionWidth()/2, pointerPosition2.y-crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
				 5f/crossHairregion.getRegionWidth(),
				 5f/crossHairregion.getRegionWidth(), 0);
		 batch.end();*/

/*		 shape.setColor( 0, 1, 0, 0.1f );
		 shape.begin( ShapeType.Line );
		 shape.line( level.getControlledUnit().getBody().getAnchor().x, level.getControlledUnit().getBody().getAnchor().y,
				 pointerPosition2.x, pointerPosition2.y );
		 shape.end();*/

		 IControlMode mode = controlModes[controlModeIdx];

		 mode.render( renderer );

	 }

	 /**
	  * @param width
	  * @param height
	  */
	 public void resize(final int width, final int height)
	 {
//		 autoController.resize(width, height);
		 freeController.resize(width, height);
	 }

	 public Vector2 getCrosshairPosition()
	 {
		 return pointerPosition2;
	 }

	public float getTimeModifier() { return timeController.getModifier() ; }

}
