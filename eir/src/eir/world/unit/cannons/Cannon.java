package eir.world.unit.cannons;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.sensors.ISensor;
import eir.world.unit.Hull;
import eir.world.unit.Unit;
import eir.world.unit.structure.Spawner;
import eir.world.unit.weapon.HomingLauncher;
import eir.world.unit.weapon.IWeapon;

public class Cannon extends Unit
{

	public static final int SENSOR_RADIUS = 100;

	private ISensor sensor;

	private IWeapon weapon;

	private Unit target;


	public Cannon()
	{
		super();
	}

	@Override
	public void postinit( final Level level )
	{


		this.sensor = level.getEnvironment().createSensor( this.getArea().getAnchor(), SENSOR_RADIUS );

		this.weapon = new HomingLauncher( this );
//		this.weapon = new Minigun( this );
		this.hull = new Hull(5, 0, new double [] {0,0,0,0});
	}

	@Override
	public void update(final float delta)
	{
		target = null;

		List <Unit> units = sensor.sense();

		weapon.update( delta );

		for(Unit unit : units)
		{
			if(getFaction().isEnemy( unit ) && !(unit instanceof Spawner))
			{
				target = unit;
				break;
			}
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
		// TODO Auto-generated method stub
		return 15;
	}

	/**
	 * Current target, null if none sensed
	 * @return
	 */
	public Unit getTarget() { return target; }
	public IWeapon getWeapon() { return weapon; }
}
