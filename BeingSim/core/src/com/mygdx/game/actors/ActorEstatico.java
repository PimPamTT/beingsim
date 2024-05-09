package com.mygdx.game.actors;

import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorEstatico extends Actor
{
	private Texture objeto;					// Objeto para guardar la textura
	private String name;				    // Guarda el nombre de la textura
	private Rectangle colision;				// Bounding Box para las colisiones
	private Rectangle caja; 				// rectangulo que contiene el área en el mapa donde está el objeto (Para detectar si hay otro objeto antes que se solape)
	private Random r = new Random();		// Obtenemos un objeto random para obtener números aleatorios		
		
	public ActorEstatico(String pficheroPng)
	{
		name = pficheroPng;									// Nombre del fichero
		objeto = new Texture(pficheroPng);					// Cargamos la textura
		setSize(objeto.getWidth(), objeto.getHeight());		// Actualizamos el tamaño
		colision = new Rectangle();							// Creamos la caja BoundingBox
		caja = new Rectangle();								// Creamos el rectangulo que contiene al objeto en el mapa
		setPosition();										// Actualizamos la posición
	}
	
	@Override
	public void act(float delta)							// Actualizar el movimiento. Al ser estático no se usa
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 		// Pintar el objeto en pantalla
	{
		// TODO Auto-generated method stub
		batch.draw(objeto, getX(), getY());
	}
	
	public boolean collision(Rectangle pRect)				// Esta función identifica si hay colisión con otro objeto
	{
		if(Intersector.overlaps(this.getRect(), pRect))
		{
			return true;
		}
		return false;
	}
	
	public void dispose()   	// Para borrar la textura en memoria
	{
		objeto.dispose();		
	}

	// Setter & Getters
	public Rectangle getRect() 	// Retorna el bounding box
	{			
		return colision;					
	}

	public void setRect(Rectangle colision)  // Setea el resctángulo de colision (bounding Box) en la posición actual
	{
		this.colision = colision;
	}

	public String getName()		// Devuelve el nombre del fichero con la textura
	{
		return name;
	}

	public void setPosition()   // Coloca el objeto en una zona del mapa 
	{
		float tamX = 48 - (objeto.getWidth()/32);	// Identifica el tamaño de los tiles de ancho y ajusta para que entre en pantalla
		float tamY = 30 - (objeto.getHeight()/32);  // Identifica el tamaño de los tiles de alto y ajusta para que entre en pantalla
		float varX = r.nextFloat(tamX*32);          // Obtenemos una posición aleatoria para el ancho del objeto que se ajuste a la pantalla
		float varY = r.nextFloat(tamY*32);			// Obtenemos una posición aleatoria para el alto del objeto que se ajuste a la pantalla
		setPosition(varX,varY);													// Posiciona el objeto en X, Y
		caja.set(getX(), getY(), getWidth(), getHeight());						// Setea el área que ocupa el objeto en la pantalla en la posición actual. 
		colision.set(getX()+32, getY()+32, getWidth()-32, getHeight()-32);		// Setea el bounding box de acuerdo con la posición actual
	}
	
	public Rectangle getPosition()	// Devuelve el rectángulo que nos indica el área que ocupa el objeto en pantalla
	{
		return caja;
	}
	
	
	
}