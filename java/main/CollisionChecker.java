package main;

import entity.Entity;

public class CollisionChecker 
{
    GamePanel gp;

    public CollisionChecker(GamePanel gp)
    {
        this.gp = gp;
    }
    
    public void checkTile(Entity entity) 
    {
        if (entity == null || gp == null || gp.tileM == null || gp.tileM.mapTileNum == null || gp.tileM.tile == null) 
        {
            return; 						// or handle the error appropriately
        }

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // Ensure indices are within bounds
        if (entityLeftCol < 0) entityLeftCol = 0;
        if (entityRightCol >= gp.tileM.mapTileNum[gp.currentMap][0].length) entityRightCol = gp.tileM.mapTileNum[gp.currentMap][0].length - 1;
        if (entityTopRow < 0) entityTopRow = 0;
        if (entityBottomRow >= gp.tileM.mapTileNum[gp.currentMap].length) entityBottomRow = gp.tileM.mapTileNum[gp.currentMap].length - 1;

        int tileNum1, tileNum2;

        // Use a temporal direction when it's being knockbacked
        String direction = entity.direction;
        if (entity.knockBack) 
        {
            direction = entity.knockBackDirection;
        }

        switch (direction)
        {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if (entityTopRow < 0) entityTopRow = 0;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (entityBottomRow >= gp.tileM.mapTileNum[gp.currentMap].length) entityBottomRow = gp.tileM.mapTileNum[gp.currentMap].length - 1;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (entityLeftCol < 0) entityLeftCol = 0;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (entityRightCol >= gp.tileM.mapTileNum[gp.currentMap][0].length) entityRightCol = gp.tileM.mapTileNum[gp.currentMap][0].length - 1;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                break;

            default:
                return; // or handle the error appropriately
        }

        // Ensure tile numbers are within valid range before checking collision
        if (tileNum1 < 0 || tileNum1 >= gp.tileM.tile.length || tileNum2 < 0 || tileNum2 >= gp.tileM.tile.length) 
        {
            return; 														// or handle the error appropriately
        }

        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
        {
            entity.collisionOn = true;
        }
    }
    
    public int checkObject(Entity entity) 
    {
        int index = 999;

        //Use a temporal direction when it's being knockbacked
        String direction = entity.direction;
        if(entity.knockBack == true)
        {
            direction = entity.knockBackDirection;
        }

        for(int i = 0;i < gp.obj[1].length; i++)
        {
            if(gp.obj[gp.currentMap][i] != null)
            {
                // get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // get the object's solid area position
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;       //entity's solid area and obj's solid area is different.
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

                switch (direction)
                {
                    case "up" :
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down" :
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left" :
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right" :
                        entity.solidArea.x += entity.speed;
                        break;
                }
                
                if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) 					// Checking if Entity rectangle and Object rectangle intersects.
                {
                    if(gp.obj[gp.currentMap][i].collision == true) 										// Collision (Player can't enter through a door.)
                    {
                        entity.collisionOn = true;  
                        
                        if(entity.state == 0)
                        {
                        	entity.changeState();
                        }
                        else
                        {
                        	entity.onPath = false;
                        }
                        
                        if(gp.obj[gp.currentMap][i].name.equals("Bone"))
                        {
                        	if(entity.diet.equals("carnivore"))
                        	{
                        		if(entity.hunger <= entity.maxHunger)
                            	{
                            		entity.hunger += 5;
                            		
                            		if(entity.life < entity.maxLife)
                            		{
                            			entity.life += gp.rnum.nextInt(5)+1;
                            			//System.out.println("Mi vida es: " + entity.life);
                            		}
                            	}
                        	}                        	                        	
                        }
                        
                        if(gp.obj[gp.currentMap][i].name.equals("NoBone"))
                        {
                        	if(entity.diet.equals("carnivore"))
                        	{
	                        	if(entity.hunger > 0)
	                        	{
	                        		entity.hunger -= 2;
	                        	}   
                        	}
                        }
                        
                        if(gp.obj[gp.currentMap][i].name.equals("Fruit"))
                        {
                        	if(entity.diet.equals("herbivore"))
                        	{
                        		if(entity.hunger <= entity.maxHunger)
                            	{
                            		entity.hunger += 5;
                            		
                            		if(entity.life < entity.maxLife)
                            		{
                            			entity.life += gp.rnum.nextInt(5)+1;
                            			System.out.println("Mi vida es: " + entity.life);
                            		}
                            	}
                        	}                        	                        	
                        }
                        
                        if(gp.obj[gp.currentMap][i].name.equals("NoFruit"))
                        {
                        	if(entity.diet.equals("herbivore"))
                        	{
	                        	if(entity.hunger > 0)
	                        	{
	                        		entity.hunger -= 2;
	                        	}   
                        	}
                        }
                        
                        if(gp.obj[gp.currentMap][i].name.equals("Puddle"))
                        {
                        	if(entity.thirst <= entity.maxThirst)
                        	{
                        		entity.thirst += 5;
                        		
                        		if(entity.life < entity.maxLife)
                        		{
                        			entity.life += gp.rnum.nextInt(5)+1;
                        			//System.out.println("Mi vida es: " + entity.life);
                        		}
                        	}
                        }
                        
                        if(gp.obj[gp.currentMap][i].name.equals("NoPuddle"))
                        {
                        	if(entity.thirst > 0)
                        	{
                        		entity.thirst -= 2;
                        	}
                        }
                    }   
                }
                
                entity.solidArea.x = entity.solidAreaDefaultX; 											// Reset
                entity.solidArea.y = entity.solidAreaDefaultY;

                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;     	// Reset
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        
        return index;
    }

    // NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[][] target) // , String name)
    {    	
    	int index = 999;   										// no collision returns 999;
        int damage = 0;
    	
        //Use a temporal direction when it's being knockbacked
        String direction = entity.direction;
        if(entity.knockBack == true)
        {
            direction = entity.knockBackDirection;
        }

        for(int i = 0;i < target[1].length; i++)
        {
            if(target[gp.currentMap][i] != null && target[gp.currentMap][i].active == true)
            {
            	if(!target[gp.currentMap][i].name.equals(entity.name))
            	{
	                // get entity's solid area position
	                entity.solidArea.x = entity.worldX + entity.solidArea.x;
	                entity.solidArea.y = entity.worldY + entity.solidArea.y;
	
	                // get the object's solid area position
	                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;       //entity's solid area and obj's solid area is different.
	                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;
	
	                switch (direction)
	                {
	                    case "up" :
	                        entity.solidArea.y -= entity.speed;
	                        break;
	                    case "down" :
	                        entity.solidArea.y += entity.speed;
	                        break;
	                    case "left" :
	                        entity.solidArea.x -= entity.speed;
	                        break;
	                    case "right" :
	                        entity.solidArea.x += entity.speed;
	                        break;
	                }
	
	                if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea))
	                {
	                    if(target[gp.currentMap][i] != entity) 			// avoid entity includes itself as a collision target
	                    {
	                        entity.collision = true;
	                        index = i;   								// Non-player characters cannot pickup objects.
	                    
	                        // cambiar la dirección del primero
	                        if(entity.state == 0)	                        
	                        {
	                        	entity.changeState();
	                        }
	                        else
	                        {
	                        	entity.onPath = false;
	                        }
	                        
	                        // función para bajar la vida
	                        if(entity.life > 0)
	                        {
	                        	damage = target[gp.currentMap][i].attack - entity.defense;
	                        	
	                        	if(damage <= 0)
	                        	{
	                        		damage = gp.rnum.nextInt(3)+1;
	                        	}
	                        	
	                        	entity.life -= damage;
	                        	
		                        //System.out.println("Me han atacado, mi vida es: " + entity.life);
	                        }                        
	                    }
	                }
	                entity.solidArea.x = entity.solidAreaDefaultX;		//Reset
	                entity.solidArea.y = entity.solidAreaDefaultY;
	
	                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;     //Reset
	                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
	            }
        	}
        }
        return index;
    }
}
