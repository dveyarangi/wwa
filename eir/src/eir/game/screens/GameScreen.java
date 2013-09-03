package eir.game.screens;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import eir.game.EirGame;
import eir.resources.BodyLoader;
import eir.resources.GameFactory;
import eir.resources.Level;
import eir.resources.LevelLoader;
import eir.world.Asteroid;

/**
 * place holder screen for now. does same as application listener from sample
 * code.
 * 
 * @author Ni
 * 
 */
public class GameScreen extends AbstractScreen
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;

	private static final Vector2 GRAVITY = Vector2.Zero;

	private World physicsWorld;
	private Box2DDebugRenderer debugRenderer;
	

	private Level level; 
	private float a = 0;
	
	private Rectangle  glViewport;
	float w, h;

	public GameScreen(EirGame game)
	{
		super( game );
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera( w, h );

		batch = new SpriteBatch();
		batch.setProjectionMatrix( camera.projection );
		batch.setTransformMatrix( camera.view );
		
		physicsWorld = new World( GRAVITY, true/* sleep */);

		debugRenderer = new Box2DDebugRenderer(true, true, true, true, true);
		
		LevelLoader loader = new LevelLoader();
		String levelName = loader.getLevelNames( "exodus" ).iterator().next();

		level = loader.readLevel( physicsWorld, levelName );
		level.init();
	}

	@Override
	public void render(float delta)
	{
		super.render( delta );
		GL10 gl = Gdx.graphics.getGL10();
		
		camera.rotate( 0.1f );
		camera.update();
		
		// TODO: those are copying matrice arrays, maybe there is a lighter way to do this
		batch.setProjectionMatrix( camera.projection );
		batch.setTransformMatrix( camera.view );
		
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		Body body;
		Sprite sprite;
		batch.begin();
		for(Asteroid asteroid : level.getAsteroids())
		{
			asteroid.getModel().render(batch);
		}
		batch.end();
		
		debugRenderer.render( physicsWorld, camera.combined );
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize( width, height );
	}

	@Override
	public void show()
	{
		super.show();
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
	}

}
