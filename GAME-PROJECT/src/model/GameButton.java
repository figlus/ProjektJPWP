package model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameButton extends Button
{
    private final static String FONT_PATH = "view/resources/Oswald-Medium.ttf";
    private final static String BUTTON_PRESSED_STYLE="-fx-background-color:transparent; -fx-background-image:url('view/resources/greyButtonSmall.png');";
    private final static String BUTTON_RELEASED_STYLE="-fx-background-color:transparent; -fx-background-image:url('view/resources/greyButtonSmall.png');";


    public GameButton(String text) throws FileNotFoundException
    {
        setButtonFont();
        setText(text);
        setPrefWidth(150);
        setPrefHeight(50);
        setStyle(BUTTON_RELEASED_STYLE);
        initializeButtonListeners();
    }



    private void setButtonFont() throws FileNotFoundException {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        }catch (FileNotFoundException  e){
            setFont(Font.font("Verdana",23));
        }
    }

    private void setButtonPressedStyle()
    {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(50);
        setLayoutY(getLayoutY()+3);
    }

    private void setButtonReleasedStyle()
    {
        setStyle(BUTTON_RELEASED_STYLE);
        setPrefHeight(50);
        setLayoutY(getLayoutY()-3);
    }

    private void initializeButtonListeners()
    {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    setButtonPressedStyle();
                }
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    setButtonReleasedStyle();
                }
            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(null);
            }
        });

    }








}