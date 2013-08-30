package eir.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;

import eir.game.screens.MenuScreen;

/**
 * The game's main class, called as application events are fired.
 */
public class EirGame extends Game
{
    // constant useful for logging
    public static final String LOG = "EIR";

    // whether we are in development mode
    public static final boolean DEV_MODE = true;

    // a libgdx helper class that logs the current FPS each second
    private FPSLogger fpsLogger;

    public EirGame()
    {
    }

 

    // Game-related methods

    @Override
    public void create()
    {
        Gdx.app.log( LOG, "Creating game on " + Gdx.app.getType() );

        // create the helper objects
        fpsLogger = new FPSLogger();
    }

    @Override
    public void resize( int width, int height )
    {
        super.resize( width, height );
        Gdx.app.log( LOG, "Resizing game to: " + width + " x " + height );

        // show the splash screen when the game is resized for the first time;
        // this approach avoids calling the screen's resize method repeatedly
 //       if( getScreen() == null ) {
 //           setScreen( new SplashScreen( this ) );
 //       }
        setScreen( new MenuScreen( this ) );
    }

    @Override
    public void render()
    {
        super.render();

        // output the current FPS
        if( DEV_MODE ) fpsLogger.log();
    }

    @Override
    public void pause()
    {
        super.pause();
        Gdx.app.log( LOG, "Pausing game" );

    }

    @Override
    public void resume()
    {
        super.resume();
        Gdx.app.log( LOG, "Resuming game" );
    }

    @Override
    public void setScreen( Screen screen )
    {
        super.setScreen( screen );
        Gdx.app.log( LOG, "Setting screen: " + screen.getClass().getSimpleName() );
    }

    @Override
    public void dispose()
    {
        super.dispose();
        Gdx.app.log( LOG, "Disposing game" );

    }
}