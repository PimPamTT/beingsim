package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_NoBone extends Entity
{
    public static final String objName = "NoBone";

    public OBJ_NoBone(GamePanel gp)
    {
        super(gp);

        type = type_light;
        name = objName;
        down1 = setup("/objects/NoHueso",gp.tileSize,gp.tileSize);
        description = "[Lantern]\nIlluminates your \nsurroundings.";
        price = 200;
        lightRadius = 350;
        collision = true;
    }
}
