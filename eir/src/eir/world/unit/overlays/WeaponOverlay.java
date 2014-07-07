package eir.world.unit.overlays;

import yarangi.math.Angles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.rendering.IRenderer;
import eir.world.environment.spatial.AABB;
import eir.world.unit.IOverlay;
import eir.world.unit.Unit;
import eir.world.unit.weapon.Weapon;

public class WeaponOverlay implements IOverlay <Unit>
{

	@Override
	public void draw( final Unit unit, final SpriteBatch batch )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void draw( final Unit unit, final IRenderer renderer )
	{
		if( unit.getWeapon() == null )
		{
			Debug.log( "Unit has no weapon:" + unit );
			return;
		}

		Weapon weapon = unit.getWeapon();

		ShapeRenderer shape = renderer.getShapeRenderer();


		float sensorRadius = weapon.getDef().getSensorRadius();

		GLCommon gl = Gdx.gl;
		gl.glEnable(GL20.GL_BLEND);

		shape.setColor( 0,1,0,0.5f );

		// weapon sensor range
		shape.begin(ShapeType.Circle);
		shape.circle(unit.cx(), unit.cy(), sensorRadius, 100);
		shape.end();

		Vector2 direction = weapon.getDirection();
		float angle = direction.angle();
		if(weapon.getDef().getAngularSpeed() > 0)
		{
			shape.begin(ShapeType.Line);
			shape.line(unit.cx(), unit.cy(),
				(float)(unit.cx() + sensorRadius * Math.cos( angle * Angles.TO_RAD )),
				(float)(unit.cy() + sensorRadius * Math.sin( angle * Angles.TO_RAD )));
			shape.end();
			shape.setColor( 0,1,0,0.1f );
			shape.begin(ShapeType.FilledTriangle);
			for(float a = 0; a < weapon.getDef().getMaxFireAngle(); a += 1)
			{
				shape.filledTriangle( unit.cx(), unit.cy(),
						(float)(unit.cx() + sensorRadius * Math.cos( (a+angle) * Angles.TO_RAD )),
						(float)(unit.cy() + sensorRadius * Math.sin( (a+angle) * Angles.TO_RAD )),
						(float)(unit.cx() + sensorRadius * Math.cos( (a+angle+1) * Angles.TO_RAD )),
						(float)(unit.cy() + sensorRadius * Math.sin( (a+angle+1) * Angles.TO_RAD )));
				shape.filledTriangle( unit.cx(), unit.cy(),
						(float)(unit.cx() + sensorRadius * Math.cos( (angle-a) * Angles.TO_RAD )),
						(float)(unit.cy() + sensorRadius * Math.sin( (angle-a) * Angles.TO_RAD )),
						(float)(unit.cx() + sensorRadius * Math.cos( (angle-a-1) * Angles.TO_RAD )),
						(float)(unit.cy() + sensorRadius * Math.sin( (angle-a-1) * Angles.TO_RAD )));
			}
			shape.end();
		}

		if(weapon.getTarget() != null)
		{
			shape.setColor( 0,1,0,0.5f );
			AABB aabb = weapon.getTarget().getArea();
			shape.begin(ShapeType.Line);
			shape.line( aabb.getMinX(), aabb.getMinY(), aabb.getMinX()+aabb.getHalfWidth()/2, aabb.getMinY() );
			shape.line( aabb.getMinX(), aabb.getMinY(), aabb.getMinX()                      , aabb.getMinY()+aabb.getHalfHeight()/2 );
			shape.line( aabb.getMinX(), aabb.getMaxY(), aabb.getMinX()+aabb.getHalfWidth()/2, aabb.getMaxY() );
			shape.line( aabb.getMinX(), aabb.getMaxY(), aabb.getMinX()                      , aabb.getMaxY()-aabb.getHalfHeight()/2 );
			shape.line( aabb.getMaxX(), aabb.getMaxY(), aabb.getMaxX()-aabb.getHalfWidth()/2, aabb.getMaxY() );
			shape.line( aabb.getMaxX(), aabb.getMaxY(), aabb.getMaxX()                      , aabb.getMaxY()-aabb.getHalfHeight()/2 );
			shape.line( aabb.getMaxX(), aabb.getMinY(), aabb.getMaxX()-aabb.getHalfWidth()/2, aabb.getMinY() );
			shape.line( aabb.getMaxX(), aabb.getMinY(), aabb.getMaxX()                      , aabb.getMinY()+aabb.getHalfHeight()/2 );
			shape.end();
		}
	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
