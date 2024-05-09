package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MainGame extends Game
{
	@Override
	public void create() 
	{
		setScreen(new MainGameScreen(this));	// Crea una pantalla y lanza el juego	
	}
}
