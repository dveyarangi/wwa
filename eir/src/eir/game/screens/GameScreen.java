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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import eir.game.Asteroid;
import eir.game.EirGame;
import eir.game.GameFactory;
import eir.resources.BodyLoader;

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

	private List<Asteroid> asteroids = new LinkedList<Asteroid>();

	private World physicsWorld = new World( Vector2.Zero, true/* sleep */);
	private Box2DDebugRenderer debugRenderer;

	public GameScreen(EirGame game)
	{
		super( game );
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera( 1, h / w );
		batch = new SpriteBatch();

		Asteroid asteroid = GameFactory.createAsteroidHead( 200, 200, physicsWorld, "asteroid_head_01" );
		asteroids.add( asteroid );

		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void render(float delta)
	{
		super.render( delta );
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		Body body;
		Sprite sprite;
		batch.begin();
		for(Asteroid asteroid : asteroids)
		{
			body = asteroid.getBody();
			sprite = asteroid.getSprite();
			sprite.setPosition(body.getPosition().x, body.getPosition().y );
			sprite.draw( batch );
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
