package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Base extends Entity 
{
    GamePanel gp;
    public static final String objName = "Base";

    public OBJ_Base(GamePanel gp)
    {
        super(gp);
        this.gp = gp;
        type = type_obstacle;
        name = objName;
        down1 = setup("/objects/Base",gp.tileSize,gp.tileSize);
        collision = false;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        price = 35;
    }
}
