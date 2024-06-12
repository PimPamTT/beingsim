package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_NoFruit extends Entity
{
    public static final String objName = "NoFruit";

    public OBJ_NoFruit(GamePanel gp)
    {
        super(gp);

        type = type_light;
        name = objName;
        down1 = setup("/objects/NoFruta",gp.tileSize,gp.tileSize);
        description = "[Lantern]\nIlluminates your \nsurroundings.";
        price = 200;
        lightRadius = 350;
        collision = true;
    }
}
