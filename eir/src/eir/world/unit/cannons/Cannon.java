package eir.world.unit.cannons;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.PolygonalModel;
import eir.world.Asteroid;
import eir.world.IRenderer;
import eir.world.Level;
import eir.world.LevelRenderer;
import eir.world.environment.sensors.ISensor;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.TaskedUnit;
import eir.world.unit.Unit;
import eir.world.unit.weapon.IWeapon;
import eir.world.unit.weapon.Minigun;

public class Cannon extends TaskedUnit implements IDamager
{

	public static final int SENSOR_RADIUS = 100;

	private ISensor sensor;

	private IWeapon weapon;

	private Damage impactDamage = new Damage( 10, 1, 0, 0 );

	private TargetProvider targetProvider;
	private TargetingModule targetingModule;


	public Cannon()
	{
		super();
	}

	@Override
	public void postinit( final Level level )
	{

		this.sensor = level.getEnvironment().createSensor( this, SENSOR_RADIUS );

		this.hull = new Hull(500f, 0f, new float [] {0f,0f,0f,0f});

		int navIdx = this.getAnchorNode().getDescriptor().getIndex();
		Asteroid asteroid = (Asteroid) getAnchorNode().getDescriptor().getObject();

		PolygonalModel model = asteroid.getModel();

		Vector2 surface = model.getNavNode( navIdx-1 ).getPoint().tmp()
					.sub( model.getNavNode( navIdx+1 )                  .getPoint() );


		this.angle = surface.rotate( 90 ).angle();

		///*
//		this.weapon = new HomingLauncher( this );
//		this.targetProvider = TargetProvider.RANDOM_TARGETER( this );

		/**/
		this.weapon = new Minigun( this );
		this.targetProvider = TargetProvider.CLOSEST_TARGETER( this );

 		/***/
		this.targetingModule = new LinearTargetingModule();
	}

	@Override
	public void update(final float delta)
	{

		super.update( delta );

		List <ISpatialObject> units = sensor.sense( faction.getEnemyFilter() );


		if(weapon.getBulletsInMagazine() == 0)
		{
			weapon.reload();
		}

//		if(target == null || !target.isAlive() )
//		{
//		}



		target = targetProvider.pickTarget( units );
		Vector2 targetDirection = targetingModule.getShootingDirection( target, this );
		if(targetDirection != null)
		{
			weapon.getTargetOrientation().set( targetDirection );
		}

		weapon.update( delta );

		if(! weapon.isOriented() )
			return;

		if( target != null)
		{

			if(weapon.getTimeToReload() > 0)
				return;
			weapon.fire( target );
		}
	}

	private static Sprite sprite = GameFactory.createSprite( "anima//cannons//cannon_hybrid_01.png" );

	@Override
	public void draw( final IRenderer renderer )
	{
		final SpriteBatch batch = renderer.getSpriteBatch();
		Vector2 position = getBody().getAnchor();
		batch.draw( sprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionWidth(), angle);
	}

	@Override
	public float getSize()
	{
		return 4;
	}

	@Override
	public IWeapon getWeapon() { return weapon; }

	@Override
	public Damage getDamage() { return impactDamage; }

	@Override
	public Unit getSource() { return this; }

	@Override
	public float getMaxSpeed() { return 0; }

	@Override
	protected void registerOverlays()
	{
		super.registerOverlays();
		addHoverOverlay( LevelRenderer.WEAPON_OID);
	}


}
