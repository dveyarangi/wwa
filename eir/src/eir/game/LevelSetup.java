package eir.game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eir.resources.GameFactory;
import eir.resources.LevelParameters;
import eir.resources.levels.LevelDef;
import eir.world.unit.UnitsFactory;

public class LevelSetup
{
	private GameFactory gameFactory;

	private UnitsFactory unitsFactory;

	private LevelDef levelDef;

	final ExecutorService pool = Executors.newFixedThreadPool(1);
	private float progress;
	private String state;


	public LevelSetup()
	{
		// game resources registry and loader
		gameFactory = new GameFactory();


		// preparing units factory:
		unitsFactory = new UnitsFactory( gameFactory );


		int width = 1536;
		int height = 1536;
		int factionsNum = 3;
		int asteroidNum = 15;

		LevelParameters levelParams = new LevelParameters( width, height, factionsNum, asteroidNum);
//		Level level = new LevelGenerator().generate( levelParams, unitsFactory );


		// loading level definitions from file:
		// this call also registers resource handles at the factory for future loading
		levelDef = gameFactory.readLevelDefs( "levels/level_exodus_01.dat", unitsFactory );
	}


	public GameFactory getGameFactory() { return gameFactory; }
	public UnitsFactory getUnitsFactory() { return unitsFactory; }
	public LevelDef getLevelDef() { return levelDef; }


	public synchronized void advance(final float amount, final String state)
	{
		progress += amount;
		this.state = state;
	}
	public float getProgress() { return progress; }
	public String getState() { return state; }


}
