package com.nnf.eir;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import eir.game.EirGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "eir";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 512;
		
		new LwjglApplication(new EirGame(), cfg);
	}
}
