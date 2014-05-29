package eir.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.debug.Debug;
import eir.game.EirGame;
import eir.input.GameInputProcessor;
import eir.resources.GameFactory;
import eir.world.Level;

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

	public GameScreen(final EirGame game)
	{
		super( game );

		GameFactory.init();

		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();

		level = GameFactory.loadLevel( "levels/level_exodus_01.dat" );

		inputController = new GameInputProcessor( level );

		level.getBackground().init( inputController );

		Debug.init( level, inputController );
	}

	@Override
	public void render(final float delta)
	{
		super.render( delta );
		inputController.update( delta );
		level.update( delta );

//		Gdx.gl.glClearColor( 0.8f, 0.8f, 1f, 1 );
//		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		level.getBackground().update( delta );
		level.getBackground().draw( batch );


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
		inputController.draw( batch, shapeRenderer );
		Debug.debug.update( delta );
		Debug.debug.draw(batch, shapeRenderer);

	}

	@Override
	public void resize(final int width, final int height)
	{
		super.resize( width, height );
		inputController.resize( width, height );

		level.getBackground().resize( width, height );
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
