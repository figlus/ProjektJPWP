package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class InGameLabel extends Label {

    Image labelImage;
    BackgroundImage labelBackgroundImage;
    private final static String FONT_PATH = "view/resources/Oswald-Medium.ttf";


    public InGameLabel(String text) {
        setPrefWidth(150);
        setPrefHeight(60);
        labelImage = new Image("view/resources/inGameLabel.png", 150, 60, false, true);
        labelBackgroundImage = new BackgroundImage(labelImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(labelBackgroundImage));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 10, 10, 10));
        setLabelFont();
        setText(text);

    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 15));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 15));
        }

    }

}