package view;

import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.*;
import javafx.scene.image.Image ;
import javafx.util.Duration;

import java.awt.*;


public class GameOverSubscene extends SubScene
{

    public final static String SUBSCENE_IMAGE_PATH = "view/resources/gameOver.png";
    public final static int prefLabelWidth = 600;
    public final static int prefLabelHeight = 250;

    Image image;
    BackgroundImage backgroundImage;


    public GameOverSubscene()
    {
        super(new AnchorPane(),prefLabelWidth,prefLabelHeight);
        prefHeight(prefLabelHeight);
        prefWidth(prefLabelWidth);
        image = new Image(SUBSCENE_IMAGE_PATH,prefLabelWidth,prefLabelHeight,false,true);
        backgroundImage = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,null);
        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(backgroundImage));



    }


    public AnchorPane getPane()
    {
        return (AnchorPane) this.getRoot();
    }
}

