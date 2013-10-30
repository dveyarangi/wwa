package eir.world.unit;

import eir.world.unit.ai.Task;

/**
 * not sure what this one should be doing
 * @author Ni
 *
 */
public interface UnitBehavior <U extends Unit>
{
	public void update( float delta, Task task, U unit );
}
