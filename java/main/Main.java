package main;

import java.sql.*;

import javax.swing.*;

import db.DatabaseManager;

public class Main 
{
    public static JFrame window;
    public static DatabaseManager dbManager;

    public static void main(String[] args) 
    {   
    	try 
    	{
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
        } 
    	catch (SQLException e) 
    	{
            e.printStackTrace();
        }  
            
        window = new JFrame();											// Instanciamos un objeto de la case JFrame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			// Si pulsamos en X superior derecha cerramos el juego
        window.setResizable(false); 									// No dejamos que se puede hacer un resize
        window.setTitle("BeingSim\n");						 			// Establecemos el nombre del juego
        new Main().setIcon();											// Cargamos el icono del juego
        GamePanel gamePanel = new GamePanel(dbManager);					// Creamos un objeto de GamePanel
        window.add(gamePanel);											// Lo añadimos al Frame

        window.pack(); 													// Reajusta el tamaño a prefered size y previene overflow

        window.setLocationRelativeTo(null); 							// Centramos la pantalla
        window.setVisible(true);										// La hacemos visible

        gamePanel.setupGame(); 											// Configuramos el juego antes de empezar
        gamePanel.startGameThread();									// Lanzamos el game loop
    }
    public void setIcon()												
    {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("monster/Numo_down_1.png"));		// Cargamos el icono
        window.setIconImage(icon.getImage());																	// Lo ponemos en la barra de arriba.
    }
}