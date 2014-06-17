package eir.input;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.IRenderer;
import eir.world.Level;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.UnitsFactory;
import eir.world.unit.cannons.Cannon;

public class BuildingControlMode implements IControlMode
{


	PickingSensor sensor = new PickingSensor(new IPickingFilter () {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof SurfaceNavNode;
		}

	});

	private static int crosshairId = GameFactory.registerAnimation("anima//ui//crosshair01.atlas", "crosshair");
	private static Animation crosshair = GameFactory.getAnimation( crosshairId );

	private final Level level;

	private SurfaceNavNode pickedNode;

	BuildingControlMode(final Level level)
	{
		this.level = level;
	}

	@Override
	public PickingSensor getPickingSensor() { return sensor; }

	@Override
	public void touchUnit( final ISpatialObject pickedObject )
	{
		SurfaceNavNode node = (SurfaceNavNode) pickedObject;

		Cannon cannon = level.getUnitsFactory().getUnit(
				UnitsFactory.CANNON,
				node,
				level.getFactions()[0] );

		level.addUnit( cannon );

		cannon.postinit( level );

//			NavNode sourceNode = level.getControlledUnit().anchor;
//			NavNode targetNode = pickingSensor.getNode();

//			level.toggleWeb((SurfaceNavNode)sourceNode, (SurfaceNavNode)targetNode);

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
