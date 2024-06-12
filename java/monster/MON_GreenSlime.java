package monster;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity 
{
    GamePanel gp; 							// cuz of different package
    
    public int maxCount;
    
    public MON_GreenSlime(GamePanel gp) 
    {       
    	super(gp);

        this.gp = gp;
        
        maxHunger = 25;
        hunger = maxHunger;        
        maxThirst = 25;
        thirst = maxThirst;
        
        type = type_monster;
        name = "Green Slime";
        defaultSpeed = gp.rnum.nextInt(3)+1;
        speed = defaultSpeed;
        maxLife = 50;
        life = maxLife;
        attack = 0;
        defense = 0;
        
        diet = gp.keyH.form1.diet;
		System.out.println("Dieta: " + diet + ".");
        
        behaviors = gp.dbManager.getBehaviorsBySpeciesId(1);
		for(int i = 0; i < behaviors.length; i++)
		{
			System.out.println("Registros realizado correctamente:" + behaviors[i] + ".");
		}

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        attributesGS();
        defenseAttackAdjustement();
        
//        System.out.println("Mi defensa es:" + defense);
//        System.out.println("Mi ataque es:" + attack);
    }

    public void getImage()
    {
        up1 = setup("/monster/Numo_up_1",gp.tileSize,gp.tileSize);
        up2 = setup("/monster/Numo_up_2",gp.tileSize,gp.tileSize);
        down1 = setup("/monster/Numo_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/monster/Numo_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/monster/Numo_left_1",gp.tileSize,gp.tileSize);
        left2 = setup("/monster/Numo_left_2",gp.tileSize,gp.tileSize);
        right1 = setup("/monster/Numo_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("/monster/Numo_right_2",gp.tileSize,gp.tileSize);
    }

    public void setAction()
    {    	    	
    	if (active)
        {
    		if(countRndHT == maxCountRndHT || maxCountRndHT == -1)
    		{
    			hungerThirst();
    		}    		
    		
    		if(countRndSLL == maxCountRndSLL || maxCountRndSLL == -1)
	        {
	            if(hunger <= 0 || thirst <= 0) 
	            {
	                startLosingLife();	                							
	            }
	        }
    		else
            {
                countRndSLL++;
            }
    		
    		if(state == 0)
    		{
    			if(countRnd < maxCountRnd)
    			{
    				getRandomDirection(120);
    				countRnd++;
    			}
    			else
    			{
    				changeState();
    			}
    		}
    		else if(state == 1)
    		{                
                if (onPath == true)									// Mientras esté buscando un path se va mover 
                {
                	searchPath(goalCol, goalRow);
                }
                else  												// Cuando llega a su destino, onPath = falso
                {
                	changeState();
                } 			
    		}    	
    		
    		countRndHT++;
        }
    }
}
