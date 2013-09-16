package eir.game.screens;

import java.util.LinkedList;
import java.util.List;

import yarangi.numbers.RandomUtil;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;

import eir.debug.CoordinateGrid;
import eir.debug.DebugRenderer;
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
import eir.world.environment.NavNode;
import eir.world.unit.Ant;
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
	private ShapeRenderer shapeRenderer;
	
	private DebugRenderer debug;
	
	private GameFactory gameFactory;
	

	private Level level; 

	private float w, h;
	
	private List <Spider> spiders = new LinkedList <Spider> ();
	private List <Ant> ants = new LinkedList <Ant> ();

	
	public GameScreen(EirGame game)
	{
		super( game );
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera( w, h );

		batch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();
		
		this.gameFactory = new GameFactory();


		level = gameFactory.loadLevel( "levels/level_exodus_01.dat" );
		level.init(gameFactory);

		camController = new CameraController(camera, level);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor( new UIInputProcessor() );
		inputMultiplexer.addProcessor( new GestureDetector(new GameGestureListener(camController)) );
		inputMultiplexer.addProcessor( new GameInputProcessor(camController, level) );
		
		
		// infest Nir:
		for(int i = 0; i < 15; i ++)
		{
			spiders.add(new Spider( gameFactory, level.getAsteroids().get(2), 
					RandomUtil.N( 10 ) + 5, // size 
					RandomUtil.N( 25 ), // location
					(RandomUtil.N(2)==1? 1:-1) *(RandomUtil.R( 20 )+5) ) // speed
			);
		}
		
		// increasing infestation
		for(int i = 0; i < 10; i ++)
		{
			NavNode startingNode = gameFactory.getNavMesh().getNode( 
					RandomUtil.N( 33 ) + 60 );
			Ant ant = Ant.getAnt( gameFactory, startingNode );
			
			ants.add( ant );
		}

		
		debug = new DebugRenderer( gameFactory, level, camera );
	}

	@Override
	public void render(float delta)
	{
		super.render( delta );
		camController.cameraStep(delta);
		
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
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
		
		for(Spider spider : spiders)
		{
			spider.update(delta);
			spider.draw( batch );
		}
		
		for(Ant ant : ants)
		{
			ant.update(delta);
			ant.draw( delta, batch );
		}
		
		batch.end();
		
		
		//////////////////////////////////////////////////////////////////
		// debug rendering
		
		debug.draw(batch, shapeRenderer);
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
		
		// dispose sprite batch renderer:
		batch.dispose();
		
		// dispose shape renderer:
		shapeRenderer.dispose();
		
		// dispose textures and stuff:
		gameFactory.dispose();
		
	}

}
