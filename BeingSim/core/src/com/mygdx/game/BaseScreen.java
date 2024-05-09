package com.mygdx.game;

import com.badlogic.gdx.Screen;

// Clase BaseScreen --> Es igual que Screen pero gestionamos un objeto de la clase MainGame para poder lanzar otras pantallas

public abstract class BaseScreen implements Screen
{
	protected MainGame game;			// Objeto de la clase MainGame
	
	public BaseScreen (MainGame game)
	{
		this.game = game;				// Lo relacionamos con la clase principal para abrir otras pantalla d ejuego
	}	
	
	@Override
	public void show() 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void render(float delta) 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub		
	}
}