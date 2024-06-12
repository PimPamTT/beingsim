package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import monster.*;
import object.*;

public class AssetSetter 
{
    GamePanel gp;
    Timer timer;
    int interval = 120;
    
    Random rnum = new Random();
    
    int minAmmount = rnum.nextInt(5) + 3;
    int maxAmmount = rnum.nextInt(5) + 1;
    
    int minAmmountGreen = rnum.nextInt(5) + 5;
    int maxAmmountGreen = rnum.nextInt(5) + 1;
    
    int minAmmountRed = rnum.nextInt(5) + 5;
    int maxAmmountRed = rnum.nextInt(5) + 1;
    
    public ArrayList<Integer> puntosInteresMapa0X = new ArrayList<>();
    public ArrayList<Integer> puntosInteresMapa0Y = new ArrayList<>();
    
    public boolean isPositionOccupied(int mapNum, int x, int y) 
    {
        for (int i = 0; i < gp.obj[mapNum].length; i++)
        {
            if (gp.obj[mapNum][i] != null && gp.obj[mapNum][i].worldX == x && gp.obj[mapNum][i].worldY == y) 
            {
                return true;
            }
        }
        
        return false;
    }
    
    public int[] getRandomEmptyPosition(int mapNum)
    {
        int x, y;
        
        do 
        {
            x = gp.tileSize * puntosInteresMapa0X.get(gp.rnum.nextInt(8));
            y = gp.tileSize * puntosInteresMapa0Y.get(gp.rnum.nextInt(12));
        } while (isPositionOccupied(mapNum, x, y));
        
        return new int[]{x, y};
    }
    
    public void map0PointsX()
    {
    	puntosInteresMapa0X.add(13);		// 0
    	puntosInteresMapa0X.add(20);		// 2
    	puntosInteresMapa0X.add(22);		// 3
    	puntosInteresMapa0X.add(24);		// 4
    	puntosInteresMapa0X.add(25);		// 5
    	puntosInteresMapa0X.add(27);		// 6
    	puntosInteresMapa0X.add(29);		// 7
    	puntosInteresMapa0X.add(36);		// 9
    }
    
    public void map0PointsY()
    {
    	puntosInteresMapa0Y.add(8);			// 0
    	puntosInteresMapa0Y.add(11);		// 1
    	puntosInteresMapa0Y.add(14);		// 2
    	puntosInteresMapa0Y.add(17);		// 3
    	puntosInteresMapa0Y.add(20);		// 4
    	puntosInteresMapa0Y.add(23);		// 5
    	puntosInteresMapa0Y.add(26);		// 6
    	puntosInteresMapa0Y.add(29);		// 7
    	puntosInteresMapa0Y.add(32);		// 8
    	puntosInteresMapa0Y.add(35);		// 9
    	puntosInteresMapa0Y.add(38);		// 10
    	puntosInteresMapa0Y.add(41);		// 11
    }
    
    public AssetSetter(GamePanel gp)
    {
        this.gp = gp;
        timer = new Timer();
        map0PointsX();
        map0PointsY();
    }

    public void setObject()
    {
        boolean perm2 = true;
        boolean perm3 = true;
        boolean perm4 = true;
        
        int mapNum = 0;
        int i = 0;
        
        int[] position;

        position = getRandomEmptyPosition(mapNum);
        gp.obj[mapNum][i] = new OBJ_Base(gp);
        gp.obj[mapNum][i].worldX = position[0];
        gp.obj[mapNum][i].worldY = position[1];
        i++;

        position = getRandomEmptyPosition(mapNum);
        gp.obj[mapNum][i] = new OBJ_Base(gp);
        gp.obj[mapNum][i].worldX = position[0];
        gp.obj[mapNum][i].worldY = position[1];
        i++;
        
        // 1
        for (int j = 0; j < minAmmount; j++) 
        {
            position = getRandomEmptyPosition(mapNum);
            gp.obj[mapNum][i] = new OBJ_NoBone(gp);
            gp.obj[mapNum][i].worldX = position[0];
            gp.obj[mapNum][i].worldY = position[1];
            i++;
        }
        
        for (int j = 0; j < maxAmmount; j++) 
        {
            if (perm2) 
            {
                perm2 = gp.rnum.nextBoolean();
            }
            
            if (perm2) 
            {
                position = getRandomEmptyPosition(mapNum);
                gp.obj[mapNum][i] = new OBJ_NoBone(gp);
                gp.obj[mapNum][i].worldX = position[0];
                gp.obj[mapNum][i].worldY = position[1];
                i++;
            }
        }
        
        // 2
        minAmmount = rnum.nextInt(5) + 3;
        maxAmmount = rnum.nextInt(5) + 1;
        
        for (int j = 0; j < minAmmount; j++)
        {
            position = getRandomEmptyPosition(mapNum);
            gp.obj[mapNum][i] = new OBJ_Puddle(gp);
            gp.obj[mapNum][i].worldX = position[0];
            gp.obj[mapNum][i].worldY = position[1];
            i++;
        }
        
        for (int j = 0; j < maxAmmount; j++) 
        {
            if (perm3)
            {
                perm3 = gp.rnum.nextBoolean();
            }
            
            if (perm3)
            {
                position = getRandomEmptyPosition(mapNum);
                gp.obj[mapNum][i] = new OBJ_Puddle(gp);
                gp.obj[mapNum][i].worldX = position[0];
                gp.obj[mapNum][i].worldY = position[1];
                i++;
            }
        }
        
        // 3
        minAmmount = rnum.nextInt(5) + 3;
        maxAmmount = rnum.nextInt(5) + 1;
        
        for (int j = 0; j < minAmmount; j++)
        {
            position = getRandomEmptyPosition(mapNum);
            gp.obj[mapNum][i] = new OBJ_Bone(gp);
            gp.obj[mapNum][i].worldX = position[0];
            gp.obj[mapNum][i].worldY = position[1];
            i++;
        }
        
        for (int j = 0; j < maxAmmount; j++) 
        {
            if (perm4) 
            {
                perm4 = gp.rnum.nextBoolean();
            }
            
            if (perm4) 
            {
                position = getRandomEmptyPosition(mapNum);
                gp.obj[mapNum][i] = new OBJ_Bone(gp);
                gp.obj[mapNum][i].worldX = position[0];
                gp.obj[mapNum][i].worldY = position[1];
                i++;
            }
        }
        
        // 4
        minAmmount = rnum.nextInt(5) + 3;
        maxAmmount = rnum.nextInt(5) + 1;
        
        for (int j = 0; j < minAmmount; j++)
        {
            position = getRandomEmptyPosition(mapNum);
            gp.obj[mapNum][i] = new OBJ_NoPuddle(gp);
            gp.obj[mapNum][i].worldX = position[0];
            gp.obj[mapNum][i].worldY = position[1];
            i++;
        }
        
        for (int j = 0; j < maxAmmount; j++) 
        {
            if (perm3)
            {
                perm3 = gp.rnum.nextBoolean();
            }
            
            if (perm3)
            {
                position = getRandomEmptyPosition(mapNum);
                gp.obj[mapNum][i] = new OBJ_NoPuddle(gp);
                gp.obj[mapNum][i].worldX = position[0];
                gp.obj[mapNum][i].worldY = position[1];
                i++;
            }
        }
        
        // 5
        minAmmount = rnum.nextInt(5) + 3;
        maxAmmount = rnum.nextInt(5) + 1;
        
        for (int j = 0; j < minAmmount; j++)
        {
            position = getRandomEmptyPosition(mapNum);
            gp.obj[mapNum][i] = new OBJ_Fruit(gp);
            gp.obj[mapNum][i].worldX = position[0];
            gp.obj[mapNum][i].worldY = position[1];
            i++;
        }
        
        for (int j = 0; j < maxAmmount; j++) 
        {
            if (perm3)
            {
                perm3 = gp.rnum.nextBoolean();
            }
            
            if (perm3)
            {
                position = getRandomEmptyPosition(mapNum);
                gp.obj[mapNum][i] = new OBJ_Fruit(gp);
                gp.obj[mapNum][i].worldX = position[0];
                gp.obj[mapNum][i].worldY = position[1];
                i++;
            }
        }
        
        // 6
        minAmmount = rnum.nextInt(5) + 3;
        maxAmmount = rnum.nextInt(5) + 1;
        
        for (int j = 0; j < minAmmount; j++)
        {
            position = getRandomEmptyPosition(mapNum);
            gp.obj[mapNum][i] = new OBJ_NoFruit(gp);
            gp.obj[mapNum][i].worldX = position[0];
            gp.obj[mapNum][i].worldY = position[1];
            i++;
        }
        
        for (int j = 0; j < maxAmmount; j++) 
        {
            if (perm3)
            {
                perm3 = gp.rnum.nextBoolean();
            }
            
            if (perm3)
            {
                position = getRandomEmptyPosition(mapNum);
                gp.obj[mapNum][i] = new OBJ_NoFruit(gp);
                gp.obj[mapNum][i].worldX = position[0];
                gp.obj[mapNum][i].worldY = position[1];
                i++;
            }
        }
    }
   
    public void setMonster()
    {    	

    	int numGreens = minAmmountGreen + maxAmmountGreen;
    	int numReds   = minAmmountRed + maxAmmountRed;
 //   	int counter   = 0;
    	boolean alternate = false;
    	
    	gp.totalMonstersCreated = numGreens + numReds;
    	
    	for (int i =0; i < gp.totalMonstersCreated; i++)
    	{
    		// case 2 - We only have Greens pending
    		if (numGreens != 0 && numReds == 0) 
    		{
				gp.monster[gp.currentMap][i] = new MON_GreenSlime(gp);
	            gp.monster[gp.currentMap][i].worldX = gp.obj[gp.currentMap][0].worldX;
	            gp.monster[gp.currentMap][i].worldY = gp.obj[gp.currentMap][0].worldY;
	            numGreens--;
    		} 
    		
    		// case 4 - We only have Reds pending
    		if (numGreens == 0 && numReds != 0) 
    		{
				gp.monster[gp.currentMap][i] = new MON_RedSlime(gp);
	            gp.monster[gp.currentMap][i].worldX = gp.obj[gp.currentMap][1].worldX;
	            gp.monster[gp.currentMap][i].worldY = gp.obj[gp.currentMap][1].worldY;
	            numReds--;
    		}    		
    		// Case 1 - We have both species counter > 0 - We alternate
    		if (numGreens != 0 && numReds != 0) 
    		{	
    			alternate = !alternate;
    			if (alternate)
    			{
    				gp.monster[gp.currentMap][i] = new MON_GreenSlime(gp);
    	            gp.monster[gp.currentMap][i].worldX = gp.obj[gp.currentMap][0].worldX;
    	            gp.monster[gp.currentMap][i].worldY = gp.obj[gp.currentMap][0].worldY;
    	            numGreens--;
    			} 
    			else
    			{
    				gp.monster[gp.currentMap][i] = new MON_RedSlime(gp);
    	            gp.monster[gp.currentMap][i].worldX = gp.obj[gp.currentMap][1].worldX;
    	            gp.monster[gp.currentMap][i].worldY = gp.obj[gp.currentMap][1].worldY;
    	            numReds--;
    			}
    		}
    	}
    }
}