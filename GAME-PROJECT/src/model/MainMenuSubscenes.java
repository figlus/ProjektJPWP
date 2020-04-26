package model;

import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.*;
import javafx.scene.image.Image ;
import javafx.util.Duration;

import java.awt.*;

public class MainMenuSubscenes extends SubScene
{
    private final static String FONT_PATH="view/resources/Oswald-Medium.ttf";
    private final static String BACKGROUND_IMAGE_PATH="view/resources/transparentPanel.png";
    private final static int subsceneBackgroundprefWidth=500;
    private final static int subSceneBackgroundprefHeight=800;

    private boolean isHidden;
    public MainMenuSubscenes()
    {
     super(new AnchorPane(),subsceneBackgroundprefWidth,subSceneBackgroundprefHeight);
     prefWidth(subsceneBackgroundprefWidth);
     prefHeight(subSceneBackgroundprefHeight);

     Image backgroundImage = new Image(BACKGROUND_IMAGE_PATH,subsceneBackgroundprefWidth,subSceneBackgroundprefHeight,false,true);
     BackgroundImage image = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
             BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
     AnchorPane root2 = (AnchorPane) this.getRoot();
     root2.setBackground(new Background(image));
     setLayoutX(1720);
     setLayoutY(150);
     isHidden = true;
    }

    public void moveMainMenuSubscene()
    {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        transition.setToX(-1080);
        if(isHidden==true)
        {
            transition.setToX(-1080);
            isHidden=false;
        }
        else
        {
            transition.setToX(0);
            isHidden=true;
        }
        transition.play();


    }
    public AnchorPane getPane(){return (AnchorPane) this.getRoot();}
}



