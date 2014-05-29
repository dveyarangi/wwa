package eir.world;

import java.util.List;

import eir.world.environment.parallax.Background;

public class LevelDef
{
	private String name;

	private int width;
	private int height;

	private Background background;

	private List <AsteroidDef> asteroids;

	private List <FactionDef> factions;

}
