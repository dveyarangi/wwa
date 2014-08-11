package eir.resources.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class FactionDef
{
	private int ownerId;

	private List <UnitDef> units;

	private Color color;

	private String controller;

	public FactionDef(final int factionId, final Color color, final String controller)
	{
		this.ownerId = factionId;
		this.color = color;
		this.controller = controller;
		this.units = new ArrayList <UnitDef> ();
	}

	public int getOwnerId() { return ownerId; }

	public List <UnitDef> getUnitDefs() { return units; }

	public Color getColor() { return color; }

	public String getControllerType() { return controller; }
}
