package eir.game;

import com.badlogic.gdx.Game;

import eir.game.screens.GameScreen;

/**
 * main game class.
 * @author Ni
 *
 */
public class EirGame extends Game
{
	@Override
	public void create()
	{
		setScreen(new GameScreen());
	}
	
}
