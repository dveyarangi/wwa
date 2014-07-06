package eir.rendering;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eir.debug.Debug;
import eir.input.GameInputProcessor;
import eir.resources.AnimationHandle;
import eir.resources.GameFactory;
import eir.world.Asteroid;
import eir.world.Effect;
import eir.world.Level;
import eir.world.Web;
import eir.world.unit.IOverlay;
import eir.world.unit.Unit;
import eir.world.unit.overlays.IntegrityOverlay;
import eir.world.unit.overlays.WeaponOverlay;
import gnu.trove.map.hash.TIntObjectHashMap;

public class LevelRenderer implements IRenderer
{
	private GameInputProcessor inputController;

	private Level level;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private final GameFactory gameFactory;

	private final List <Effect> effects;

	private TIntObjectHashMap <IOverlay> overlays;

	public static final int INTEGRITY_OID = 1;
	public static final int WEAPON_OID = 2;

	public LevelRenderer( final GameFactory gameFactory, final GameInputProcessor inputController, final Level level )
	{
		this.gameFactory = gameFactory;
		this.inputController = inputController;
		this.level = level;

		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();

		effects = new LinkedList <Effect> ();

		this.overlays = new TIntObjectHashMap <IOverlay> ();

		overlays.put( INTEGRITY_OID, new IntegrityOverlay() );
		overlays.put( WEAPON_OID, new WeaponOverlay() );
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
			asteroid.draw( this );
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
			if( unit.isHovered() )
			{
				for(int oidx = 0; oidx < unit.getHoverOverlays().size(); oidx ++)
				{
					int oid = unit.getHoverOverlays().get( oidx );
					overlays.get( oid ).draw( unit, this );
				}
			}
		}

		//////////////////////////////////////////////////////////////////
		// debug rendering
		inputController.draw( this );
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

	@Override
	public Animation getAnimation( final AnimationHandle handle )
	{
		return gameFactory.getAnimation( handle );
	}
}
