package eir.game.screens;

import eir.debug.Debug;
import eir.game.EirGame;
import eir.input.GameInputProcessor;
import eir.rendering.LevelRenderer;
import eir.resources.GameFactory;
import eir.resources.levels.LevelBuilder;
import eir.resources.levels.LevelDef;
import eir.world.Level;
import eir.world.unit.UnitsFactory;

/**
 * place holder screen for now. does same as application listener from sample
 * code.
 *
 * @author Ni
 *
 */
public class GameScreen extends AbstractScreen
{
	private GameFactory gameFactory;
	private GameInputProcessor inputController;
	private LevelRenderer renderer;
	private Level level;

	public GameScreen(final EirGame game)
	{
		super( game );

		// game resources registry and loader
		gameFactory = new GameFactory();

/*
		int width = 1536;
		int height = 1536;
		int factionsNum = 3;
		int asteroidNum = 15;

		LevelParameters levelParams = new LevelParameters( width, height, factionsNum, asteroidNum);
		Level level = new LevelGenerator().generate( levelParams );
*/

		// preparing units factory:
		UnitsFactory unitsFactory = new UnitsFactory( gameFactory );

		// loading level definitions from file:
		// this call also registers resource handles at the factory for future loading
		LevelDef levelDef = gameFactory.readLevelDefs( "levels/level_exodus_01.dat", unitsFactory );

		// loading level; this is long procedures that load level resources
		// TODO: run in separate thread and make loading screen:
		gameFactory.loadLevelResources();

		// creating level from level definitions:
		level = new LevelBuilder( gameFactory, unitsFactory ).build( levelDef );

		inputController = new GameInputProcessor( gameFactory, level );

		level.getBackground().init( gameFactory, inputController );

		Debug.init( level, inputController );

		this.renderer = new LevelRenderer( gameFactory, inputController, level );
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
		gameFactory.dispose();

	}

}
