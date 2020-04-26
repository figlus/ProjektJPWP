package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainMenu;

import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.show();
        ConfigFile configFile = new ConfigFile();
        HashMap<String, Integer> players = configFile.loadPlayers();
        HashMap<String, Integer> scoreSave = configFile.loadScore();
        MainMenu mainMenu = new MainMenu(players, scoreSave);

        primaryStage = mainMenu.getMainStage();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
