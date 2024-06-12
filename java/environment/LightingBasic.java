package environment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class LightingBasic {
    GamePanel gp;
    BufferedImage darknessFilter;
    public int dayCounter;
    public float filterAlpha = 0f;
    
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;
    public int dayState = day;

    public LightingBasic(GamePanel gp)
    {
        this.gp = gp;
        setLightSource();
    }
   
    public void setLightSource()
    {
        //Create a buffered image
    }
    
    public void update()
    {
        //Check the state of the day
    	if(gp.gameState == gp.playState || gp.gameState == gp.spectatorState)
    	{
	        if(dayState == day)
	        {
	            dayCounter++;
	            if(dayCounter > 3600) // 1 min day
	            {
	                dayState = dusk;
	                dayCounter = 0;
	            }
	        }
	        
	        if(dayState == dusk)
	        {
	            filterAlpha += 0.0005f;   //0.0005f x 2000 = 1f, 2000/60 = 32 seconds
	            if(filterAlpha > 0.5f)
	            {
	                filterAlpha = 0.5f;
	                dayState = night;
	            }
	        }
	        
	        if(dayState == night)
	        {
	            dayCounter++;
	            if(dayCounter > 3600) //1 min night
	            {
	                dayState = dawn;
	                dayCounter = 0;
	            }
	        }
	        
	        if(dayState == dawn)
	        {
	            filterAlpha -= 0.0005f;   //0.0005f x 2000 = 1f, 2000/60 = 32 seconds
	            if(filterAlpha < 0)
	            {
	                filterAlpha = 0;
	                dayState = day;
	            }
	        }
        }
    }
    
    
    public void draw(Graphics2D g2)
    {
    	Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));
        g2.setColor(new Color (0,0,0,filterAlpha));
        g2.fill(screenArea);

        String situation = "";
        
        switch (dayState)
        {
            case day: situation = "Day"; break;
            case dusk: situation = "Dusk"; break;
            case night: situation = "Night"; break;
            case dawn: situation = "Dawn"; break;
        }

		g2.setFont(g2.getFont().deriveFont(50f));
	    
	    Color frameColor = new Color(36, 34, 46);
	    g2.setColor(frameColor);
	    g2.drawString(situation,803,503); 
	    
	    frameColor = new Color(230,235,214);
	    g2.setColor(frameColor);
	    g2.drawString(situation,800,500);      	      
    }
}
