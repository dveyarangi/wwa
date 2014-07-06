package eir.resources.levels;

import java.util.List;
import java.util.Map;

import eir.resources.AnimationHandle;
import eir.resources.LevelInitialSettings;
import eir.world.environment.parallax.Background;

public class LevelDef
{
	private String name;

	private int width;
	private int height;

	private LevelInitialSettings initialSettings;

	private Background background;

	private List <AsteroidDef> asteroids;

	private List <FactionDef> factions;

	private List <IUnitDef> units;

	private Map <String, AnimationHandle> animations;

	public float getWidth() { return width;	}
	public float getHeight() { return height; }
	public Background getBackgroundDef() { return background; }
	public List <FactionDef> getFactionDefs() { return factions; }

	public List <AsteroidDef> getAsteroidDefs() { return asteroids; }

	public List <IUnitDef> getUnitDefs() { return units; }

	public Map <String, AnimationHandle> getAnimations() { return animations; }

	public String getName() { return name; }
	public LevelInitialSettings getInitialSettings() { return initialSettings; }

}
