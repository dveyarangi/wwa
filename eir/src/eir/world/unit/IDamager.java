package eir.world.unit;

import eir.world.environment.spatial.AABB;


public interface IDamager
{

	public Damage getDamage();

	public String getType();

	public Faction getFaction();

	public Unit getSource();

	public boolean dealsFriendlyDamage();

	public AABB getArea();

	public float getSize();
}
