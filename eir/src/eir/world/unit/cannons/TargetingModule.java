package eir.world.unit.cannons;

import com.badlogic.gdx.math.Vector2;

import eir.world.environment.spatial.ISpatialObject;

public abstract class TargetingModule
{
	public abstract Vector2 getShootingDirection( ISpatialObject target, Cannon cannon );
}
