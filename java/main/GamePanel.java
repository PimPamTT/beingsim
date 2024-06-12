package main;

import ai.PathFinder;
import db.DatabaseManager;
//import data.SaveLoad;
//import entity.Player;
//import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
//import tile_interactive.InteractiveTile;

import javax.swing.JPanel;

import entity.Entity;
import entity.Timer;
//import monster.MON_GreenSlime;
import environment.EnvironmentManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.Comparator;
import java.util.Random;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable
{	
	public boolean instructions = true;
	
	public int behaviorsSelectedGS = 0;
	public int behaviorsSelectedRS = 0;
	
	public boolean reviewGameOver = false;							// vamos a revisar el gameover solo si se han creado al menos un monstruo de cada especie
	public boolean pierdes = false;
	
	public Random rnum = new Random();
	int numEntity = rnum.nextInt(20) + 50;
	int numObject = rnum.nextInt(20) + 50;
	
	public DatabaseManager dbManager;								// Lo utilizamos para acceder a la base de datos
	
    // CONFIGURACIÓN DE PANTALLA
    final int originalTileSize = 16; 								// 16*16  tile. default
    final int scale = 3; 											// 16*3 scale

    public final int tileSize = originalTileSize * scale; 			// 48*48 tile // los hacemos public por lo usamos en la clase Player
    public final int maxScreenCol = 20; 							// 4:3 
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  		//48*20 = 960 pixels
    public final int screenHeight = tileSize * maxScreenRow;  		//48*12 = 576 pixels  // TAMAÑO DE LA PANTALLA

    // CONFIGURACIÓN DEL MUNDO
    public int maxWorldCol;											// Máximo número de columnas del mundo
    public int maxWorldRow;			
    public final int worldWidth = tileSize + maxWorldCol;
    public final int worldHeight = tileSize + maxWorldRow;
    
    // Máximo número de filas del mundo
    public final int maxMap = 10;									// Máximo número de mapas que puede tener el juego (10)
    public int currentMap = 0;										// Partimos del mapa 0

    // PARA PANTALLA COMPLETA
    int screenWidth2 = screenWidth;									// Guardamos la resolución para pantalla completa
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;										// Usa una pantalla temporal
    Graphics2D g2;													// Usamos un objeto graphics2D para hacer todas las impresiones
    public boolean fullScreenOn = false;							// Por defecto partimos de que no está seleccionada la pantalla completa

    // FPS
    public int FPS = 60;											// Marcamos 60 frames como obejtivo para el renderizado del juego

    // SISTEMA
    public TileManager tileM = new TileManager(this);				// Creamos un objeto de TileManager
    public Entity camera = new Entity(this); 						// Creamos una camera
      
    public Timer timer = new Timer(240);
    
    public KeyHandler keyH = new KeyHandler(this);					// Creamos un objeto para manejar el teclado  
    Sound music = new Sound(); 										// Creamos 2 objetos diferentes para efectos de Sonido y Música. Es mejor tener dos objetos distintos
    Sound se = new Sound();											// Objeto para efectos de sonido
    public CollisionChecker cChecker = new CollisionChecker(this);	// Creamos un objeto colisionador
    public AssetSetter  aSetter = new AssetSetter(this);			// Creamos un objeto que añade objetos en el mundo
    public UI ui = new UI(this);									// Creamos un objeto de tipo UI
    Map map = new Map(this);									    // Creamos un objeto de tipo mapa
    Thread gameThread;											    // Creamos un objeto de tipo hilo
    EnvironmentManager eManager = new EnvironmentManager(this);		// Creamos un objeto de tipo entorno

    // ENTIDADES Y OBJETOS
    public Entity obj[][] = new Entity[maxMap][numObject]; 						// Mostramos 10 objetos a la vez
    public Entity monster[][] = new Entity[maxMap][numEntity];					// Mostramos 10 monstruos
    public int totalMonstersCreated = 0;									    // Variable para identificar los monstruos creados 		
    ArrayList<Entity> entityList = new ArrayList<>();						// Generamos un array de cameraes
    
    // GENERAMOS ESTADOS PARA EL JUEGO
    public int gameState;						// Variable que guarda el estado del juego
    public final int titleState = 0;			// Pantalla inicial (Título)
    public final int playState = 1;				// Pantalla de juego
    public final int spectatorState = 2;		// Pantalla de juego
    public final int pauseState = 3;			// Juego en pausa
    public final int mapState = 4;				// Pantalla mostrando el mapa
    public final int endState = 5;				// Juego terminado

    public int currentIndex = 0;
    boolean perm = true;
    
    public GamePanel(DatabaseManager dbManager) // constructor
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); 		// Tamaño del panel
        this.setBackground(Color.black);										// Fondo del panel de color negro
        this.setDoubleBuffered(true); 											// mejorar el rendimiento de renderizado del juego
        this.addKeyListener(keyH);												// Lanzamos el keylistener
        this.setFocusable(true);												// Activamos que tenga el foco
        this.dbManager = dbManager;
             
        camera.worldX = 20 * tileSize;
        camera.worldY = 20 * tileSize;
        camera.screenX = screenWidth/2 - (tileSize/2);
        camera.screenY = screenHeight/2- (tileSize/2);
    }
   
    public void setupGame()					// Se llama a esta función desde la clse main
    {
    	aSetter.setObject();				// Cargamos los objetos en el array correspondiente
    	eManager.setup();					
    	gameState = titleState;				// Partimnos de la pantalla de título
    	
        // FOR FULLSCREEN
        tempScreen = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB); 		// instanciamos la pantalla temporal (de color negro)
        g2 = (Graphics2D) tempScreen.getGraphics(); 												// g2 apunta al tempScreen. g2 dibujará en esta tempScreen la imagen almacenada en buffer.
    }

	public void startGameThread()		// Función para lanzar el Game Loop				
    {
        gameThread = new Thread(this);			// Creamos un hilo
        gameThread.start(); 					// Ejecuta la función run
    }

    @Override
    public void run()
    {
        double drawInterval = 1000000000/FPS;	// Sacamos lo que tarda un frame en nanosegundos
        double delta = 0;						// El delsta es cero
        long lastTime = System.nanoTime();		// Nos traemos el timpo actual en nanosegundos
        long currentTime;						// Variable para actualizar el timpo

        while(gameThread != null)				// Loop del juego
        {
            currentTime = System.nanoTime();						// Cogemos el timpo actual
            delta += (currentTime - lastTime) / drawInterval;		// Identificamos el delta e el que estamos 
            lastTime = currentTime;									// Guardamos este tiempo en la variable lastTime
            if(delta >= 1)											// Si hemos completado un frame 
            {	
                update();											// Actualizamos los personajes
                
                /*repaint(); comentado para pantalla completa*/
                drawToTempScreen(); 								// PARA LA PANTALLA COMPLETA - Dibujar todo en la imagen almacenada
                drawToScreen();     								// PARA LA PANTALLA COMPLETA - Dibujar la imagen almacenada en la memoria intermedia en la pantalla
                delta--;											// Ponemos delta a cero;
            }
        }
    }

    public void update()
    {        	
    	if(gameState == playState)
    	{
        	timer.update();
        	
        	if(timer.isTriggered())
        	{
        		updateMonster();
        	}
        	
    		if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.spacePressed == true)
            {
    	        if(keyH.upPressed == true)
    	        {
    	        	camera.worldY -= 4;
    	        }
    	        else if(keyH.downPressed == true)
    	        {                                                                 
    	        	camera.worldY += 4;                                        
    	        }                                                                 
    	        else if(keyH.leftPressed == true)                                 
    	        {
    	        	 camera.worldX -= 4;
    	        }
    	        else if(keyH.rightPressed == true)
    	        {
    	        	 camera.worldX += 4;
    	        }
    	        else if(keyH.spacePressed == true)
    	        {
    	        	keyH.spacePressed = false;
    	        	
    	        	for(int i = 0; i < monster.length; i++)
    	        	{
    	        		if(monster[currentMap][i].active == true)
    	        		{
    	        			gameState = spectatorState;
    	    	        	monster[currentMap][i].cameraSelected = true;
    	    	        	currentIndex = i;
    	    	        	break;
    	        		}
    	        	}    
    	        }
            } 
    		
    		// GameOver
        	if (reviewGameOver == false)
        	{
        		if (isMonsterActive("Green Slime") && isMonsterActive("Red Slime"))
        		{
        			reviewGameOver = true;
        		}
        	}
        	else
        	{
        		if (isExtinted("Green Slime"))
        		{
        			//System.out.println("Green Slime extinguidos");
        			pierdes = true;
        			stopMusic();
        			playSE(2);
        			gameState = endState;        			
        		}
        		
        		if (isExtinted("Red Slime"))
        		{
        			//System.out.println("Red Slime extinguidos");
        			stopMusic();
        			playSE(6);
        			gameState = endState;
        		}
        	}
    		
    		moveEntity();
    		eManager.update();
    	}   	
    	
    	// Controlar que ya no quedan más bichos activos para cerrar la aplicación    	
    	if(gameState == spectatorState)
    	{
        	timer.update();
        	
        	if(timer.isTriggered())
        	{
        		updateMonster();
        	}
        	
    		if(keyH.qPressed == true || keyH.ePressed == true || keyH.spacePressed == true)
            {
    	        if(keyH.qPressed == true)												// Miramos si la tecla q está pulsada
    	        {
    	        	keyH.qPressed = false;												// Desactivamos la detección de la tecla Q
    	        	
    	        	monster[currentMap][currentIndex].cameraSelected = false;
    	        	
    	        	if(currentIndex == 0)												// Si estamos en la posición 0
    	        	{
    	        		currentIndex = totalMonstersCreated-1;							// Nos ponemos al final de los monstruos creados    	        		
    	        	}
    	        	else
    	        	{
    	        		currentIndex--;													// si no restamos uno
    	        	}   
    	        	
    	        	if (monster[currentMap][currentIndex].active == false) 
        			{
        				do
	        			{
        					if(currentIndex == 0)												// Si estamos en la posición 0
            	        	{
            	        		currentIndex = totalMonstersCreated-1;							// Nos ponemos al final de los monstruos creados    	        		
            	        	}
            	        	else
            	        	{
            	        		currentIndex--;													// si no restamos uno
            	        	}   
	        			}	        			
	        			while(monster[currentMap][currentIndex].active == false);
        			}
    	        	
    	        	monster[currentMap][currentIndex].cameraSelected = true;
    	        }
    	        else if(keyH.ePressed == true)
    	        {             
    	        	keyH.ePressed = false;
    	        	
    	        	monster[currentMap][currentIndex].cameraSelected = false;
    	        	
    	        	if(currentIndex == totalMonstersCreated-1)
    	        	{
    	        		currentIndex = 0;
    	        	}
    	        	else
    	        	{
    	        		currentIndex++;
    	        	}                  
	        		
    	        	if (monster[currentMap][currentIndex].active == false) 
        			{
        				do
	        			{
        					currentIndex++;	 
        					if(currentIndex == totalMonstersCreated-1)
            	        	{
            	        		currentIndex = 0;
            	        	}        					 
	        			}	        			
	        			while(monster[currentMap][currentIndex].active == false);
        			}   
    	        	monster[currentMap][currentIndex].cameraSelected = true;
    	        }  
    	        else if(keyH.spacePressed == true)
    	        {
    	        	 gameState = playState;
    	        	 keyH.spacePressed = false;
    	        }
            } 
    		
    		// GameOver
        	if (reviewGameOver == false)
        	{
        		if (isMonsterActive("Green Slime") && isMonsterActive("Red Slime"))
        		{
        			reviewGameOver = true;
        		}
        	}
        	else
        	{
        		if (isExtinted("Green Slime"))
        		{
        			//System.out.println("Green Slime extinguidos");
        			pierdes = true;
        			stopMusic();
        			playSE(2);
        			gameState = endState;
        		}
        		
        		if (isExtinted("Red Slime"))
        		{
        			//System.out.println("Red Slime extinguidos");
        			stopMusic();
        			playSE(6);
        			gameState = endState;
        		}
        	}
    		
    		moveEntity();
    		eManager.update();
    	} 

        if(gameState == pauseState)									// Si es pause no hacemos nada
        {
            // nothing, just pause screen
        }
        
        if(gameState == endState)									// Si es pause no hacemos nada
        {
            // nothing, just pause screen
        }
    }
    
    // PARA PANTALLA COMPLETA (PRIMERO DIBUJAR EN PANTALLA TEMPORAL EN LUGAR DE JPANEL)
    public void drawToTempScreen()
    {
        // SI ESTAMOS EN LA PANTALLA DE TITULO
        if(gameState == titleState)
        {
            ui.draw(g2);											// Dibujamos el UI
        }
        // PANTALLA DE MAPA
        else if(gameState == mapState)								// Si estamos en la pantalla del mapa
        {	
            map.drawFullMapScreen(g2);								// Pintamos el mapa a pantalla completa
        }
        // PARA EL RESTO
        else
        {
            // TILE
            tileM.draw(g2);											// Dibujamos el mapa

            // AÑADIMOS LOS OBJETOS
            for(int i = 0; i < obj[1].length; i++)
            {
                if(obj[currentMap][i] != null)
                {
                    entityList.add(obj[currentMap][i]);
                }
            }

            // AÑADIMOS LOS MONSTERS
            for(int i = 0; i < monster[1].length; i++)
            {
                if(monster[currentMap][i] != null && monster[currentMap][i].active == true)
                {
                	entityList.add(monster[currentMap][i]);
                    
                    if(gameState == spectatorState)
                    {
                    	if (i == currentIndex) {
                    		camera.worldX = monster[currentMap][currentIndex].worldX;
                            camera.worldY = monster[currentMap][currentIndex].worldY;
                    	}                    	                 	
                    }
                }
            }

            // ORDENAMOS LA ENTITY LIST 
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);   // result returns : (x=y : 0, x>y : >0, x<y : <0)
                    return result;
                }
            });

            // DIBUJAR LAS ENTIDADES
            for(int i = 0; i < entityList.size(); i++)
            {
                entityList.get(i).draw(g2);
            }

            // LIMPIAMOS LA ENTITY LIST
            entityList.clear();
            
            //ENVIRONMENT
            if(gameState == playState || gameState == spectatorState)
        	{
            	eManager.draw(g2);
        	}
            // PINTAMOS MINI MAP
            map.drawMiniMap(g2);

            // PINTAMOS UI
            ui.draw(g2);                       
        }
    }
    
    public void drawToScreen()												// Función para pasar la pantalla 
    {
        Graphics g = getGraphics();											// Nos traemos el Gráfico actual del objetoi
        g.drawImage(tempScreen, 0, 0,screenWidth2,screenHeight2,null);		// Cargamos la pantalla temporal en el objeto gráfico

        g.dispose();														// Lo pintamos. 
    }
    
    public void playMusic(int i)		// Función para para que se ponga la música
    {
        music.setFile(i);				// Cargamos el fichero
        music.play();					// Hacemos play
        music.loop();				    // Lo ponemos en modo repetición
    }
    
    public void stopMusic()				
    {
        music.stop();					// Paramos la música
    }
    
    public void playSE(int i) 			// Función para los effectos de sonido, no necesitan repertirse
    {
        se.setFile(i);					// Cargamos el fichero
        se.play();						// lo hacemos sonar
    }
    
    public void moveEntity()
    {
    	// MONSTER
        for(int i = 0; i < monster[1].length; i++)			// Recorremos todos los monstruos
        {
            if(monster[currentMap][i] != null && monster[currentMap][i].active == true)					// Si el monstruo del mapa actual no es null 
            {
                if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false)		// Si está vivo y no está muriendo
                {
                    monster[currentMap][i].update();		// Lo actualizamos
                }
                if(monster[currentMap][i].alive == false)	// Si esta muerto
                {
                    monster[currentMap][i] = null;			// lo pone a null
                }
            }
        }
    }
    
    public void updateMonster()
    {
    	if(perm)
    	{
	    	for(int i = 0; i < monster[1].length; i++)			// Recorremos todos los monstruos
	        {
	    		if(monster[currentMap][i+1] == null)
	    		{
	    			perm = false;
	    		}
	    		
	    		if(monster[currentMap][i].active == false && monster[currentMap][i].dead == false)
	    		{
	    			monster[currentMap][i].active = true;
	    			break;
	    		}	    		
	        }
    	}
    }
    
    public void restartDatabase() throws SQLException
    {
    	dbManager.closeConnection();
    	
    	System.out.println("Base de datos cerrada.");
    	
    	dbManager = new DatabaseManager("jdbc:sqlite::memory:");

        dbManager.createTables();
        
        String[] dietColumns = {"name"};
        Object[] dietValues = {"carnivore"};
        dbManager.insert("Diet", dietColumns, dietValues);            

        dietValues = new Object[] {"herbivore"};
        dbManager.insert("Diet", dietColumns, dietValues);
        
        String[] behaviorsColumns = {"name"};
        Object[] behaviorsValues = {"aggresive"}; 
        dbManager.insert("Behaviors", behaviorsColumns, behaviorsValues);

        behaviorsValues = new Object[] {"calm"};
        dbManager.insert("Behaviors", behaviorsColumns, behaviorsValues);
        
        behaviorsValues = new Object[] {"smart"};
        dbManager.insert("Behaviors", behaviorsColumns, behaviorsValues);
        
        behaviorsValues = new Object[] {"dumb"};
        dbManager.insert("Behaviors", behaviorsColumns, behaviorsValues);
        
        behaviorsValues = new Object[] {"farsighted"};
        dbManager.insert("Behaviors", behaviorsColumns, behaviorsValues);
        
        behaviorsValues = new Object[] {"careless"};
        dbManager.insert("Behaviors", behaviorsColumns, behaviorsValues);        	
        
        System.out.println("Base de datos y tablas creadas con éxito.");
        
        keyH.form1 = new Form();
        keyH.form2 = new Form();
    }  
    
    public boolean isExtinted(String especie)
    {    	
    	for(int i = 0; i < monster[1].length; i++)			// Recorremos todos los monstruos
        {    		
    		if(monster[currentMap][i].name.equals(especie) && monster[currentMap][i].active == true)
    		{
    			return false;
    		}
    		
    		if(monster[currentMap][i+1] == null)
    		{
    			break;
    		}
        }
    	
    	return true;
    }
    
    public boolean isMonsterActive(String especie)
    {    	
    	for(int i = 0; i < monster[1].length; i++)			// Recorremos todos los monstruos
        {    		
    		if(monster[currentMap][i].name.equals(especie) && monster[currentMap][i].active == true)
    		{
    			return true;
    		}
    		
    		if(monster[currentMap][i+1] == null)
    		{
    			break;
    		}
        }
    	
    	return false;
    } 
    
    public void setBehaviors()
    {
    	// Green Slime
    	if(keyH.form1.behaviors.contains("aggresive"))
    	{
    		behaviorsSelectedGS++;
    	}
    	if(keyH.form1.behaviors.contains("calm"))
    	{
    		behaviorsSelectedGS++;
    	}
    	if(keyH.form1.behaviors.contains("smart"))
    	{
    		behaviorsSelectedGS++;
    	}
    	if(keyH.form1.behaviors.contains("dumb"))
    	{
    		behaviorsSelectedGS++;
    	}
    	if(keyH.form1.behaviors.contains("farsighted"))
    	{
    		behaviorsSelectedGS++;
    	}
    	if(keyH.form1.behaviors.contains("careless"))
    	{
    		behaviorsSelectedGS++;
    	}
    	
    	// Red Slime
    	if(keyH.form2.behaviors.contains("aggresive"))
    	{
    		behaviorsSelectedRS++;
    	}
    	if(keyH.form2.behaviors.contains("calm"))
    	{
    		behaviorsSelectedRS++;
    	}
    	if(keyH.form2.behaviors.contains("smart"))
    	{
    		behaviorsSelectedRS++;
    	}
    	if(keyH.form2.behaviors.contains("dumb"))
    	{
    		behaviorsSelectedRS++;
    	}
    	if(keyH.form2.behaviors.contains("farsighted"))
    	{
    		behaviorsSelectedRS++;
    	}
    	if(keyH.form2.behaviors.contains("careless"))
    	{
    		behaviorsSelectedRS++;
    	}
    }
    
}
