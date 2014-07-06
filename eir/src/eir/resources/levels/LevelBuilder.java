package eir.resources.levels;

import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.unit.UnitsFactory;

/**
 * Builds level from level definitions
 * @author Fima
 *
 */
public class LevelBuilder
{
	private GameFactory gameFactory;
	private final UnitsFactory unitsFactory;
	public LevelBuilder(final GameFactory gameFactory, final UnitsFactory unitsFactory)
	{
		this.gameFactory = gameFactory;
		this.unitsFactory = unitsFactory;
	}

	public Level build(final LevelDef levelDef)
	{
		Level level = new Level( unitsFactory );


		level.init( levelDef, gameFactory );

		return level;

	}
}
