package eir.world.unit.ai;

import eir.world.unit.ant.Ant;

/**
 * not sure what this one should be doing
 * @author Ni
 *
 */
public interface TaskBehavior <A extends Ant>
{
	public void update( float delta, Task task, A ant );
}
