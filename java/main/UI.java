package main;

import entity.Entity;
//import object.OBJ_Coin_Bronze;
//import object.OBJ_Heart;
//import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class UI 
{
    GamePanel gp;
    Graphics2D g2;
    public Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; 			// 0 : Main Menu, 1 : the second screen
    
    //Player Inventory
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;


    int subState = 0;
    int counter = 0; // transition
    public Entity npc;
    int charIndex = 0;
    String combinedText = "";

    public UI(GamePanel gp)
    {
        this.gp = gp;
        try
        {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (FontFormatException e) 
        {
            e.printStackTrace();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void drawPauseScreen()
    {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
        
        String text =  "PAUSE";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        
        Color frameColor = new Color(36, 34, 46);
        g2.setColor(frameColor);
        g2.drawString(text,x+4,y+4);
        
        frameColor = new Color(230,235,214);
        g2.setColor(frameColor);
        g2.drawString(text,x,y);
    }

    public void drawPlayState()
    {   
    	if(gp.instructions == true)
    	{
    		g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
        	
        	String text =  "PLAY STATE";
            Color frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*7+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*7);
        	
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
            
            text =  "WASD - TO MOVE";
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*8+1);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*8);
            
            text =  "SPACE - TO CHANGE STATE";
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*9+1);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*9);
            
            text =  "P - TO PAUSE/UNPAUSE";
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*10+1);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*10);
            
            text =  "M/P - TO TOGGLE MAP/MINIMAP";
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*11+1);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*11);
    	}    	
    }
    
    public void drawMonsterLife()
    {
        Entity monster = gp.monster[gp.currentMap][gp.currentIndex];
        Color frameColor = new Color(36, 34, 46);
        
        if(monster!= null && monster.inCamera() == true && monster.active == true)
        {
        	g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
        	
        	String text =  "LIFE:";                
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize);
            
            text =  "HUNGER:";  
        	frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*2+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*2);
            
        	text =  "THIRST:";   
        	frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2+3,gp.tileSize*3+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,gp.tileSize/2,gp.tileSize*3);
        	
            double oneScaleHp = (double)gp.tileSize/monster.maxLife; // (bar lenght / maxlife) Ex: if monster hp = 2, tilesize = 48px. So, 1 hp = 24px
            double hpBarValue = oneScaleHp * monster.life;

            if(hpBarValue < 0) //Ex: You attack 5 hp to monster which has 3 hp. Monster's hp will be -2 and bar will ofset to left. To avoid that check if hpBarValue less than 0.
            {
                hpBarValue = 0;
            }

            double oneScaleHn = (double)gp.tileSize/monster.maxHunger; 
            double hnBarValue = oneScaleHn * monster.hunger;

            if(hnBarValue < 0) 
            {
                hnBarValue = 0;
            }
            
            double oneScaleTh = (double)gp.tileSize/monster.maxThirst; 
            double thBarValue = oneScaleTh * monster.thirst;

            if(thBarValue < 0) 
            {
                thBarValue = 0;
            }
            
            if(monster.life > 0)
            {
            	g2.setColor(new Color(35,35,35));
                g2.fillRect(gp.tileSize*3+24,gp.tileSize/2+2,(int)hpBarValue*2+4,22);
            	
            	g2.setColor(new Color(255,0,30));
                g2.fillRect(gp.tileSize*3+24+2,gp.tileSize/2+1+2, (int)hpBarValue*2,20);
            }                  

            if(monster.hunger > 0)	
            {
                g2.setColor(new Color(35,35,35));
                g2.fillRect(gp.tileSize*3+24,gp.tileSize+(gp.tileSize/2)+2,(int)hnBarValue*2+4,22);
                
            	g2.setColor(new Color(128,64,30));
                g2.fillRect(gp.tileSize*3+24+2,gp.tileSize+(gp.tileSize/2)+1+2, (int)hnBarValue*2,20);
            }
            
            if(monster.thirst > 0)
            {
            	g2.setColor(new Color(35,35,35));
                g2.fillRect(gp.tileSize*3+24,gp.tileSize*2+(gp.tileSize/2)+2,(int)thBarValue*2+4,22);
            	
            	g2.setColor(new Color(30,0,255));
                g2.fillRect(gp.tileSize*3+24+2,gp.tileSize*2+(gp.tileSize/2)+1+2, (int)thBarValue*2,20); 
            }
            
            monster.hpBarCounter++;
            monster.hnBarCounter++;
            monster.thBarCounter++;
            
            if(monster.hpBarCounter > 60)  // 10
            {
                monster.hpBarCounter = 0;
            }
            
            if(monster.hnBarCounter > 60)  // 10
            {
                monster.hnBarCounter = 0;
            }
            
            if(monster.thBarCounter > 60)  // 10
            {
                monster.thBarCounter = 0;
            }
        }
    }
    
    public void drawSpectatorState()
    {
    	if(gp.instructions == true)
    	{
	    	g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
	    	
	    	String text =  "SPECTATOR STATE";
	        Color frameColor = new Color(36, 34, 46);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2+3,gp.tileSize*7+3);
	        
	        frameColor = new Color(230,235,214);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2,gp.tileSize*7);
	    	
	        g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
	        
	        text =  "QE - TO CHANGE BETWEEN SPECIES";
	        frameColor = new Color(36, 34, 46);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2+3,gp.tileSize*8+1);
	        
	        frameColor = new Color(230,235,214);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2,gp.tileSize*8);
	        
	        text =  "SPACE - TO CHANGE STATE";
	        frameColor = new Color(36, 34, 46);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2+3,gp.tileSize*9+1);
	        
	        frameColor = new Color(230,235,214);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2,gp.tileSize*9);
	        
	        text =  "P - TO PAUSE/UNPAUSE";
	        frameColor = new Color(36, 34, 46);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2+3,gp.tileSize*10+1);
	        
	        frameColor = new Color(230,235,214);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2,gp.tileSize*10);
	        
	        text =  "M/P - TO TOGGLE MAP/MINIMAP";
	        frameColor = new Color(36, 34, 46);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2+3,gp.tileSize*11+1);
	        
	        frameColor = new Color(230,235,214);
	        g2.setColor(frameColor);
	        g2.drawString(text,gp.tileSize/2,gp.tileSize*11);	
    	}
    }
    
    public void drawEndState()
    {
    	Color frameColor = new Color(36, 34, 46);
    	int x;
        int y = gp.screenHeight / 2;
    	
    	if(gp.pierdes == true)
    	{
	    	g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
	    	
	        String text =  "GAME OVER";
	        x = getXforCenteredText(text);	        
	        
            g2.setColor(frameColor);
            g2.drawString(text,x+3,y+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
    	}
    	else 
    	{
    		g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
    		
	        String text =  "YOU WIN";
	        x = getXforCenteredText(text);
	        
	        frameColor = new Color(36, 34, 46);
	        g2.setColor(frameColor);
            g2.drawString(text,x+3,y+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
    	}
    }
    
    public void drawSubWindow(int x, int y, int width, int height)
    {
        Color c = new Color(0,0,0,210);  // R,G,B, alfa(opacity)
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));    // 5 = width of outlines of graphics
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }

    public void drawTitleScreen()
    {
    	float[] fractions = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
        Color[] colors = {
            new Color(36, 34, 46),       
            new Color(129, 150, 126),     
            new Color(132, 173, 167),     
            new Color(129, 150, 126),     
            new Color(36, 34, 46)       
        };
        
        LinearGradientPaint gradient = new LinearGradientPaint(
            0, 0, 0, gp.screenHeight, fractions, colors, CycleMethod.NO_CYCLE
        );
        
        g2.setPaint(gradient);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // MAIN MENU
        if(titleScreenState == 0)
        {
        	int frameWidth = 60;
            Color frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.setStroke(new BasicStroke(16));
            int arcWidth = 20;
            int arcHeight = 20;
            g2.drawRoundRect(frameWidth, frameWidth, gp.screenWidth - 2 * frameWidth, gp.screenHeight - 2 * frameWidth, arcWidth, arcHeight);
        	
            // TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "BeingSim\n";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 5;
            
            // SHADOW
            g2.setColor(frameColor);
            g2.drawString(text,x+4,y+4);
            
            // MAIN COLOR
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text, x, y);
            
            // MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "PLAY";
            x = getXforCenteredText(text);
            y = gp.screenHeight / 2 + gp.tileSize*2;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+3,y+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 0)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+3, y+3);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }

            text = "QUIT";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+3,y+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);            
            
            if(commandNum == 1)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+3, y+3);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
        }
        // SECOND SCREEN
        else if(titleScreenState == 1)
        {
            // CLASS SELECTION SCREEN
            g2.setFont(g2.getFont().deriveFont(72F));

            String text = "FORM";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*2;
            
            Color frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+3,y+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            // 1 
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Behavior";
            x = gp.tileSize*2;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Aggresive";
            y += gp.tileSize*0.75;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            
            if(commandNum == 0)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.behaviors.contains("aggresive"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }            
            
            text = "Calm";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 1)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.behaviors.contains("calm"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }   
            
            text = "Smart";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 2)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.behaviors.contains("smart"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            text = "Dumb";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 3)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.behaviors.contains("dumb"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            text = "Farsighted";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 4)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.behaviors.contains("farsighted"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            text = "Careless";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 5)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.behaviors.contains("careless"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            // 2
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Diet";
            y += gp.tileSize;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Carnivore";
            y += gp.tileSize*0.75;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 6)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.diet.equals("carnivore"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);;
            }
            
            text = "Herbivore";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 7)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form1.diet.equals("herbivore"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }
            
            // 3
            g2.setColor(Color.yellow);
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Next";
            y += gp.tileSize*1.25;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 8)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            text = "Back";
            y += gp.tileSize;
           
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 9)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            // 4
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Species";
            x = gp.tileSize*15;
            y = gp.tileSize*3;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Numo";
            x += 36;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Instructions";
            x = gp.tileSize*15-30;
            y += gp.tileSize*5-8;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Kill the other";
            x = gp.tileSize*15;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            text = "species to win";
            x = gp.tileSize*15-12;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            BufferedImage titleImage;
            try 
            {
                titleImage = ImageIO.read(getClass().getResource("/monster/Numo_down_1.png"));
                g2.drawImage(titleImage,gp.screenWidth/2-(gp.tileSize*2-gp.tileSize/2),gp.screenHeight/2-(gp.tileSize*2-gp.tileSize/2),gp.tileSize*3,gp.tileSize*3,null);
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }            
        }        
        // THIRD SCREEN
        else if(titleScreenState == 2)
        {
        	// CLASS SELECTION SCREEN
            g2.setFont(g2.getFont().deriveFont(72F));

            String text = "FORM";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*2;
            
            Color frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+3,y+3);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            // 1 
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Behavior";
            x = gp.tileSize*2;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Aggresive";
            y += gp.tileSize*0.75;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            
            if(commandNum == 0)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.behaviors.contains("aggresive"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }            
            
            text = "Calm";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 1)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.behaviors.contains("calm"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }   
            
            text = "Smart";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 2)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.behaviors.contains("smart"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            text = "Dumb";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 3)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.behaviors.contains("dumb"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            text = "Farsighted";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 4)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.behaviors.contains("farsighted"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            text = "Careless";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 5)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.behaviors.contains("careless"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }  
            
            // 2
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Diet";
            y += gp.tileSize;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Carnivore";
            y += gp.tileSize*0.75;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 6)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.diet.equals("carnivore"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);;
            }
            
            text = "Herbivore";
            y += gp.tileSize*0.5;

            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 7)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            if(gp.keyH.form2.diet.equals("herbivore"))
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
            	g2.drawString("X",x+gp.tileSize*4+2,y+2);
            	frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString("X",x+gp.tileSize*4,y);
            }
            
            // 3
            g2.setColor(Color.yellow);
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Next";
            y += gp.tileSize*1.25;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 8)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            text = "Back";
            y += gp.tileSize;
           
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            if(commandNum == 9)
            {
            	frameColor = new Color(36, 34, 46);
            	g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize+2, y+2);
                frameColor = new Color(230,235,214);
                g2.setColor(frameColor);
                g2.drawString(">",x - gp.tileSize, y);
            }
            
            // 4
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Species";
            x = gp.tileSize*15;
            y = gp.tileSize*3;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Hebikura";
            x += 20;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(48F));
            
            text = "Instructions";
            x = gp.tileSize*15-30;
            y += gp.tileSize*5-8;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            g2.setFont(g2.getFont().deriveFont(28F));
            
            text = "Don't get killed";
            x = gp.tileSize*15-12;
            y += gp.tileSize;
            
            frameColor = new Color(36, 34, 46);
            g2.setColor(frameColor);
            g2.drawString(text,x+2,y+2);
            
            frameColor = new Color(230,235,214);
            g2.setColor(frameColor);
            g2.drawString(text,x,y);
            
            BufferedImage titleImage;
            try 
            {
                titleImage = ImageIO.read(getClass().getResource("/monster/Hebikara_down_1.png"));
                g2.drawImage(titleImage,gp.screenWidth/2-(gp.tileSize*2-gp.tileSize/2),gp.screenHeight/2-(gp.tileSize*2-gp.tileSize/2),gp.tileSize*3,gp.tileSize*3,null);
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            } 
        }
    }
    
    public int getXforCenteredText(String text)
    {
        int textLenght;
        textLenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); // Gets width of text.
        int x = gp.screenWidth / 2 - textLenght/2;
        return x;
    }
    
    public int getXforAlignToRight(String text, int tailX)
    {
        int textLenght;
        textLenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth(); // Gets width of text.
        int x = tailX - textLenght;
        return x;
    }
    
    public void draw(Graphics2D g2)
    {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  // Anti Aliasing // Smoothes the text
        g2.setColor(Color.white);

        // TITLE STATE
        if(gp.gameState == gp.titleState)
        {
            drawTitleScreen();
        }
        // OTHERS
        else
        {
            // PLAY STATE
            if(gp.gameState == gp.playState)
            {
                drawPlayState();
            }
            // SPECTATOR STATE
            if(gp.gameState == gp.spectatorState)
            {
                drawSpectatorState();
                drawMonsterLife();
            }
            // PAUSE STATE
            if(gp.gameState == gp.pauseState)
            {
                drawPauseScreen();
            }
            // END STATE
            if(gp.gameState == gp.endState)
            {
            	drawEndState();
            }
        }
    }
}
