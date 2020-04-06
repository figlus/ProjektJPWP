package view;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import model.*;
import sample.Main;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class MainMenu
{
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private List<GameButton> mainMenuButtons;
    private final static int MENU_BUTTONS_START_X=50;
    private final static int MENU_BUTTONS_START_Y=100+50;

    private MainMenuSubscenes creditsSubscene;
    private MainMenuSubscenes helpSubscene;
    private MainMenuSubscenes sceneToHide;
    private MainMenuSubscenes carPickerSubscene;

    MainMenuSubscenes loginSubscene;
    MainMenuSubscenes levelPickerSubScene;
    private List<GameButton> pickLevelButtons;


    private List<CarPicker> carList;
    private CAR pickedCar;

    private InfoLabel totalCollectedPointsLabel;
    public static int totalCollectedPointsValue;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private AnimationTimer mainMenuTimer;

    public static boolean isLevelOneCopleted = false;
    public static boolean isLevelTwoCompleted = false;
    public static boolean isLevelThreeCompleted = false;
    public static boolean isLeveLFourCompleted = false;
    public static boolean isLevelFiveCompleted = false;


    private HashMap<String, Integer> players;

    private AudioClip mainMenuButtonSound;

    public MainMenu(HashMap<String, Integer> players) throws FileNotFoundException
    {   this.players = players;
        mainPane = new AnchorPane();
        mainStage = new Stage();
        mainScene = new Scene(mainPane,1200,1000);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Game");
        mainMenuButtons = new ArrayList<>();
        pickLevelButtons = new ArrayList<>();

        createBackground();
        createMainMenuSubscenes(); //has to be before createButtons() function
        createButtons();
        createTotalCollectedPointsLabel();
        createMainMenuSounds();
        createMainMenuLoop();


        carPickerSubscene.getPane().getChildren().add(createCarPicker());







    }
    private void createMainMenuSounds()
    {
        mainMenuButtonSound = new AudioClip(Paths.get("menuButtonClick.mp3").toUri().toString());
    }

    private void createMainMenuLoop()
    {
        mainMenuTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                totalCollectedPointsLabel.setText("Points: "+totalCollectedPointsValue);
            }
        };
        mainMenuTimer.start();
    }

    private void createTotalCollectedPointsLabel()
    {
        totalCollectedPointsLabel = new InfoLabel("Total Points: "+ totalCollectedPointsValue);
        mainPane.getChildren().add(totalCollectedPointsLabel);
        totalCollectedPointsLabel.setLayoutY(950);
    }

    private VBox createCarPicker()
    {
        VBox box = new VBox();
        box.setSpacing(20);
        carList = new ArrayList<>();

        for(CAR car:CAR.values())
        {
            CarPicker carToPick = new CarPicker(car);
            carList.add(carToPick);
            box.getChildren().add(carToPick);
            carToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for (CarPicker car:carList)
                    {
                        car.setIsCircleChoosen(false);
                    }

                    //musimy najpierw wybrac jakis samochod zeby sprawdzic czy nas na niego stac
                    carToPick.setIsCircleChoosen(true);
                    pickedCar = carToPick.getCar();
                    //if there is not enough points to buy better car we will use basic car LATER
                    if(totalCollectedPointsValue >= pickedCar.getPointsRequiredToUnlock() )
                    {
                        carToPick.setIsCircleChoosen(true);
                        pickedCar = carToPick.getCar();

                        totalCollectedPointsValue = totalCollectedPointsValue - pickedCar.getPointsRequiredToUnlock();
                        //pickedCar.setIsBoughtToTrue();
                    }
                    else
                    {
                        pickedCar=null;
                        carToPick.setIsCircleChoosen(false);
                        System.out.println("Za malo punktow");
                        JOptionPane.showMessageDialog(null,"Not enough points"); //narazie niech zostanie opcja z messageDialog
                    }

                }
            });

        }
        box.setLayoutX(20);
        box.setLayoutY(80);
        return box;
    }

    private void createLevelPickerSubScene()
    {
        levelPickerSubScene = new MainMenuSubscenes();
        mainPane.getChildren().add(levelPickerSubScene);

        InfoLabel pickLevelLabel = new InfoLabel("Pick Level");
        pickLevelLabel.setLayoutX(100);
        pickLevelLabel.setLayoutY(20);
        levelPickerSubScene.getPane().getChildren().add(pickLevelLabel);
    }

    private void createLoginSubScene()
    {
        loginSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(loginSubscene);

        HashMap<String, TextField>  textFieldHashMap = new HashMap<>();
        TextField userNameTextField = new TextField();
        textFieldHashMap.put("userName", userNameTextField);

        Button loginButton = new Button("LOG IN");

            loginButton.setOnAction((event) -> {
                String test = textFieldHashMap.get("userName").getText();
                boolean been = false;
                for (Map.Entry<String, Integer> entry : players.entrySet()) {
                    if (entry.getKey().equals(test)) {
                        been = true;
                        totalCollectedPointsValue = entry.getValue();
                    }
            }
            if (been){JOptionPane.showMessageDialog(null, "Logged");}
            else{JOptionPane.showMessageDialog(null, "This user does not exist");}
            });
        userNameTextField.setLayoutX(100);
        userNameTextField.setLayoutY(100);
        userNameTextField.setPrefWidth(200);
        userNameTextField.setPrefHeight(50);

        loginButton.setLayoutX(300);
        loginButton.setLayoutY(100);
        loginButton.setPrefWidth(100);
        loginButton.setPrefHeight(50);

        loginSubscene.getPane().getChildren().addAll(userNameTextField, loginButton);


        TextField newNameTextField = new TextField();
        textFieldHashMap.put("newName", newNameTextField);

        Button createButton = new Button("CREATE");
        createButton.setOnAction((event) -> {
            String test = textFieldHashMap.get("newName").getText();
            boolean been = false;
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                if (entry.getKey().equals(test)) {
                    been = true;
                }
            }
            if (been){JOptionPane.showMessageDialog(null, "This user already exists");}
            else{players.put(test,0); JOptionPane.showMessageDialog(null, "Create Player");}
        });

        newNameTextField.setLayoutX(100);
        newNameTextField.setLayoutY(300);
        newNameTextField.setPrefWidth(200);
        newNameTextField.setPrefHeight(50);

        createButton.setLayoutX(300);
        createButton.setLayoutY(300);
        createButton.setPrefWidth(100);
        createButton.setPrefHeight(50);

        loginSubscene.getPane().getChildren().addAll(newNameTextField, createButton);


        InfoLabel loginLabel1 = new InfoLabel("LOG IN");
        loginLabel1.setLayoutX(100);
        loginLabel1.setLayoutY(20);
        loginSubscene.getPane().getChildren().add(loginLabel1);
        InfoLabel loginLabel2 = new InfoLabel("CREATE NEW USER");
        loginLabel2.setLayoutX(100);
        loginLabel2.setLayoutY(200);
        loginSubscene.getPane().getChildren().add(loginLabel2);

    }

    public Stage getMainStage()
    {
        return mainStage;
    }
    public void createBackground()
    {
        Image backgroundImage = new Image("view/resources/MAIN_MENU.png",1200,1000,false,true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        mainPane.setBackground(new Background(background));
    }

    private void createMainMenuSubscenes()
    {
        creditsSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(creditsSubscene);

        helpSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(helpSubscene);

        carPickerSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(carPickerSubscene);

        createLoginSubScene();
        createLevelPickerSubScene();
    }
    private void showSubscene(MainMenuSubscenes subScene)
    {
        if(sceneToHide!=null)
        {
            sceneToHide.moveMainMenuSubscene();
        }
        subScene.moveMainMenuSubscene();
        sceneToHide=subScene;
    }

    private void addMenuButton(GameButton button)
    {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + mainMenuButtons.size()*100);
        mainMenuButtons.add(button);
        mainPane.getChildren().add(button);
    }
    private void addLevelButton(GameButton button)
    {
        button.setLayoutX(100+70);
        button.setLayoutY(100+50+pickLevelButtons.size()*120);
        pickLevelButtons.add(button);
        levelPickerSubScene.getPane().getChildren().add(button);
    }
    private void createStartButton() throws FileNotFoundException {
        GameButton startButton = new GameButton("PLAY");
        addMenuButton(startButton);

        startButton.setOnAction(e ->showSubscene(levelPickerSubScene));


    }
    private void createHelpButton() throws FileNotFoundException {
        GameButton helpButton = new GameButton("HELP");
        addMenuButton(helpButton);

        helpButton.setOnAction(e->showSubscene(helpSubscene));
        //helpButton.setOnAction(e->mainMenuButtonSound.play());
    }
    private void createCreditsButton() throws FileNotFoundException {
        GameButton creditsButton = new GameButton("CREDITS");
        addMenuButton(creditsButton);

        creditsButton.setOnAction(e->showSubscene(creditsSubscene));
        //creditsButton.setOnAction(e->mainMenuButtonSound.play());
    }
    private void createLoginButton() throws FileNotFoundException {
        GameButton loginButton = new GameButton("LOGIN");
        addMenuButton(loginButton);

        loginButton.setOnAction(e->showSubscene(loginSubscene));
        //loginButton.setOnAction(e->mainMenuButtonSound.play());
    }
    private void createExitButton() throws FileNotFoundException {
        GameButton exitButton = new GameButton("EXIT");
        addMenuButton(exitButton);


        exitButton.setOnAction((event) -> {
            //mainMenuButtonSound.play();
            PrintWriter zapis = null;
            try {
                zapis = new PrintWriter("D:/Git/ProjektJPWP/GAME-PROJECT/src/config.test");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            zapis.println("[Users]");
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                zapis.println("<"+entry.getKey()+">");
                zapis.println(entry.getValue());
                zapis.println("</"+entry.getKey()+">");
                zapis.println("");
                }

            zapis.println("[endUsers]");
            zapis.close();

            mainStage.close();
        });


    }
    private void createCarPickerButton() throws FileNotFoundException
    {
        GameButton carPickerButton = new GameButton("Cars");
        addMenuButton(carPickerButton);

        carPickerButton.setOnAction(e->showSubscene(carPickerSubscene));

    }
    private void createLevel_01Button() throws FileNotFoundException
    {
        GameButton level01Button = new GameButton("Level 1");
        addLevelButton(level01Button);

        level01Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(pickedCar!=null)
                {
                    GameViewManagerLevel_01 gameViewManager = new GameViewManagerLevel_01();
                    gameViewManager.createNewGame(mainStage,pickedCar);
                }
                if(pickedCar==null)
                {
                    showSubscene(carPickerSubscene);
                }
            }
        });

    }
    private void createLevel_02Button() throws FileNotFoundException
    {
        GameButton level02Button = new GameButton("Level 2");
        addLevelButton(level02Button);


        //miejsce na actionListener
    }
    private void createLevel_03Button() throws FileNotFoundException
    {
        GameButton level03Button = new GameButton("Level 3");
        addLevelButton(level03Button);

        //miejsce na actionListener
    }
    private void createLevel_04Button() throws FileNotFoundException
    {
        GameButton level04Button = new GameButton("Level 4");
        addLevelButton(level04Button);

        //miejsce na actionListener
    }
    private void createLevel_05Button() throws FileNotFoundException
    {
        GameButton level05Button = new GameButton("Level 5");
        addLevelButton(level05Button);

        //miejsce na actionListener
    }
    private void createButtons() throws FileNotFoundException {
        createLoginButton();
        createStartButton();
        createCarPickerButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();



        createLevel_01Button();
        createLevel_02Button();
        createLevel_03Button();
        createLevel_04Button();
        createLevel_05Button();
    }


}
