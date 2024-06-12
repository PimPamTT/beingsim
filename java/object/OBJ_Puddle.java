package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Puddle extends Entity
{
    GamePanel gp;
    public static final String objName = "Puddle";

    public OBJ_Puddle(GamePanel gp)
    {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = objName;
        down1 = setup("/objects/Charco",gp.tileSize,gp.tileSize);
        description = "[Tent]\nYou can sleep until \nnext morning.";
        price = 100;
        stackable = true;
        collision = true;
    }
}
