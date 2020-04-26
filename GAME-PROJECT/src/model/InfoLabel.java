package model;

import java.awt.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InfoLabel extends Label
{
    public final static String FONT_PATH = "view/resources/Oswald-Medium.ttf";
    public final static String LABEL_IMAGE_PATH = "view/resources/pickLevel.png";

    public final static int prefLabelWidth = 300;
    public final static int prefLabelHeight = 50;

    Image labelImage;
    BackgroundImage labelBackgroundImage;

    public InfoLabel(String text)
    {
        setPrefWidth(prefLabelWidth);
        setPrefHeight(prefLabelHeight);

        setAlignment(Pos.CENTER);
        setText(text);
        setWrapText(true);
        setLabelFont();
        labelImage = new Image(LABEL_IMAGE_PATH,prefLabelWidth,prefLabelHeight,false,true);
        labelBackgroundImage = new BackgroundImage(labelImage,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,null);
        setBackground(new Background(labelBackgroundImage));

    }

    public void setLabelFont()
    {
        try{
            setFont(Font.loadFont(new FileInputStream(FONT_PATH),30));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana",23));
        }
    }

    public void setLabelFontSize(int fontSizeValue)
    {
        try{
            setFont(Font.loadFont(new FileInputStream(FONT_PATH),fontSizeValue));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana",fontSizeValue));
        }
    }

}



