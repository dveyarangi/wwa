package eir.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import eir.debug.CoordinateGrid;
import eir.game.EirGame;
import eir.input.CameraController;
import eir.input.GameGestureListener;
import eir.input.GameInputProcessor;
import eir.input.UIInputProcessor;
import eir.resources.GameFactory;
import eir.resources.Level;
import eir.world.Asteroid;
import eir.world.Web;
import eir.world.environment.NavMesh;
import eir.world.unit.Spider;

/**
 * place holder screen for now. does same as application listener from sample
 * code.
 * 
 * @author Ni
 * 
 */
public class GameScreen extends AbstractScreen
{
	private InputMultiplexer inputMultiplexer;
	
	private OrthographicCamera camera;
	private CameraController camController;
	private SpriteBatch batch;

	private static final Vector2 GRAVITY = Vector2.Zero;

	private World physicsWorld;
	private Box2DDebugRenderer debugRenderer;
	
	private ShapeRenderer shapeRenderer;
	
	private GameFactory gameFactory;
	

	private Level level; 

	private float w, h;
	
	private NavMesh navMesh;
	
	private Spider spider;

	private CoordinateGrid debugGrid;
	
	public GameScreen(EirGame game)
	{
		super( game );
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera( w, h );

		batch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();
		
		physicsWorld = new World( GRAVITY, true/* sleep */);

		debugRenderer = new Box2DDebugRenderer(true, true, false, true, true);
		
		this.navMesh = new NavMesh();
		
		this.gameFactory = new GameFactory( navMesh );
		
//		String levelName = loader.getLevelNames( "exodus" ).iterator().next();


		level = gameFactory.loadLevel( "data/levels/level_exodus_01.dat" );
		level.init(gameFactory);
		
		camController = new CameraController(camera, level);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor( new UIInputProcessor() );
		inputMultiplexer.addProcessor( new GestureDetector(new GameGestureListener(camController)) );
		inputMultiplexer.addProcessor( new GameInputProcessor(camController, level) );
		
		debugGrid = new CoordinateGrid( level.getWidth(), level.getHeight(), camera );
		
		spider = new Spider( physicsWorld, 200, 100 );
		spider.setAsteroid( level.getAsteroids().iterator().next());
	}

	@Override
	public void render(float delta)
	{
		super.render( delta );
		camController.cameraStep(delta);
		
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		spider.update(delta);
		physicsWorld.step( delta, 3,1 );
		
		// setting renderers to camera view:
		// TODO: those are copying matrix arrays, maybe there is a lighter way to do this
		// TODO: at least optimize to not set if camera has not moved
		batch.setProjectionMatrix( camera.projection );
		batch.setTransformMatrix( camera.view );
		
		shapeRenderer.setProjectionMatrix( camera.projection);
		shapeRenderer.setTransformMatrix( camera.view );
		
		batch.begin();
		
		batch.draw( level.getBackgroundTexture(), -level.getWidth()/2, -level.getHeight()/2, level.getWidth(), level.getHeight() );
		// TODO: clipping?
		for(Asteroid asteroid : level.getAsteroids())
		{
			asteroid.draw( batch );
		}
		
		for( Web web : level.getWebs() )
		{
			web.draw( batch );
		}
		
		batch.end();
		
		//////////////////////////////////////////////////////////////////
		// debug rendering
		
		debugGrid.render( shapeRenderer );
		
		navMesh.draw( shapeRenderer);
		
		debugRenderer.render( physicsWorld, camera.combined );
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize( width, height );
		this.w = width;
		this.h = height;
		camera.setToOrtho( false, width, height );
	}

	@Override
	public void show()
	{
		super.show();
		Gdx.input.setInputProcessor( inputMultiplexer );
	}

	@Override
	public void hide()
	{
		super.hide();
	}

	@Override
	public void pause()
	{
		super.pause();
	}

	@Override
	public void resume()
	{
		super.resume();
	}

	@Override
	public void dispose()
	{
		super.dispose();
		gameFactory.dispose();
	}

}
