package eir.world.environment;

import com.badlogic.gdx.math.Vector2;

/**
 * Generic anchor for units that require one.
 *
 * Anchor provides origin point and orientation, and may be dependent on other unit
 * spatial position
 * @author Fima
 *
 */
public interface Anchor
{

	Vector2 getPoint();

	float getAngle();

	Object getParent();

}
