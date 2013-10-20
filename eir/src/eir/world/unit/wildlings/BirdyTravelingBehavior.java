/**
 * 
 */
package eir.world.unit.wildlings;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.world.unit.UnitBehavior;
import eir.world.unit.ai.Task;

/**
 * @author dveyarangi
 *
 */
public class BirdyTravelingBehavior implements UnitBehavior <Birdy>
{

	@Override
	public void update(float delta, Task task, Birdy unit)
	{
		Vector2 target = task.getOrder().getTargetNode().getPoint();
		
		if(unit.timeToPulse <= 0)
		{
			unit.timeToPulse = unit.pulseLength;
			unit.velocity.set( target ).sub( unit.getBody().getAnchor() ).nor().mul( unit.pulseStreght );
		}
		
		unit.getArea().getAnchor().add( unit.velocity.tmp().mul( delta ) );
		
		unit.velocity.mul( unit.pulseDecay * delta );
	}

}
