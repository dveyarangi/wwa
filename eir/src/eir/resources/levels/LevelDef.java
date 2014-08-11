package eir.resources.levels;

import java.util.ArrayList;
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

	private List <FactionDef> factions = new ArrayList <FactionDef> ();

	private List <IUnitDef> units = new ArrayList <IUnitDef> ();

	private Map <String, AnimationHandle> animations;

	public float getWidth() { return width;	}
	public void setWidth( final int width ) { this.width = width; }
	public float getHeight() { return height; }
	public void setHeight( final int height ) { this.height = height; }

	public Background getBackgroundDef() { return background; }
	public void setBackgroundDef( final Background background ) { this.background = background; }
	public List <FactionDef> getFactionDefs() { return factions; }
	public void setFactionDefs( final List<FactionDef> factions ) { this.factions = factions; }

	public List <AsteroidDef> getAsteroidDefs() { return asteroids; }
	public void setAsteroidDefs( final List<AsteroidDef> asteroids ) { this.asteroids = asteroids; }

	public List <IUnitDef> getUnitDefs() { return units; }

	public Map <String, AnimationHandle> getAnimations() { return animations; }

	public String getName() { return name; }
	public LevelInitialSettings getInitialSettings() { return initialSettings; }

	public void setInitialSettings(final LevelInitialSettings settings) { this.initialSettings = settings; }

}
