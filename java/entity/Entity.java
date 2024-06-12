package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;

import ai.PathFinder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Entity 
{
	public boolean collidingObject = false;
	
	public boolean dead = false;
	public boolean lightUpdated = false;
	public Entity currentLight;
	
    GamePanel gp;
	public int state;
	public int countRnd = 0;
	public int maxCountRnd = 0;
	
	public int countRndHT = 0;
	public int maxCountRndHT = -1;
	
	public int countRndSLL = 0;
	public int maxCountRndSLL = -1;
	
	public int goalCol = -1;
	public int goalRow = -1;
	
    public BufferedImage up1,up2,down1,down2,left1,left2,right1,right2;
    public BufferedImage attackUp1,attackUp2,attackDown1,attackDown2,attackLeft1,attackLeft2,attackRight1,attackRight2,guardUp,guardDown,guardLeft,guardRight;
    public BufferedImage image, image2, image3;    
    public Rectangle solidArea = new Rectangle(0,0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public String dialogues[][] = new String[20][20];
    public Entity attacker;
    public Entity linkedEntity; 				// link big rock and metal plate
    public boolean cameraSelected = false;      // Si es el seleccionado en la cámara
    
    public boolean active = false;   
    
    // PATHFINDER
    public PathFinder pFinder; 
    
    // STATE
    public int worldX,worldY; 					// player's position on the map
    public int screenX, screenY;
    public String direction = "down";
    public int spriteNum = 1;
    public int dialogueSet = 0;
    public int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public String knockBackDirection;
    public boolean guarding = false;
    public boolean transparent = false; 		// invincible when only gets damage
    public boolean offBalance = false;
    public Entity loot;
    public boolean opened = false;
    public boolean inRage = false;
    public boolean sleep = false;
    public boolean drawing = true;

    //COUNTER
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    public int hpBarCounter = 0;
    public int hnBarCounter = 0;
    public int thBarCounter = 0;
    int knockBackCounter = 0;
    public int guardCounter = 0;
    int offBalanceCounter = 0;

    //CHARACTER ATTRIBUTES
    public String name;
    public String diet;				// db
    public String[] behaviors;		// db
    public int defaultSpeed;
    public int speed;
    public int maxLife;
    public int life;
    public int attack;
    public int defense;
    public int hunger;
    public int maxHunger;
    public int thirst;
    public int maxThirst;
    public int motion1_duration;
    public int motion2_duration;
    
    // ITEM ATTRIBUTES
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int value;
    public int price;
    public int knockBackPower;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;

    // TYPE
    public int type; 								// 0=player, 1=npc, 2=monster etc.
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_obstacle = 8;
    public final int type_light = 9;
    public final int type_pickaxe = 10;

    public Entity(GamePanel gp)
    {
        this.gp = gp;
        pFinder = new PathFinder(gp);				// Creamos un objeto para usar los path finders para cada entidad
    }
    
    public void getImage()
    {
    	
    }
    
    public int getScreenX()
    {
        return screenX;
    }
    
    public int getScreenY()
    {
        return screenY;
    }
    
    public int getLeftX()
    {
        return worldX + solidArea.x;
    }
    
    public int getRightX()
    {
        return worldX + solidArea.width + solidArea.width;
    }
    
    public int getTopY()
    {
        return worldY + solidArea.y;
    }
    
    public int getBottomY()
    {
        return worldY + solidArea.y + solidArea.height;
    }
    
    public int getCol()
    {
        return (worldX + solidArea.x) / gp.tileSize;
    }
    
    public int getRow()
    {
        return (worldY + solidArea.y) / gp.tileSize;
    }
    
    public int getCenterX()
    {
        int centerX = worldX + left1.getWidth()/2;
        return centerX;
    }
    
    public int getCenterY()
    {
        int centerY = worldY + up1.getWidth()/2;
        return centerY;
    }
    
    public int getXdistance(Entity target)
    {
        int xDistance = Math.abs(getCenterX() - target.getCenterX());
        return xDistance;
    }
    
    public int getYdistance(Entity target)
    {
        int yDistance = Math.abs(getCenterY() - target.getCenterY());
        return yDistance;
    }
    
    public int getTileDistance(Entity target)
    {
        int tileDistance = (getXdistance(target) + getYdistance(target))/gp.tileSize;
        return tileDistance;
    }
    
    public int getGoalCol(Entity target)
    {
        int goalCol = (target.worldX + target.solidArea.x) / gp.tileSize;
        return goalCol;
    }
    
    public int getGoalRow(Entity target)
    {
        int goalRow = (target.worldY + target.solidArea.y) / gp.tileSize;
        return goalRow;
    }
    
    public void resetCounter()
    {
        spriteCounter = 0;
        actionLockCounter = 0;
        invincibleCounter = 0;
        shotAvailableCounter = 0;
        dyingCounter = 0;
        hpBarCounter = 0;
        knockBackCounter = 0;
        guardCounter = 0;
        offBalanceCounter = 0;
    }
    
    public void setAction()
    {

    }
    
    public void move(String direction)
    {

    }
    
    public boolean use(Entity entity)
    {
        return false;
        // return "true" if you used the item and "false" if you failed to use it.
    }
    
    public Color getParticleColor()
    {
        Color color = null;
        //Sub-class specifications
        return color;
    }
    
    public int getParticleSize()
    {
        int size = 0; //pixels
        //Sub-class specifications
        return size;
    }
    
    public int getParticleSpeed()
    {
        int speed = 0;
        // Sub-class specifications
        return speed;
    }
    
    public int getParticleMaxLife()
    {
        int maxLife = 0;
        // Sub-class specifications
        return maxLife;
    }
    
    public void checkCollision()
    {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this);
        gp.cChecker.checkEntity(this, gp.monster); // , "Green Slime");
//        gp.cChecker.checkEntity(this, gp.monster, "Red Slime");
    }
    
    public void update()
    {
        if(sleep == false)
        {
            if(knockBack == true)
            {
                checkCollision();
                if(collisionOn == true)
                {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                }
                else if(collisionOn == false)
                {
                    switch (knockBackDirection)
                    {
                        case "up" :
                            worldY -= speed;
                            break;

                        case "down" :
                            worldY += speed;
                            break;

                        case "left" :
                            worldX -= speed;
                            break;

                        case "right" :
                            worldX += speed;
                            break;
                    }
                }
                knockBackCounter++;
                if(knockBackCounter == 10)
                {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                }
            }
            else
            {
                setAction();
                checkCollision();

                if(collisionOn == false)
                {
                    switch (direction)
                    {
                        case "up" :
                            worldY -= speed;
                            break;

                        case "down" :
                            worldY += speed;
                            break;

                        case "left" :
                            worldX -= speed;
                            break;

                        case "right" :
                            worldX += speed;
                            break;
                    }
                }
                
                spriteCounter++;
                
                if (spriteCounter > 24) {
                    if (spriteNum == 1)                  // Every 12 frames sprite num changes.
                    {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;                  // spriteCounter reset
                }
            }
            
            // Like player's invincible method
            if(invincible == true)
            {
                invincibleCounter++;
                if(invincibleCounter > 40)
                {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
            
            if(shotAvailableCounter < 30)
            {
                shotAvailableCounter++;
            }
            
            if(offBalance == true)
            {
                offBalanceCounter++;
                if(offBalanceCounter > 60)
                {
                    offBalance = false;
                    offBalanceCounter = 0;
                }
            }
        }
    }
    
    public void checkStartChasingOrNot(Entity target, int distance, int rate)
    {
        if(getTileDistance(target) < distance)
        {
            int i = new Random().nextInt(rate);
            if(i == 0)
            {
                onPath = true;
            }
        }
    }
    
    public void checkStopChasingOrNot(Entity target, int distance, int rate)
    {
        if(getTileDistance(target) > distance)
        {
            int i = new Random().nextInt(rate);
            if(i == 0)
            {
                onPath = false;
            }
        }
    }
    
    public void getRandomDirection(int interval)
    {
        actionLockCounter++;

        if(actionLockCounter > interval)
        {
            Random random = new Random();
            int i = random.nextInt(100) + 1;  							// pick up  a number from 1 to 100
            if(i <= 25){direction = "up";}
            if(i>25 && i <= 50){direction = "down";}
            if(i>50 && i <= 75){direction = "left";}
            if(i>75 && i <= 100){direction = "right";}
            actionLockCounter = 0;									 	// reset
        }
    }
    
    public String getOppositeDirection(String direction)
    {
        String oppositeDirection = "";

        switch (direction)
        {
            case "up": oppositeDirection = "down";break;
            case "down": oppositeDirection = "up";break;
            case "left": oppositeDirection = "right";break;
            case "right": oppositeDirection = "left";break;
        }

        return oppositeDirection;
    }
    
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower)
    {
        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;
    }
    
    public boolean inCamera()
    {
        boolean inCamera = false;
        if(     worldX + gp.tileSize*5 > gp.camera.worldX - gp.camera.screenX && 
                worldX - gp.tileSize < gp.camera.worldX + gp.camera.screenX &&
                worldY + gp.tileSize*5 > gp.camera.worldY - gp.camera.screenY &&
                worldY - gp.tileSize < gp.camera.worldY + gp.camera.screenY)
        {
            inCamera = true;
        }
        return inCamera;
    }
    
    public void draw(Graphics2D g2)
    {
        BufferedImage image= null;

        if(inCamera() == true)
        {
            int tempScreenX = worldX - gp.camera.worldX + gp.camera.screenX;
            int tempScreenY = worldY - gp.camera.worldY + gp.camera.screenY;

            switch (direction)
            {
                case "up" :
                    if(attacking == false) 										// Normal walking sprites
                    {
                        if(spriteNum == 1){image = up1;}
                        if(spriteNum == 2) {image = up2;}
                    }
                    
                    break;

                case "down" :
                    if(attacking == false) 										// Normal walking sprites
                    {
                        if(spriteNum == 1){image = down1;}
                        if(spriteNum == 2){image = down2;}
                    }
                    
                    break;

                case "left" :
                    if(attacking == false) 										// Normal walking sprites
                    {
                        if(spriteNum == 1) {image = left1;}
                        if(spriteNum == 2) {image = left2;}
                    }
                    
                    break;

                case "right" :
                    if(attacking == false) 										// Normal walking sprites
                    {
                        if(spriteNum == 1) {image = right1;}
                        if(spriteNum == 2) {image = right2;}
                    }
                    break;
            }
            
            g2.drawImage(image, tempScreenX, tempScreenY, null);
        }
    }
    
    // Every 5 frames switch alpha between 0 and 1
    public void dyingAnimation(Graphics2D g2)
    {
        dyingCounter++;
        int i = 5;    //interval

        if(dyingCounter <= i) {changeAlpha(g2,0f);}                             //If you want add death animation or something like that, you can use your sprites instead of changing alpha inside of if statements
        if(dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2,1f);}
        if(dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2,0f);}
        if(dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2,1f);}
        if(dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2,0f);}
        if(dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2,1f);}
        if(dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2,0f);}
        if(dyingCounter > i*7 && dyingCounter <= i*8) {changeAlpha(g2,1f);}
        if(dyingCounter > i*8)
        {
            alive = false;
        }
    }
    
    public void changeAlpha(Graphics2D g2, float alphaValue)
    {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaValue));
    }
    
    public BufferedImage setup(String imagePath, int width, int height)
    {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try
        {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image,width,height);   //it scales to tilesize , will fix for player attack(16px x 32px) by adding width and height
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
    
    public void searchPath(int goalCol, int goalRow)
    {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;
        pFinder.setNodes(startCol,startRow,goalCol,goalRow,this);
        if(pFinder.search() == true)
        {
            // Next WorldX and WorldY
            int nextX = pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = pFinder.pathList.get(0).row * gp.tileSize;

            // Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            // TOP PATH
            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize)
            {
                direction = "up";
            }
            // BOTTOM PATH
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize)
            {
                direction = "down";
            }
            // RIGHT - LEFT PATH
            else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize)
            {
                // either left or right
                // LEFT PATH
                if(enLeftX > nextX)
                {
                    direction = "left";
                }
                // RIGHT PATH
                if(enLeftX < nextX)
                {
                    direction = "right";
                }
            }
            // OTHER EXCEPTIONS
            else if(enTopY > nextY && enLeftX > nextX)
            {
                // up or left
                direction = "up";
                checkCollision();
                if(collisionOn == true)
                {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX)
            {
                // up or right
                direction = "up";
                checkCollision();
                if(collisionOn == true)
                {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX)
            {
                // down or left
                direction = "down";
                checkCollision();
                if(collisionOn == true)
                {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX)
            {
                // down or right
                direction = "down";
                checkCollision();
                if(collisionOn == true)
                {
                    direction = "right";
                }
            }
            // for following player, disable this. It should be enabled when npc walking to specified location
            int nextCol = pFinder.pathList.get(0).col;
            int nextRow = pFinder.pathList.get(0).row;
            if(nextCol == goalCol && nextRow == goalRow)
            {
                onPath = false;
            }
        }
    }
    
    public int getDetected(Entity user, Entity target[][], String targetName)
    {
        int index = 999;

        //Check the surrounding object
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction)
        {
            case "up" : nextWorldY = user.getTopY() - gp.camera.speed; break;
            case "down": nextWorldY = user.getBottomY() + gp.camera.speed; break;
            case "left": nextWorldX = user.getLeftX() - gp.camera.speed; break;
            case "right": nextWorldX = user.getRightX() + gp.camera.speed; break;
        }
        int col = nextWorldX/gp.tileSize;
        int row = nextWorldY/gp.tileSize;

        for(int i = 0; i < target[1].length; i++)
        {
            if(target[gp.currentMap][i] != null)
            {
                if (target[gp.currentMap][i].getCol() == col                                //checking if player 1 tile away from target (key etc.) (must be same direction)
                        && target[gp.currentMap][i].getRow() == row
                            && target[gp.currentMap][i].name.equals(targetName))
                {
                    index = i;
                    break;
                }
            }

        }
        return  index;
    }
    
    public int[] getRandomEmptyPosition(int mapNum)
    {
        int col, row;
        
        do 
        {
            col = gp.rnum.nextInt(50);
            row = gp.rnum.nextInt(50);
        } while (isPositionOccupied(mapNum, col * gp.tileSize, row * gp.tileSize));
        
        return new int[]{col, row};
    }

    
    public boolean isPositionOccupied(int mapNum, int x, int y) 
    {        
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;
        
        if (gp.tileM.tile[gp.tileM.mapTileNum[mapNum][col][row]].collision) 
        {
            return true;
        }
        
        return false;
    }
    
    public void changeState()
    {
    	state = gp.rnum.nextInt(2);   	
    	
    	if(state == 0) 			// RANDOM
    	{
    		//System.out.println("Estamos en random.");
    		maxCountRnd = (gp.rnum.nextInt(10)+1) * gp.FPS;
    		countRnd = 0;
    		onPath = false;
    	}
    	else if(state == 1) 	// PATH 
    	{
    		//System.out.println("Estamos en path.");
    		
    		if (goalCol == -1 && goalRow == -1)					// La primera vez buscamos un objetivo
            {
                int[] pos = getRandomEmptyPosition(0); 			// Busca una nueva posiciçón
                goalCol = pos[0];
                goalRow = pos[1];
                onPath = true;
                //System.out.println("Camino: Col/Row" + goalCol + "/" + goalRow);
            }
    		else
    		{
    			pFinder.resetNodes();							// Resetea los nodos
            	int[] pos = getRandomEmptyPosition(0); 			// Busca una nueva posiciçón
                goalCol = pos[0];								// Seteamos los objetivos
                goalRow = pos[1];
                onPath = true;
                //System.out.println("Nuevo camino: Col/Row" + goalCol + "/" + goalRow);
    		}   		
    	}
    }    
    
    public void hungerThirst()
    {
    	maxCountRndHT = (gp.rnum.nextInt(10)+1) * gp.FPS;
		countRndHT = 0;
		
		if(hunger > 0)
		{
			hunger -= gp.rnum.nextInt(5)+1;
			//System.out.println("Hunger actual: " + hunger);
		}
		
		if(thirst > 0)
		{
			thirst -= gp.rnum.nextInt(5)+1;
			//System.out.println("Thirst actual: " + thirst);
		}		
    }
    
    public void startLosingLife()
    {
    	if(life <= 0)
    	{
    		active = false;
    		System.out.println("Entidad muerta.");
    		dead = true;
    		gp.playSE(1);
    		
    		if (gp.gameState == gp.spectatorState && cameraSelected == true)
    		{
    			cameraSelected = false;
    			gp.gameState = gp.playState;
    		}
    	}
    	else
    	{
    		maxCountRndSLL = (gp.rnum.nextInt(10)+1) * gp.FPS;
    		countRndSLL = 0;
        	
        	if(hunger <= 0 || thirst <= 0)
        	{
        		if(life > 0)
        		{    			
        			life -= gp.rnum.nextInt(2)+1;
        			//System.out.println("Life actual: " + life);
        		}
        	}
    	}    	    	
    }
    
    public void defenseAttackAdjustement()
    {
    	if(defense <= 0)
    	{
    		defense = gp.rnum.nextInt(5)+1;
    	}
    	
    	if(attack <= 0)
    	{
    		attack = gp.rnum.nextInt(5)+1;
    	}
    }
    
    public void attributesGS()
    {
    	if(gp.behaviorsSelectedGS == 1)
    	{
    		if(behaviors[0].equals("aggresive"))
    		{
        		defense -= 1*3;
        		attack += 2*3;
    		}
    		
    		if(behaviors[0].equals("calm"))
    		{
        		defense += 1*3;
        		attack -= 2*3;
    		}
    		
    		if(behaviors[0].equals("smart"))
    		{
        		defense += 1*3;
        		attack += 1*3;
    		}
    		
    		if(behaviors[0].equals("dumb"))
    		{

        		defense -= 1*3;
        		attack -= 1*3;
    		}
    		
    		if(behaviors[0].equals("farsighted"))
    		{
        		defense += 2*3;
        		attack -= 1*3;
    		}
    		
    		if(behaviors[0].equals("careless"))
    		{
        		defense -= 2*3;
        		attack += 1*3;
    		}
    	}
    	
    	if(gp.behaviorsSelectedGS == 2)
    	{
    		for(int i = 0; i < gp.behaviorsSelectedGS; i++)
    		{
    			if(behaviors[i].equals("aggresive"))
        		{
            		defense -= 1*2;
            		attack += 2*2;
        		}
        		
        		if(behaviors[i].equals("calm"))
        		{
            		defense += 1*2;
            		attack -= 2*2;
        		}
        		
        		if(behaviors[i].equals("smart"))
        		{
            		defense += 1*2;
            		attack += 1*2;
        		}
        		
        		if(behaviors[i].equals("dumb"))
        		{
            		defense -= 1*2;
            		attack -= 1*2;
        		}
        		
        		if(behaviors[i].equals("farsighted"))
        		{
            		defense += 2*2;
            		attack -= 1*2;
        		}
        		
        		if(behaviors[i].equals("careless"))
        		{
            		defense -= 2*2;
            		attack += 1*2;
        		}
    		}
    	}
    	
    	if(gp.behaviorsSelectedGS == 3)
    	{
    		for(int i = 0; i < gp.behaviorsSelectedGS; i++)
    		{
    			if(behaviors[i].equals("aggresive"))
        		{
            		defense -= 1;
            		attack += 2;
        		}
        		
        		if(behaviors[i].equals("calm"))
        		{
            		defense += 1;
            		attack -= 2;
        		}
        		
        		if(behaviors[i].equals("smart"))
        		{
            		defense += 1;
            		attack += 1;
        		}
        		
        		if(behaviors[i].equals("dumb"))
        		{
            		defense -= 1;
            		attack -= 1;
        		}
        		
        		if(behaviors[i].equals("farsighted"))
        		{
            		defense += 2;
            		attack -= 1;
        		}
        		
        		if(behaviors[i].equals("careless"))
        		{
            		defense -= 2;
            		attack += 1;
        		}
    		}
    	}
    }
    	
	public void attributesRS()
	{        	
    	if(gp.behaviorsSelectedRS == 1)
    	{
    		if(behaviors[0].equals("aggresive"))
    		{
        		defense -= 1*3;
        		attack += 2*3;
    		}
    		
    		if(behaviors[0].equals("calm"))
    		{
        		defense += 1*3;
        		attack -= 2*3;
    		}
    		
    		if(behaviors[0].equals("smart"))
    		{
        		defense += 1*3;
        		attack += 1*3;
    		}
    		
    		if(behaviors[0].equals("dumb"))
    		{

        		defense -= 1*3;
        		attack -= 1*3;
    		}
    		
    		if(behaviors[0].equals("farsighted"))
    		{
        		defense += 2*3;
        		attack -= 1*3;
    		}
    		
    		if(behaviors[0].equals("careless"))
    		{
        		defense -= 2*3;
        		attack += 1*3;
    		}
    	}
    	
    	if(gp.behaviorsSelectedRS == 2)
    	{
    		for(int i = 0; i < gp.behaviorsSelectedGS; i++)
    		{
    			if(behaviors[i].equals("aggresive"))
        		{
            		defense -= 1*2;
            		attack += 2*2;
        		}
        		
        		if(behaviors[i].equals("calm"))
        		{
            		defense += 1*2;
            		attack -= 2*2;
        		}
        		
        		if(behaviors[i].equals("smart"))
        		{
            		defense += 1*2;
            		attack += 1*2;
        		}
        		
        		if(behaviors[i].equals("dumb"))
        		{
            		defense -= 1*2;
            		attack -= 1*2;
        		}
        		
        		if(behaviors[i].equals("farsighted"))
        		{
            		defense += 2*2;
            		attack -= 1*2;
        		}
        		
        		if(behaviors[i].equals("careless"))
        		{
            		defense -= 2*2;
            		attack += 1*2;
        		}
    		}
    	}
        
    	if(gp.behaviorsSelectedRS == 3)
    	{
    		for(int i = 0; i < gp.behaviorsSelectedGS; i++)
    		{
    			if(behaviors[i].equals("aggresive"))
        		{
            		defense -= 1;
            		attack += 2;
        		}
        		
        		if(behaviors[i].equals("calm"))
        		{
            		defense += 1;
            		attack -= 2;
        		}
        		
        		if(behaviors[i].equals("smart"))
        		{
            		defense += 1;
            		attack += 1;
        		}
        		
        		if(behaviors[i].equals("dumb"))
        		{
            		defense -= 1;
            		attack -= 1;
        		}
        		
        		if(behaviors[i].equals("farsighted"))
        		{
            		defense += 2;
            		attack -= 1;
        		}
        		
        		if(behaviors[i].equals("careless"))
        		{
            		defense -= 2;
            		attack += 1;
        		}
    		}
    	}
    }
}
