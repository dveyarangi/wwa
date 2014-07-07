package eir.game.screens;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.debug.Debug;
import eir.game.EirGame;
import eir.game.LevelSetup;

public class LoadingScreen  extends AbstractScreen
{
	private LevelSetup levelSetup;

	private ShapeRenderer renderer = new ShapeRenderer();

	private int minx, miny, lw, lh;

	public LoadingScreen(final EirGame game, final LevelSetup levelSetup)
	{
		super(game);

		this.levelSetup = levelSetup;
	}

	@Override
	public void render(final float delta)
	{
		super.render( delta );
//		Debug.log( levelSetup.getState() + " " + levelSetup.getPercentage());
		float progress = levelSetup.getGameFactory().loadResources();

		renderer.begin( ShapeType.Rectangle );
		renderer.rect( minx, miny, lw, lh );
		renderer.end();
		renderer.begin( ShapeType.FilledRectangle );
		renderer.filledRect( minx, miny, progress*lw, lh );
		renderer.end();

		if(progress == 1)
		{
			game.setScreen( new GameScreen( game, levelSetup ) );
			Debug.log( "Finished loading level");

		}
	}
    @Override
	public void resize(
            final int width,
            final int height )
    {
    	this.minx = width / 2 - width / 4;
    	int maxx = width / 2 + width / 4;
    	lw = maxx - minx;
    	this.miny = height / 2 - height / 100;
    	int maxy = height / 2 + height / 100;
    	lh = maxy - miny;
    }
}
