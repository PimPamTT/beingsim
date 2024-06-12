package object;

import entity.Entity;
import main.AssetSetter;
import main.GamePanel;

import java.awt.*;

public class OBJ_Fruit extends Entity 
{
    GamePanel gp;
    public static final String objName = "Fruit";
    
    public OBJ_Fruit(GamePanel gp)
    {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        name = objName;
        down1 = setup("/objects/Fruta",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nIt opens a door.";
        price = 350;
        stackable = true;
        collision = true;
    }
}
