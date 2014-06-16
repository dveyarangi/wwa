package eir.world;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.debug.Debug;
import eir.input.GameInputProcessor;
import eir.world.unit.IOverlay;
import eir.world.unit.Unit;
import eir.world.unit.overlays.IntegrityOverlay;
import gnu.trove.map.hash.TIntObjectHashMap;

public class LevelRenderer implements IRenderer
{
	private GameInputProcessor inputController;

	private Level level;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;



	private final List <Effect> effects;

	private TIntObjectHashMap <IOverlay> overlays;

	public static final int INTEGRITY_OID = 1;

	public LevelRenderer( final GameInputProcessor inputController, final Level level )
	{
		this.inputController = inputController;
		this.level = level;

		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();

		effects = new LinkedList <Effect> ();

		this.overlays = new TIntObjectHashMap <IOverlay> ();

		overlays.put( INTEGRITY_OID, new IntegrityOverlay() );
	}

	public void render(final float delta)
	{
		level.getBackground().draw( batch );


		// setting renderers to camera view:
		// TODO: those are copying matrix arrays, maybe there is a lighter way to do this
		// TODO: at least optimize to not set if camera has not moved
		batch.setProjectionMatrix( inputController.getCamera().projection );
		batch.setTransformMatrix( inputController.getCamera().view );

		shapeRenderer.setProjectionMatrix( inputController.getCamera().projection);
		shapeRenderer.setTransformMatrix( inputController.getCamera().view );



		batch.begin();

		// TODO: clipping?
		for(Asteroid asteroid : level.getAsteroids())
		{
			asteroid.draw( batch );
		}

		for( Web web : level.getWebs() )
		{
			web.draw( batch );
		}

		for(Unit unit : level.getUnits())
		{
			if(unit.isAlive())
			{
				unit.draw( this );
			}
		}

		for(Effect effect : effects)
		{
			effect.draw( batch );
		}


		Iterator <Effect> effectIt = effects.iterator();
		while(effectIt.hasNext())
		{
			Effect effect = effectIt.next();
			effect.update( delta );
			if(!effect.isAlive())
			{

				effectIt.remove();
				Effect.free( effect );
			}

		}


		batch.end();



		for(Unit unit : level.getUnits())
		{

			if(!unit.isAlive()) // adding death effect:
			{
				Effect hitEffect = unit.getDeathEffect();
				if(hitEffect != null)
				{
					effects.add( hitEffect );
				}
			}

			for(int oidx = 0; oidx < unit.getActiveOverlays().size(); oidx ++)
			{
				int oid = unit.getActiveOverlays().get( oidx );
				overlays.get( oid ).draw( unit, this );
			}
		}

		//////////////////////////////////////////////////////////////////
		// debug rendering
		inputController.draw( batch, shapeRenderer );
		Debug.debug.draw(batch, shapeRenderer);
	}


	@Override
	public void addEffect(final Effect effect)
	{
		effects.add( effect );
	}

	public void dispose()
	{

		// dispose sprite batch renderer:
		batch.dispose();

		// dispose shape renderer:
		shapeRenderer.dispose();	}

	@Override
	public SpriteBatch getSpriteBatch() { return batch; }

	@Override
	public ShapeRenderer getShapeRenderer() { return shapeRenderer;	}
}
