package eir.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

import eir.debug.Debug;
import eir.game.EirGame;


/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen
{
    // the fixed viewport dimensions (ratio: 1.6)
    protected static final int GAME_VIEWPORT_WIDTH = 512, GAME_VIEWPORT_HEIGHT = 1024;
    protected static final int MENU_VIEWPORT_WIDTH = 1024, MENU_VIEWPORT_HEIGHT = 512;

    protected final EirGame game;
    protected final Stage stage;

    private BitmapFont font;
    private SpriteBatch batch;
    private TextureAtlas atlas;


    public AbstractScreen( final EirGame game )
    {
        this.game = game;
        int width = isGameScreen() ? GAME_VIEWPORT_WIDTH : MENU_VIEWPORT_WIDTH;
        int height = isGameScreen() ? GAME_VIEWPORT_HEIGHT : MENU_VIEWPORT_HEIGHT;
        this.stage = new Stage();
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }

    protected boolean isGameScreen()
    {
        return false;
    }

    // Lazily loaded collaborators

    public BitmapFont getFont()
    {
        if( font == null ) {
            font = new BitmapFont();
        }
        return font;
    }

    public SpriteBatch getBatch()
    {
        if( batch == null ) {
            batch = new SpriteBatch();
        }
        return batch;
    }

    public TextureAtlas getAtlas()
    {
        if( atlas == null ) {
            atlas = new TextureAtlas( Gdx.files.internal( "image-atlases/pages-info" ) );
        }
        return atlas;
    }


    // Screen implementation

    @Override
    public void show()
    {
    	Debug.log( "Showing screen" );

        // set the stage as the input processor
        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void resize(
        final int width,
        final int height )
    {
    	Debug.log( "Resizing screen to: " + width + " x " + height );
    }

    @Override
    public void render(
        final float delta )
    {
        // (1) process the game logic

        // update the actors
        stage.act( delta );

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // draw the actors
        stage.draw();
    }

    @Override
    public void hide()
    {
    	Debug.log( "Hiding screen" );

        // dispose the screen when leaving the screen;
        // note that the dipose() method is not called automatically by the
        // framework, so we must figure out when it's appropriate to call it
        dispose();
    }

    @Override
    public void pause()
    {
    	Debug.log( "Pausing screen" );
    }

    @Override
    public void resume()
    {
    	Debug.log( "Resuming screen" );
    }

    @Override
    public void dispose()
    {
    	Debug.log( "Disposing screen" );

        // as the collaborators are lazily loaded, they may be null
        if( font != null )
		{
			font.dispose();
		}
        if( batch != null )
		{
			batch.dispose();
		}
        if( atlas != null )
		{
			atlas.dispose();
		}
    }

    ///////////////////////////////////////////////////////////////////////////

}