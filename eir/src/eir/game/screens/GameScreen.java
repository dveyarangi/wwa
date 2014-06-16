package eir.game.screens;

import eir.debug.Debug;
import eir.game.EirGame;
import eir.input.GameInputProcessor;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.LevelRenderer;

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
	private LevelRenderer renderer;
	private Level level;

	public GameScreen(final EirGame game)
	{
		super( game );

		GameFactory.init();


		level = GameFactory.loadLevel( "levels/level_exodus_01.dat" );

		inputController = new GameInputProcessor( level );

		level.getBackground().init( inputController );

		Debug.init( level, inputController );

		this.renderer = new LevelRenderer( inputController, level );
	}

	@Override
	public void render(final float delta)
	{
		super.render( delta );
		inputController.update( delta );

		float modifiedTime = inputController.getTimeModifier() * delta;

		level.update( modifiedTime );
		Debug.debug.update( delta );

//		Gdx.gl.glClearColor( 0.8f, 0.8f, 1f, 1 );
//		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		level.getBackground().update( modifiedTime );

		renderer.render( modifiedTime );

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

		renderer.dispose();

		// dispose textures and stuff:
		GameFactory.dispose();

	}

}
