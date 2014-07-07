package eir.input;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.UnitsFactory;
import eir.world.unit.cannons.Cannon;
import eir.world.unit.cannons.CannonDef;
import eir.world.unit.weapon.MinigunDef;

/**
 * TODO: placeholder for building control mode;
 *
 * responds to nav node hovers
 *
 * @author Fima
 *
 */
public class BuildingControlMode implements IControlMode
{
	private static int CONTROLLING_FACTION_ID = 1;

	PickingSensor sensor = new PickingSensor(new IPickingFilter () {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof SurfaceNavNode;
		}

	});

	private final Level level;

	private SurfaceNavNode pickedNode;

	private Animation crosshair;

	private GameFactory gameFactory;


	private CannonDef cannonDef;


	BuildingControlMode(final GameFactory gameFactory, final Level level)
	{
		this.gameFactory = gameFactory;

		this.level = level;

		this.crosshair = gameFactory.getAnimation( GameFactory.CROSSHAIR_ANIM );

		this.cannonDef = new CannonDef( UnitsFactory.CANNON, CONTROLLING_FACTION_ID,
				5,
				null,
				GameFactory.EXPLOSION_04_ANIM,
				new MinigunDef(
						UnitsFactory.WEAPON,
						CONTROLLING_FACTION_ID, 5,
						GameFactory.CANNON_HYBRID_TXR,
						GameFactory.EXPLOSION_04_ANIM ));
	}

	@Override
	public PickingSensor getPickingSensor() { return sensor; }

	@Override
	public void touchUnit( final ISpatialObject pickedObject )
	{
		SurfaceNavNode node = (SurfaceNavNode) pickedObject;

		Cannon cannon = level.getUnitsFactory().getUnit( gameFactory, level,
				cannonDef,
				node
				);

		level.addUnit( cannon );
	}

	@Override
	public void render( final IRenderer renderer )
	{

		 if(pickedNode == null)
			 return;

		 SpriteBatch batch = renderer.getSpriteBatch();

		 Vector2 point = pickedNode.getPoint();
		 batch.begin();
		 TextureRegion crossHairregion = crosshair.getKeyFrame( 0, true );
		 batch.draw( crossHairregion,
				 point.x-crossHairregion.getRegionWidth()/2, point.y-crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
				 crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
				 5f/crossHairregion.getRegionWidth(),
				 5f/crossHairregion.getRegionWidth(), 0);
		 batch.end();
	}

	@Override
	public void objectPicked( final ISpatialObject pickedObject )
	{
		this.pickedNode = (SurfaceNavNode) pickedObject;
	}

	@Override
	public void objectUnpicked( final ISpatialObject pickedObject )
	{
//		assert pickedObject == pickedNode;
		pickedNode = null;
	}

	@Override
	public void reset()
	{
		pickedNode = null;
	}

}
