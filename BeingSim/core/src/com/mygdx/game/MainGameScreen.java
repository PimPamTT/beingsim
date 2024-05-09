package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.mygdx.game.actors.*;

public class MainGameScreen extends BaseScreen 
{
	private Texture bcground;							// Textura para el fondo
	private Random r = new Random();					// Crea números aleatorios	
	private ArrayList<ActorEstatico> listaObjectos;      // Creamos una lista de actores estáticos - Objetos que no se mueven
	private ArrayList<ActorHormiga> listaHormigas;		// Creamos una lista de actores hormigas
	private ActorEstatico hormiguero;					// Creamos un objeto hormiguero
	private Stage stage;								// Creamos un objeto Stage
	
	public void create () {
		
	}
	
	public MainGameScreen(MainGame game) 
	{
		super(game);
		bcground = new Texture("MainScreen.png");						// Cargamos la textura del fondo
		listaObjectos = new ArrayList<ActorEstatico>();                  // Creamos una lista de actoresEstáticos para guardar objetos de agua y setos 
		listaHormigas = new ArrayList<ActorHormiga>();    	            // Creamos una lista de actoresEstáticos para guardar hormigas 

	}
	
	
	private void comprobarColisiones()
	{
		// Revisamos cada hormiga con el hormiguero y con los objetos de pantalla
		for (int i=0; i < listaHormigas.size(); i++) 
		{
			// Revisamos si choca con hormiguero, solo muestra un mensaje
			if(listaHormigas.get(i).collision(hormiguero.getRect()))
			{
				//System.out.println("Colisión de la " + listaHormigas.get(i).getName() + " con hormiguero");
			}	
			// Ahora revisamos si la hormiga ha colisionado con alguno de los objetos estáticos (agua o seto) y cambia de dirección
			for (int j=0; j < listaObjectos.size(); j++)
			{
				if(listaHormigas.get(i).collision(listaObjectos.get(j).getRect()))
				{
					//System.out.println("Colisión de la " + listaHormigas.get(i).getName() + " con " + listaObjectos.get(j).getName());
					listaHormigas.get(i).movimiento();
				}	
			}	
			
			/*for (int g=0; g < listaHormigas.size(); g++)
			{
				if(i != g)
				{
					if(!listaHormigas.get(i).collision(listaHormigas.get(g).getRect()))
					{
						//System.out.println("Colisión de la " + listaHormigas.get(i).getName() + " con la " + listaHormigas.get(g).getName());
						listaHormigas.get(g).movimiento();
					}	
				}
			}*/
		}
	}
	
	@Override
	public void show() 
	{
		stage = new Stage();																			// Creamos un nuevo Stage
		//stage.setDebugAll(true);																		// Queremos mostrar su tamaño con un rectángulo verde
	
		crearActoresEstaticos("Agua", 4, r.nextInt(16)+5);	  											// Creamos los objetos de agua
		crearActoresEstaticos("Seto", 4, r.nextInt(16)+5);												// Creamos los objetos de seto
		anadirActoresEstaticos(stage);																	// Añadimos todos los objetos a la escena
		
		hormiguero = new ActorEstatico("Hormiguero1.png");												// Creamos el hormiguero en un lugar aleatorio
		while (!hayHuecoEnMapa(hormiguero.getPosition()))												// Revisamos si hay hueco en el mapa para el hormiguero
		{
			hormiguero.setPosition();																					// Sino, buscamos otra posición
		} 
		stage.addActor(hormiguero);																	    				// Los añadimos a la escena
		
		crearActoresHormigas("Hormiga", r.nextInt(10)+30, (float)hormiguero.getX()+16,(float)hormiguero.getY()+16);  	// Creamos hormigas
		anadirActoresHormigas(stage);																					// Las añadiamos a la escena
	}
	
	public boolean hayHuecoEnMapa(Rectangle pRect)													    				 
	{			
		for (int i=0; i < listaObjectos.size(); i++)													// Nos recorremos el array lista para ver si hay hueco para meter el objeto	
		{
			if (Intersector.overlaps(listaObjectos.get(i).getPosition(),pRect))						    // Vemos si colisiona con al menos un objeto
			{			
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public void hide() 
	{
		stage.dispose();			// Eliminamos stage
		bcground.dispose();			// Eliminamos el fondo
		//textureAnts.dispose();	// Eliminamos las texturas de la Hormiga
	}
	
	@Override
	public void render(float delta)
	{			
		// Gdx.gl.glClearColor(0.4f,0.5f,0.8f,1f);		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);			// Limpiamos el fondo
		stage.act(Gdx.graphics.getDeltaTime());				// Actualizamos todos los objetos de la escena
		
		comprobarColisiones();
		
		stage.getBatch().begin();							// Abrimos batch
		stage.getBatch().draw(bcground, 0, 0, 1536,960);	// Pintamos el fondo	
		stage.getBatch().end();							    // Cerramos el bacth
		
		stage.draw();										// Piintamos todos los objetos de la escena
	}	
	
	public void crearActoresEstaticos(String vNombre, int nObjetos, int nActores)
	{
		for (int i=0; i < nActores; i++ )												// Hacemos un bucle de nObjetos
		{
		    int aleatorio = r.nextInt(nObjetos)+1;          							// Creamos un objeto aleatorio
			String vFicheroPng = vNombre+aleatorio+".png";								// Construimos la cadena para el fichero png ("agua" + "1" + ".png"
			ActorEstatico actorTemporal = new ActorEstatico(vFicheroPng);		        // Creamos un actor en una posiciónAleatoria
			while (!hayHuecoEnMapa(actorTemporal.getPosition()))						// Revisamos si hay hueco en el mapa para el hormiguero
			{
				actorTemporal.setPosition();											// Sino, buscamos otra posición
			} 
			listaObjectos.add(actorTemporal);
		}
	}
	
	private void anadirActoresEstaticos(Stage pStage)
	{
		for (int i=0; i<listaObjectos.size(); i++ )			// Bucle para añadir a los actores objetos en el Stage
		{
			pStage.addActor(listaObjectos.get(i));
		}		
	}
	
	public void crearActoresHormigas(String vNombre, int nActores, float pX, float pY)
	{
		for (int i=0; i < nActores; i++ )												// Hacemos un bucle de nObjetos
		{
			String name= vNombre+i;										        		// Construimos la cadena para el nombre de la hormiga ("Homiga" + "1")
			ActorHormiga actorTemporal = new ActorHormiga(name, pX, pY);				// Creamos un actor hormiga en la posición que le hemos pasado
			listaHormigas.add(actorTemporal);
		}
	}
	private void anadirActoresHormigas(Stage pStage)
	{
		for (int i=0; i < listaHormigas.size(); i++ )			// Bucle para añadir a los actores  Hormigasen el Stage
		{
			pStage.addActor(listaHormigas.get(i));
		}		
	}
}