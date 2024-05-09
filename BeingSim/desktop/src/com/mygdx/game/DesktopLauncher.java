package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher
{
	public static void main (String[] arg) 
	{
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1536, 960);							// Tamaño de la pantalla principal
		config.setResizable(false);  								// No queremos que se haga un resize
		config.setForegroundFPS(60);								// Trabajamos en 60 FPS
		// config.setTitle("BeingsSim");	                        // Ponemos el título
		new Lwjgl3Application(new MainGame(), config);				// Lanzamos la clase MainGame
	}
}