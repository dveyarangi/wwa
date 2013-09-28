package eir.game.screens;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.debug.Debug;
import eir.game.EirGame;
import eir.input.GameInputProcessor;
import eir.resources.GameFactory;
import eir.world.Level;
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
	private GameInputProcessor inputController;
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private Level level; 
	
	public GameScreen(EirGame game)
	{
		super( game );
		
		GameFactory.init();

		batch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();

		level = GameFactory.loadLevel( "levels/level_exodus_01.dat" );
		level.init();

		
		// infest Nir:
/*		for(int i = 0; i < 15; i ++)
		{
			spiders.add(new Spider( level.getAsteroids().get(2), 
					RandomUtil.N( 10 ) + 5, // size 
					RandomUtil.N( 25 ), // location
					(RandomUtil.N(2)==1? 1:-1) *(RandomUtil.R( 20 )+5) ) // speed
			);
		}*/
		
		// increasing infestation
		for(int i = 0; i < 150; i ++)
		{
			NavNode startingNode = level.getNavMesh().getNode( 
					RandomUtil.N( 30 ) + 60 );
			level.addAnt(startingNode);
		}
		
		inputController = new GameInputProcessor( level );

		
		Debug.init( level, inputController );
	}

	@Override
	public void render(float delta)
	{
		super.render( delta );
		inputController.update( delta );
		level.update( delta );
		
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		// setting renderers to camera view:
		// TODO: those are copying matrix arrays, maybe there is a lighter way to do this
		// TODO: at least optimize to not set if camera has not moved
		batch.setProjectionMatrix( inputController.getCamera().projection );
		batch.setTransformMatrix( inputController.getCamera().view );
		
		shapeRenderer.setProjectionMatrix( inputController.getCamera().projection);
		shapeRenderer.setTransformMatrix( inputController.getCamera().view );
		
		
		
		level.draw(batch);
		
		//////////////////////////////////////////////////////////////////
		// debug rendering
		
		Debug.debug.update( delta );
		Debug.debug.draw(batch, shapeRenderer);
		
		
		inputController.draw( batch, shapeRenderer );
		
		batch.begin();
		
		Spider playerSpider = level.getPlayerSpider();
		playerSpider.update(delta);
		playerSpider.draw(batch);
/*		for(Spider spider : spiders)
		{
			spider.update(delta);
			spider.draw( batch );
		}*/
		
		for(Ant ant : level.getAnts())
		{
			ant.draw( delta, batch );
		}
		
		batch.end();
		
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize( width, height );
		inputController.resize( width, height );
	}

	@Override
	public void show()
	{
		super.show();
		inputController.show();
		
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
		GameFactory.dispose();
		
	}

}
