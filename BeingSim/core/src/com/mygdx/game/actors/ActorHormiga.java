package com.mygdx.game.actors;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorHormiga extends Actor
{	
	private TextureRegion hormiga;						// Txtura para mostrar solo la primera imagen
	private Texture textureHormiga;					    // Textura para la hormiga
	private Rectangle colision;							// Usamos un rectangulo para las colisiones
	private Random r = new Random();					// Crea números aleatorio
	private float velX , velY;							// Para ajustar las velocidades de los movimientos
	private String name; 								// Guardamos el nombre de la hormiga
			
	public ActorHormiga(String pName, float pX, float pY) 
	{
		textureHormiga = new Texture("CycleWalk1.png");					// Cargamos la textura de la hormiga
		hormiga = new TextureRegion(textureHormiga,0, 0, 32, 32);		// Cojemos el primer strip de la hormiga	
		setSize(hormiga.getRegionWidth(), hormiga.getRegionHeight());   // Obtenemos el tamaño del objeto hormiga
		this.name = pName;												// Guardamos su nombre
		colision = new Rectangle();								        // Creamos el area para el bounding box	
		setX(pX);														// Posicionamos el sprite en X
		setY(pY);														// Posicionamos el sprite en y
		movimiento();													// Movemos el sprite de forma aleatoria
	}
	
	public void movimiento()
	{
		int temporal1 = r.nextInt(3);		// Obtenemos el movimiento 1 aleatorio para x (0 quieto, 1 izquierda, 2 derecha)
		int temporal2 = r.nextInt(3);       // Obtenemos el movimiento 1 aleatorio para x (0 quieto, 1 abajo, 2 arriba)
		
		if(temporal1 == 0 && temporal2 == 0)   // Si salen los dos cero es que no se va a mover
		{
			while(true)							// Crea un bucle hasta que uno de los dos sea distinto de cero
			{
				temporal1 = r.nextInt(3);		
				temporal2 = r.nextInt(3); 
				
				if(temporal1 != 0 || temporal2 != 0)
				{
					break;
				}
			}
		}
		
		if(temporal1 == 0) velX = 0;			// No nos movemos horizontalmente
		if(temporal1 == 1) velX = -80;	        // Nos movemos a la izquierda
		if(temporal1 == 2) velX = 80;			// Nos movemos a la derecha
		if(temporal2 == 0) velY = 0;			// No nos movermos verticalmente
		if(temporal2 == 1) velY = -80;		    // Nos movemos hacia abajo
		if(temporal2 == 2) velY = 80;			// Nos movemos hacia arriba
	}
	
	@Override
	public void act(float delta) 
	{
		// TODO Auto-generated method stub
		setX(getX() + velX * delta);			// Empezamos a movernos de acuerdo con las velocidades aleatorias
		setY(getY() + velY * delta);			// Empezamos a movernos de acuerdo con las velocidades aleatorias
		
		colision.set(getX()+16, getY()+16, hormiga.getRegionWidth()-16, hormiga.getRegionHeight()-16);  // Obtenemos el nuevo bounding box
	
		if(getX() <= 0 || getX() >= 1536 - 32)  // Si toca en los laterales de la pantalla cambia de dirección
		{
			movimiento();
		}
		
		if(getY() <= 0 || getY() >= 960 - 32)
		{
			movimiento();
		}			
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		// TODO Auto-generated method stub
		batch.draw(hormiga, getX(), getY());    // Pinta la hormiga en pantalla
	}
	
	public boolean collision(Rectangle pRect)
	{
		if(Intersector.overlaps(this.getRect(), pRect))   // Identifica si la bounding box colisiona con otro objeto
		{
			return true;
		}
		return false;
	}
	
	//Setters and getters
	public Rectangle getRect() 				// devolvemos el bounding box
	{
		return colision;	
	}

	public void setRect(Rectangle rect)     // Seteamos el bounding box
	{
		this.colision = rect;
	}
	
	// Devolver el nombre 
	public String getName()
	{
		return name;
	}	
}