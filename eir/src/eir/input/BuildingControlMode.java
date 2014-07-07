package eir.input;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.GameFactory;
import eir.resources.levels.UnitDef;
import eir.world.Level;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;
import eir.world.unit.cannons.CannonDef;
import eir.world.unit.structure.SpawnerDef;
import eir.world.unit.weapon.HomingLauncherDef;
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
	private static int WILDLING_FACTION_ID = 3;

	PickingSensor sensor = new PickingSensor(new IPickingFilter () {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof SurfaceNavNode
				|| entity instanceof Unit;
		}

	});

	private final Level level;

	private ISpatialObject pickedObject;

	private Animation crosshair;

	private GameFactory gameFactory;


	private UnitDef [] defRobin;
	private int unitIdx;


	BuildingControlMode(final GameFactory gameFactory, final Level level)
	{
		this.gameFactory = gameFactory;

		this.level = level;

		this.crosshair = gameFactory.getAnimation( GameFactory.CROSSHAIR_ANIM );

		this.defRobin = new UnitDef [] {
				new CannonDef( UnitsFactory.CANNON, CONTROLLING_FACTION_ID,
					5,
					null,
					GameFactory.EXPLOSION_04_ANIM,
					new MinigunDef(
							UnitsFactory.WEAPON,
							CONTROLLING_FACTION_ID, 5,
							GameFactory.CANNON_HYBRID_TXR,
							GameFactory.EXPLOSION_04_ANIM, false ),
					true),

				new CannonDef( UnitsFactory.CANNON, CONTROLLING_FACTION_ID,
					5,
					null,
					GameFactory.EXPLOSION_04_ANIM,
					new HomingLauncherDef(
							UnitsFactory.WEAPON,
							CONTROLLING_FACTION_ID, 5,
							GameFactory.CANNON_FAN_TXR,
							GameFactory.EXPLOSION_04_ANIM, false ),
					true),
				new SpawnerDef( UnitsFactory.SPAWNER, WILDLING_FACTION_ID,
						10,
						GameFactory.SPAWNER_TXR,
						GameFactory.EXPLOSION_04_ANIM,
						true,
						new UnitDef(
								UnitsFactory.BIDRY,
								WILDLING_FACTION_ID, 10,
							 	GameFactory.BIRDY_TXR,
								GameFactory.EXPLOSION_02_ANIM, true ),
						50, 0.2f)
/*						{
							"type" : "spawner",
							"faction" : 3,
							"size" : 10,
							"spriteTexture" : "anima//structures//spawner01.png",
							"anchor" : { "asteroid" : "red_ass", "navIdx" : 10 },
							"isPickable" : true,
							"spawnedUnit" : {
								"type" : "birdy",
								"faction" : 3,
								"size" : 10,
								"spriteTexture" : "anima//gears//birdy_02.png",
								"deathAnimation" : { "atlasId" : "anima//effects//explosion//explosion02.atlas", "regionId" : "explosion02" },
								"isPickable" : true
							},
							"maxUnits" : 50,
							"spawnInterval" : 0.2f
						}	*/
		};
	}

	@Override
	public PickingSensor getPickingSensor() { return sensor; }

	@Override
	public void touchUnit( final ISpatialObject pickedObject )
	{
		if(pickedObject instanceof SurfaceNavNode)
		{
			SurfaceNavNode node = (SurfaceNavNode) pickedObject;
			Unit unit = level.getUnitsFactory().getUnit( gameFactory, level,
					defRobin[unitIdx],
					node
					);

			level.addUnit( unit );
		}
		else
		if(pickedObject instanceof Unit)
		{
			Unit unit = (Unit) pickedObject;
			unit.setDead();
		}
	}

	@Override
	public void render( final IRenderer renderer )
	{

		 if(pickedObject == null)
			 return;

		 if(pickedObject instanceof SurfaceNavNode)
		 {
			 SurfaceNavNode pickedNode = (SurfaceNavNode) pickedObject;
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
	}

	@Override
	public ISpatialObject objectPicked( final List <ISpatialObject> objects )
	{
		pickedObject = null;
		for(ISpatialObject o : objects)
		{
			if(o instanceof SurfaceNavNode)
			{
				pickedObject = o;
			}
			if(o instanceof Unit)
			{
				Unit pickedUnit = (Unit) o;
				if(!pickedUnit.getDef().isPickable())
				{
					continue;
				}

				pickedUnit.setIsHovered( true );
				pickedObject = o;
				break;
			}
		}

		return pickedObject;
	}

	@Override
	public void objectUnpicked( final ISpatialObject pickedObject )
	{
//		assert pickedObject == pickedNode;
		if(pickedObject instanceof Unit)
		{
			Unit pickedUnit = (Unit) pickedObject;
			pickedUnit.setIsHovered( false );
		}

		this.pickedObject = null;
	}

	@Override
	public void reset()
	{
		pickedObject = null;
	}

	@Override
	public void keyDown( final int keycode )
	{
		switch(keycode)
		{

		case Input.Keys.N:
			unitIdx ++;
			if(unitIdx >= defRobin.length)
			{
				unitIdx = 0;
			}

			break;
		}
	}

}
