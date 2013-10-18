package eir.world.unit.ai;

import eir.world.unit.Unit;
import eir.world.unit.ant.Ant;

/**
 * not sure what this one should be doing
 * @author Ni
 *
 */
public interface TaskBehavior <U extends Unit>
{
	public void update( float delta, Task task, U ant );
}
