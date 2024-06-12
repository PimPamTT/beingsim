package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

public class KeyHandler implements KeyListener 
{
    GamePanel gp;
    
    public boolean upPressed, downPressed, leftPressed, rightPressed, qPressed, ePressed, spacePressed;
    
    public int behavior1;
    public int behavior2;
    public int behavior3;
     
    public Form form1 = new Form();
    public Form form2 = new Form();
 
    @Override
    public void keyTyped(KeyEvent e) 
    { }

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }
    
    @Override
    public void keyPressed(KeyEvent e) 
    {
        int code = e.getKeyCode();

        // TITLE STATE
        if(gp.gameState == gp.titleState) 
        {
            try 
            {
				titleState(code);
			}
            catch (SQLException e1)
            {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState)
        {
            playState(code);
        }
        else if(gp.gameState == gp.spectatorState)
        {
            spectatorState(code);
        }
        // PAUSE STATE
        else if(gp.gameState == gp.pauseState)
        {
            pauseState(code);
        }
        // MAP STATE
        else if(gp.gameState == gp.mapState)
        {
            mapState(code);
        }
    }

    public void titleState(int code) throws SQLException
    {
        // MAIN MENU
        if (gp.ui.titleScreenState == 0) 
        {
            if (code == KeyEvent.VK_W)
            {
                gp.ui.commandNum--;
                
                if (gp.ui.commandNum < 0) 
                {
                    gp.ui.commandNum = 1;
                }
            }
            
            if (code == KeyEvent.VK_S) 
            {
                gp.ui.commandNum++;
                
                if (gp.ui.commandNum > 1)
                {
                    gp.ui.commandNum = 0;
                }
            }
            
            if (code == KeyEvent.VK_ENTER) 
            {
                if (gp.ui.commandNum == 0)
                {
                	gp.playSE(5);
                    gp.ui.titleScreenState = 1; 		// Character class selection screen
                }
                
                if (gp.ui.commandNum == 1)
                {
                	gp.playSE(0);
                    System.exit(0);
                }
            }
        }
        // SECOND SCREEN, CHARACTER SELECTION
        else if (gp.ui.titleScreenState == 1)
        {
            if (code == KeyEvent.VK_W)
            {
                gp.ui.commandNum--;
                
                if (gp.ui.commandNum < 0) 
                {
                    gp.ui.commandNum = 9;
                }
            }
            
            if (code == KeyEvent.VK_S)
            {
                gp.ui.commandNum++;
                
                if (gp.ui.commandNum >= 10)
                {
                    gp.ui.commandNum = 0;
                }
            }            
            
            if (code == KeyEvent.VK_ENTER) 
            {
            	form(form1);
            	
                if (gp.ui.commandNum == 8) 
                {
                	gp.playSE(5);
                	
                	if(form1.count >= 1)
                	{
                		System.out.println(form1);
                		gp.dbManager.insertSpecies("Green Slime", gp.dbManager.getDietByName(form1.diet));
                		
                		behaviors(form1);
                		
                		gp.ui.titleScreenState = 2;
                		gp.ui.commandNum = 0;
                	}                	
                }
                
                if (gp.ui.commandNum == 9) 
                {
                	gp.playSE(0);                	
                    gp.ui.titleScreenState = 0;   
                    gp.ui.commandNum = 0;
                    gp.restartDatabase();
                }
            }
        }              
        //THIRD SCREEN, CHARACTER SELECTION
        else if (gp.ui.titleScreenState == 2)
        {
        	if (code == KeyEvent.VK_W)
        	{
                gp.ui.commandNum--;
                
                if (gp.ui.commandNum < 0) 
                {
                    gp.ui.commandNum = 9;
                }
            }
        	
            if (code == KeyEvent.VK_S) 
            {
                gp.ui.commandNum++;
                
                if (gp.ui.commandNum >= 10)
                {
                    gp.ui.commandNum = 0;
                }
            }
            
            if (code == KeyEvent.VK_ENTER)
            {
            	form(form2);
            	
                if (gp.ui.commandNum == 8)
                {
                	gp.playSE(5);
                	
                	if(form2.count >= 1)
                	{
                		System.out.println(form2);
                		gp.dbManager.insertSpecies("Red Slime", gp.dbManager.getDietByName(form2.diet));
                		
                		behaviors(form2);
                		
                		gp.setBehaviors();
                		gp.aSetter.setMonster();
                		
                		gp.gameState = gp.playState;
                		gp.playMusic(3);
                	}                	
                }
                
                if (gp.ui.commandNum == 9) 
                {
                	gp.playSE(0);                	
                    gp.ui.titleScreenState = 0;   
                    gp.ui.commandNum = 0;
                    gp.restartDatabase();
                }
            }
        }
    }
    
    public void playState(int code)
    {
        if(code == KeyEvent.VK_W)
        {
            upPressed = true;
        }
        
        if(code == KeyEvent.VK_S)
        {
            downPressed = true;
        }
        
        if(code == KeyEvent.VK_A)
        {
            leftPressed = true;
        }
        
        if(code == KeyEvent.VK_D)
        {
            rightPressed = true;
        }
        
        if(code == KeyEvent.VK_SPACE)
        {
            spacePressed = true;
        }
        
        if(code == KeyEvent.VK_P)
        {
            gp.gameState = gp.pauseState;
        }

        if(code == KeyEvent.VK_M)
        {
            gp.gameState = gp.mapState;
        }
        
        if(code == KeyEvent.VK_X)
        {
            if(gp.map.miniMapOn == false)
            {
                gp.map.miniMapOn = true;
            }
            else
            {
                gp.map.miniMapOn = false;
            }
        }
        
        if(code == KeyEvent.VK_I)
        {
            if(gp.instructions == true)
            {
                gp.instructions = false;
            }
            else
            {
                gp.instructions = true;
            }
        }
    }
    
    public void spectatorState(int code)
    {
        if(code == KeyEvent.VK_Q)
        {
            qPressed = true;
        }
        
        if(code == KeyEvent.VK_E)
        {
            ePressed = true;
        }
        
        if(code == KeyEvent.VK_SPACE)
        {
            spacePressed = true;
        }
        
        if(code == KeyEvent.VK_P)
        {
            gp.gameState = gp.pauseState;
        }
        
        if(code == KeyEvent.VK_M)
        {
            gp.gameState = gp.mapState;
        }
        
        if(code == KeyEvent.VK_X)
        {
            if(gp.map.miniMapOn == false)
            {
                gp.map.miniMapOn = true;
            }
            else
            {
                gp.map.miniMapOn = false;
            }
        }
        
        if(code == KeyEvent.VK_I)
        {
            if(gp.instructions == true)
            {
                gp.instructions = false;
            }
            else
            {
                gp.instructions = true;
            }
        }
    }
    
    public void pauseState(int code)
    {
        if(code == KeyEvent.VK_P)
        {
            gp.gameState = gp.playState;
        }
    }

    public void mapState(int code)
    {
       if(code == KeyEvent.VK_M)
       {
           gp.gameState = gp.playState;
       }
    }

   @Override
    public void keyReleased(KeyEvent e)
   {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W)
        {
            upPressed = false;
        }
        
        if(code == KeyEvent.VK_S)
        {
            downPressed = false;
        }
        
        if(code == KeyEvent.VK_A)
        {
            leftPressed = false;
        }
        
        if(code == KeyEvent.VK_D)
        {
            rightPressed = false;
        }
    }
   
   	public void form(Form pForm) throws SQLException
   	{
    	if (gp.ui.commandNum == 0) 
    	{
    		gp.playSE(4);
    		pForm.addBehavior("aggresive");
    	}          
    	
    	if (gp.ui.commandNum == 1) 
    	{
    		gp.playSE(4);
            pForm.addBehavior("calm");
        }
    	
        if (gp.ui.commandNum == 2) 
        {
        	gp.playSE(4);
            pForm.addBehavior("smart");
        }    
        
        if (gp.ui.commandNum == 3) 
        {
        	gp.playSE(4);
            pForm.addBehavior("dumb");
        }      
        
        if (gp.ui.commandNum == 4) 
        {
        	gp.playSE(4);
            pForm.addBehavior("farsighted");

        }
        
        if (gp.ui.commandNum == 5) 
        {
        	gp.playSE(4);
            pForm.addBehavior("careless");
        }
        
        if (gp.ui.commandNum == 6) 
        {
        	gp.playSE(4);
        	pForm.addDiet("carnivore");
		}
        
        if (gp.ui.commandNum == 7) 
        {
        	gp.playSE(4);
	        pForm.addDiet("herbivore");
	  	}
   	}
   	
   	public void behaviors(Form pForm) throws SQLException
   	{
   		behavior1 = 0;
   		behavior2 = 0;
   		behavior3 = 0;
   		
   		if(pForm.count == 1) 
   		{
   			behavior1 = gp.dbManager.getBehaviorByName(pForm.behaviors.get(0)); 		
   		}
   		
   		if(pForm.count == 2) 
   		{
   			behavior1 = gp.dbManager.getBehaviorByName(pForm.behaviors.get(0));
   			behavior2 = gp.dbManager.getBehaviorByName(pForm.behaviors.get(1)); 
   		}
   		
   		if(pForm.count == 3) 
   		{
   			behavior1 = gp.dbManager.getBehaviorByName(pForm.behaviors.get(0));
   			behavior2 = gp.dbManager.getBehaviorByName(pForm.behaviors.get(1)); 	
   			behavior3 = gp.dbManager.getBehaviorByName(pForm.behaviors.get(2));
   		}   		
   		   		
   		if(gp.ui.titleScreenState == 1)
   		{
   			if(pForm.count == 1)
   			{
   				gp.dbManager.insertSpeciesBehaviors(1, behavior1);
   				System.out.println("Registro 1 realizado con exito.");
   			}
   			
   			if(pForm.count == 2)
   			{
   				gp.dbManager.insertSpeciesBehaviors(1, behavior1);
   				System.out.println("Registro 1 realizado con exito.");
   				gp.dbManager.insertSpeciesBehaviors(1, behavior2);
   				System.out.println("Registro 2 realizado con exito.");
   			}
   			
   			if(pForm.count == 3)
   			{
   				gp.dbManager.insertSpeciesBehaviors(1, behavior1);
   				System.out.println("Registro 1 realizado con exito.");
   				gp.dbManager.insertSpeciesBehaviors(1, behavior2);
   				System.out.println("Registro 2 realizado con exito.");
   				gp.dbManager.insertSpeciesBehaviors(1, behavior3);
   				System.out.println("Registro 3 realizado con exito.");
   			}		        
   		}
   		
   		if(gp.ui.titleScreenState == 2)
   		{
   			if(pForm.count == 1)
   			{
   				gp.dbManager.insertSpeciesBehaviors(2, behavior1);
   				System.out.println("Registro 1 realizado con exito.");
   			}
   			
   			if(pForm.count == 2)
   			{
   				gp.dbManager.insertSpeciesBehaviors(2, behavior1);
   				System.out.println("Registro 1 realizado con exito.");
   				gp.dbManager.insertSpeciesBehaviors(2, behavior2);
   				System.out.println("Registro 2 realizado con exito.");
   			}
   			
   			if(pForm.count == 3)
   			{
   				gp.dbManager.insertSpeciesBehaviors(2, behavior1);
   				System.out.println("Registro 1 realizado con exito.");
   				gp.dbManager.insertSpeciesBehaviors(2, behavior2);
   				System.out.println("Registro 2 realizado con exito.");
   				gp.dbManager.insertSpeciesBehaviors(2, behavior3);
   				System.out.println("Registro 3 realizado con exito.");
   			}		        
   		}
   	}
}
