package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_NoPuddle extends Entity
{
    public static final String objName = "NoPuddle";
    
    public OBJ_NoPuddle(GamePanel gp)
    {
        super(gp);
        name = objName;
        down1 = setup("/objects/NoCharco",gp.tileSize,gp.tileSize);
        price = 75;
        collision = true;
    }
}
