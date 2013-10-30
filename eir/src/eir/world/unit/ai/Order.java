package eir.world.unit.ai;

import eir.world.environment.nav.NavNode;
import eir.world.unit.Unit;

/**
 * general order given by user or ai controller
 * @author Ni
 *
 */
public abstract class Order
{
	private boolean isActive = true;
	
	private String unitType;
	
	private NavNode sourceNode;
	private NavNode targetNode;
	
	private Unit unit;

	private float priority;
	
	public Order(float priority, NavNode sourceNode, NavNode targetNode, Unit unit)
	{
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.unit = unit;
		this.priority = priority;
	}
	
	public abstract Task createTask(Scheduler scheduler);

	/**
	 * @return
	 */
	public NavNode getSourceNode() { return sourceNode;	}
	public NavNode getTargetNode() { return targetNode;	}
	public Unit getUnit() { return unit; }
	public void setSourceNode(NavNode node) { this.sourceNode = node; }
	public void setTargetNode(NavNode node) { this.targetNode = node; }

	public void setUnit(Unit unit) { this.unit = unit; }
	public void setActive(boolean isActive) { this.isActive = isActive; }
	
	public float getPriority() { return priority; }
	public String getUnitType() { return unitType; }

	public boolean isActive() { return isActive; }
}
