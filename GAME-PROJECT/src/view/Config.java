package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Config
{
    public final static String smallObstacleRoadBlock_PATH = "view/resources/smallObstacles/smallObstacleRoadBlock.png";
    public  final static String smallObstacleRock_PATH = "view/resources/smallObstacles/smallObstacleRock.png";
    public  final static String bigObstacleVendingMachine_PATH="view/resources/bigObstacles/bigObstacleVendingMachine.png";

    public final static String GREEN_ARROW_PATH = "view/resources/greenArrow.png";
    public final static String BLUE_ARROW_PATH= "view/resources/blueArrow.png";
    public final static String GOLD_STAR_PATH = "view/resources/goldStar.png";
    public final static String CONTROLS_PATH = "view/resources/controls.png";

    public final static ImageView goldStar = new ImageView(GOLD_STAR_PATH);
    public final static ImageView blueArrow = new ImageView(BLUE_ARROW_PATH);
    public final static ImageView greenArrow = new ImageView(GREEN_ARROW_PATH);

    public final static ImageView smallObstacleRoadBlock = new ImageView(smallObstacleRoadBlock_PATH);
    public final static ImageView smallObstacleRock = new ImageView(smallObstacleRock_PATH);
    public final static ImageView bigObstacleVendingMachine = new ImageView(bigObstacleVendingMachine_PATH);

    public final static ImageView controlsImage = new ImageView(CONTROLS_PATH);


}
