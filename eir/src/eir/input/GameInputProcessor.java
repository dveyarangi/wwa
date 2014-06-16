package eir.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import eir.debug.Debug;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.unit.UnitsFactory;
import eir.world.unit.cannons.Cannon;

/**
 * handles input for game
 * @author Ni
 *
 */
public class GameInputProcessor implements InputProcessor
{
	private final InputMultiplexer inputMultiplexer;

	private final AutoCameraController autoController;
	private final FreeCameraController freeController;
	private ICameraController camController;

	private final Level level;

	private int lastx, lasty;
	private final Vector3 pointerPosition3 = new Vector3();
	private final Vector2 pointerPosition2 = new Vector2();

	private boolean dragging = false;

	private static int crosshairId = GameFactory.registerAnimation("anima//ui//crosshair01.atlas", "crosshair");
	private static Animation crosshair = GameFactory.getAnimation( crosshairId );

	private float lifeTime = 0;

	private final PickingSensor pickingSensor;

	private float timeModifier = 1;

	private UIInputProcessor uiProcessor;

	public GameInputProcessor(final Level level)
	{


		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		OrthographicCamera camera = new OrthographicCamera( w, h );

		freeController = new FreeCameraController(camera, level);
		autoController = new AutoCameraController(camera, this, level);

		camController = freeController;

		inputMultiplexer = new InputMultiplexer();


		this.uiProcessor = new UIInputProcessor();

		Debug.registerDebugActions( uiProcessor );

		uiProcessor.registerAction( Keys.PLUS, new InputAction() {
			@Override
			public void execute( final InputContext context )
			{
				if(timeModifier < 4)
				{
					timeModifier *= 2;
				}
			}
		});
		uiProcessor.registerAction( Keys.MINUS, new InputAction() {
			@Override
			public void execute( final InputContext context )
			{
				if(timeModifier >= 0.25)
				{
					timeModifier /= 2;
				}
			}
		});

		inputMultiplexer.addProcessor( uiProcessor );
		inputMultiplexer.addProcessor( new GestureDetector(new GameGestureListener(camController)) );
		inputMultiplexer.addProcessor( this );

		this.level = level;

		lastx = (int) camController.getCamera().viewportWidth/2;
		lasty = (int) camController.getCamera().viewportHeight/2;

		pickingSensor = new PickingSensor();

	}

	@Override
	public boolean keyDown(final int keycode)
	{
		switch(keycode)
		{

		case Input.Keys.A:
			level.getControlledUnit().walkCCW(true);
			break;

		case Input.Keys.D:
			level.getControlledUnit().walkCW(true);
			break;

		case Input.Keys.W:
			level.getControlledUnit().walkUp(true);
			break;

		case Input.Keys.S:
			level.getControlledUnit().walkDown(true);
			break;

		case Input.Keys.SPACE:
			if(camController == freeController)
			{
				autoController.zoomTarget = camController.getCamera().zoom;
				camController = autoController;
			}
			else
			{
				camController = freeController;
			}
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(final int keycode)
	{
		switch(keycode)
		{
		case Input.Keys.A:
			level.getControlledUnit().walkCCW(false);
			break;
		case Input.Keys.D:
			level.getControlledUnit().walkCW(false);
			break;
		case Input.Keys.W:
			level.getControlledUnit().walkUp(false);
			break;
		case Input.Keys.S:
			level.getControlledUnit().walkDown(false);
			break;
		default:
			return false;
		}
		return true;	}

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

			if(pickingSensor.getNode() != null)
			{

				Cannon cannon = level.getUnitsFactory().getUnit( UnitsFactory.CANNON, pickingSensor.getNode(), level.getFactions()[0] );

				level.addUnit( cannon );

				cannon.postinit( level );

//				NavNode sourceNode = level.getControlledUnit().anchor;
//				NavNode targetNode = pickingSensor.getNode();

//				level.toggleWeb((SurfaceNavNode)sourceNode, (SurfaceNavNode)targetNode);
			}
		}

		if(button == Input.Buttons.LEFT)
		{
			level.getControlledUnit().setShootingTarget( pointerPosition2 );
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
		if(button == Input.Buttons.LEFT)
		{
			level.getControlledUnit().setShootingTarget( null );
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
		 pickingSensor.clear();
		 level.getEnvironment().getIndex().queryAABB( pickingSensor,
				 pointerPosition2.x,
				 pointerPosition2.y, 3, 3 );


	 }

	 public void draw(final SpriteBatch batch, final ShapeRenderer renderer)
	 {
		 batch.begin();
		 TextureRegion crossHairregion = crosshair.getKeyFrame( lifeTime, true );
		 batch.draw( crossHairregion,
				 pointerPosition2.x-crossHairregion.getRegionWidth()/2, pointerPosition2.y-crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
				 5f/crossHairregion.getRegionWidth(),
				 5f/crossHairregion.getRegionWidth(), 0);
		 batch.end();

		 renderer.setColor( 0, 1, 0, 0.1f );
		 renderer.begin( ShapeType.Line );
		 renderer.line( level.getControlledUnit().getBody().getAnchor().x, level.getControlledUnit().getBody().getAnchor().y,
				 pointerPosition2.x, pointerPosition2.y );
		 renderer.end();

		 if(pickingSensor.getNode() != null)
		 {
			 Vector2 point = pickingSensor.getNode().getPoint();
			 batch.begin();
			 crossHairregion = crosshair.getKeyFrame( lifeTime, true );
			 batch.draw( crossHairregion,
					 point.x-crossHairregion.getRegionWidth()/2, point.y-crossHairregion.getRegionHeight()/2,
					 crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
					 crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
					 5f/crossHairregion.getRegionWidth(),
					 5f/crossHairregion.getRegionWidth(), 0);
			 batch.end();		}
	 }

	 /**
	  * @param width
	  * @param height
	  */
	 public void resize(final int width, final int height)
	 {
		 autoController.resize(width, height);
		 freeController.resize(width, height);
	 }

	 public Vector2 getCrosshairPosition()
	 {
		 return pointerPosition2;
	 }

	public float getTimeModifier() { return timeModifier ; }
}
