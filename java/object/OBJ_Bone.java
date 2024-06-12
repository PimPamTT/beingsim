package object;

import entity.Entity;
import main.AssetSetter;
import main.GamePanel;

import java.awt.*;

public class OBJ_Bone extends Entity 
{
    GamePanel gp;
    public static final String objName = "Bone";
    
    public OBJ_Bone(GamePanel gp)
    {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        name = objName;
        down1 = setup("/objects/Hueso",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nIt opens a door.";
        price = 350;
        stackable = true;
        collision = true;
    }
}
