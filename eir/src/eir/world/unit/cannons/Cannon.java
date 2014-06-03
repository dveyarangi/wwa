package eir.world.unit.cannons;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.PolygonalModel;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.sensors.ISensor;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.TaskedUnit;
import eir.world.unit.Unit;
import eir.world.unit.weapon.HomingLauncher;
import eir.world.unit.weapon.IWeapon;
import eir.world.unit.weapon.TargetProvider;

public class Cannon extends TaskedUnit implements IDamager, TargetProvider
{

	public static final int SENSOR_RADIUS = 100;

	private ISensor sensor;

	private IWeapon weapon;

	private Damage impactDamage = new Damage( 10, 1, 0, 0 );

	private TargetingModule targetingModule;


	public Cannon()
	{
		super();
	}

	@Override
	public void postinit( final Level level )
	{
		this.targetingModule = TargetingModule.RANDOM_TARGETER( this );

		this.sensor = level.getEnvironment().createSensor( this, SENSOR_RADIUS );

		this.weapon = new HomingLauncher( this );
//		this.weapon = new Minigun( this );
		this.hull = new Hull(500f, 0f, new float [] {0f,0f,0f,0f});

		int navIdx = this.getAnchorNode().getDescriptor().getIndex();
		Asteroid asteroid = (Asteroid) getAnchorNode().getDescriptor().getObject();

		PolygonalModel model = asteroid.getModel();

		Vector2 surface = model.getNavNode( navIdx-1 ).getPoint().tmp()
					.sub( model.getNavNode( navIdx+1 )                  .getPoint() );


		this.angle = surface.rotate( 90 ).angle();
	}

	@Override
	public void update(final float delta)
	{
		List <ISpatialObject> units = sensor.sense( faction.getEnemyFilter() );

		weapon.update( delta );

		if(target == null || !getTarget().isAlive() || weapon.getTimeToReload() <= 0)
		{
			target = targetingModule.pickTarget( units );
		}


		super.update( delta );
	}
	private static Sprite sprite = GameFactory.createSprite( "anima//cannons//fan_canon_01.png" );

	@Override
	public void draw( final SpriteBatch batch )
	{
		Vector2 position = getBody().getAnchor();
		batch.draw( sprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionWidth(), angle);
	}


	@Override
	public void draw( final ShapeRenderer shape )
	{
		shape.begin(ShapeType.FilledCircle);
		shape.setColor(faction.color.r,faction.color.g,faction.color.b,0.5f);
		shape.filledCircle(getBody().getAnchor().x, getBody().getAnchor().y, getSize() / 2);
		shape.end();
		shape.begin(ShapeType.Circle);
		shape.setColor(faction.color.r,faction.color.g,faction.color.b,0.5f);
		shape.circle(getBody().getAnchor().x, getBody().getAnchor().y, SENSOR_RADIUS);
		shape.end();
	}

	@Override
	public float getSize()
	{
		return 10;
	}

	public IWeapon getWeapon() { return weapon; }

	@Override
	public Damage getDamage() { return impactDamage; }

	@Override
	public Unit getSource() { return this; }

	@Override
	public float getMaxSpeed() { return 0; }
}
