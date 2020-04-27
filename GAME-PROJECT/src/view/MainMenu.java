package view;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.util.*;

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
    private MainMenuSubscenes scoreSubscene;

    MainMenuSubscenes loginSubscene;
    MainMenuSubscenes levelPickerSubScene;
    private List<GameButton> pickLevelButtons;


    private List<CarPicker> carList;
    private CAR pickedCar;

    private InfoLabel totalCollectedPointsLabel;
    public static int totalCollectedPointsValue;

    public static String  userNick = "Not logged in";
    public InfoLabel userNickLabel;


    private AnimationTimer mainMenuTimer;




    private static HashMap<String, Integer> players;
    public static  HashMap<String, Integer> scoreSave;

    private AudioClip buttonClickSound;
    private boolean beenLagged = false;

    public MainMenu(HashMap<String, Integer> players, HashMap<String, Integer> scoreSave) throws FileNotFoundException
    {   this.players = players;
        this.scoreSave = scoreSave;
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


    private void createMainMenuSounds()
    {
        buttonClickSound = new AudioClip(Paths.get("menuButtonSoundVolume2.wav").toUri().toString());
    }
    private void createMainMenuLoop()
    {
        mainMenuTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                totalCollectedPointsLabel.setText("Points: "+totalCollectedPointsValue);
                userNickLabel.setText("User: "+userNick);
            }
        };
        mainMenuTimer.start();
    }

    private void createTotalCollectedPointsLabel()
    {
        totalCollectedPointsLabel = new InfoLabel("Total Points: "+ totalCollectedPointsValue);
        mainPane.getChildren().add(totalCollectedPointsLabel);
        totalCollectedPointsLabel.setLayoutY(950);
        userNickLabel = new InfoLabel("User: "+ userNick);
        mainPane.getChildren().add(userNickLabel);
        userNickLabel.setLayoutY(900);
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

                    }
                    else
                    {
                        pickedCar=null;
                        carToPick.setIsCircleChoosen(false);
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
    private void createLoginSubScene()  throws FileNotFoundException
    {
        loginSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(loginSubscene);

        HashMap<String, TextField>  textFieldHashMap = new HashMap<>();
        TextField userNameTextField = new TextField();
        textFieldHashMap.put("userName", userNameTextField);

        GameButton loginButton1 = new GameButton("LOG IN");

            loginButton1.setOnAction((event) -> {
                String test = textFieldHashMap.get("userName").getText();

                if (userNick.equals("Not logged in")){
                    for (Map.Entry<String, Integer> entry : players.entrySet()) {
                        if (entry.getKey().equals(test)) {
                            beenLagged = true;
                            totalCollectedPointsValue = entry.getValue();
                            userNick=entry.getKey();
                        }
                    }
                    if (beenLagged){showSubscene(carPickerSubscene);}
                    else{JOptionPane.showMessageDialog(null, "This user does not exist");}
                }
                else{
                    for (Map.Entry<String, Integer> entry : players.entrySet()) {
                        if (entry.getKey().equals(userNick)) {
                            entry.setValue(totalCollectedPointsValue);
                        }
                    }
                    for (Map.Entry<String, Integer> entry : players.entrySet()) {
                        if (entry.getKey().equals(test)) {
                            beenLagged = true;
                            totalCollectedPointsValue = entry.getValue();
                            userNick=entry.getKey();

                        }
                    }
                    if (beenLagged){showSubscene(carPickerSubscene);}
                    else{JOptionPane.showMessageDialog(null, "This user does not exist");}
                }
            });
        userNameTextField.setLayoutX(100);
        userNameTextField.setLayoutY(100);
        userNameTextField.setPrefWidth(200);
        userNameTextField.setPrefHeight(50);

        loginButton1.setLayoutX(300);
        loginButton1.setLayoutY(100);

        loginSubscene.getPane().getChildren().addAll(userNameTextField, loginButton1);


        TextField newNameTextField = new TextField();
        textFieldHashMap.put("newName", newNameTextField);

       GameButton createButton = new GameButton("CREATE");
        createButton.setOnAction((event) -> {
            String test = textFieldHashMap.get("newName").getText();
            boolean been = false;
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                if (entry.getKey().equals(test)) {
                    been = true;
                }
            }
            if (been){JOptionPane.showMessageDialog(null, "This user already exists");}
            else{players.put(test,0); scoreSave.put(test,0); JOptionPane.showMessageDialog(null, "Player created");}
        });

        newNameTextField.setLayoutX(100);
        newNameTextField.setLayoutY(300);
        newNameTextField.setPrefWidth(200);
        newNameTextField.setPrefHeight(50);

        createButton.setLayoutX(300);
        createButton.setLayoutY(300);

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
    private void createScoreSubscene()
    {
       scoreSubscene = new MainMenuSubscenes();
       mainPane.getChildren().add(scoreSubscene);

        InfoLabel scoreTitle = new InfoLabel("TOP SCORE");
        scoreTitle.setLayoutX(100);
        scoreTitle.setLayoutY(20);
        scoreSubscene.getPane().getChildren().add(scoreTitle);
        int i =0;
        int xPlayser = 75;
        int yPlayser = 100;
        scoreSave = sortByValues(scoreSave);
        for (Map.Entry<String, Integer> entry : scoreSave.entrySet()) {
                ScoreLabel usersName = new ScoreLabel(i+1 +". "+ entry.getKey() + "     " + (entry.getValue()));
                usersName.setLayoutX(xPlayser);
                usersName.setLayoutY(yPlayser + 60*i );
                scoreSubscene.getPane().getChildren().add(usersName);
                if (i>9){break;}
                i++;
        }

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
    private void createCarPickerSubscene() throws FileNotFoundException{
        carPickerSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(carPickerSubscene);
        GameButton playButton = new GameButton("PLAY");
        playButton.setLayoutX(300);
        playButton.setLayoutY(650);
        playButton.setOnAction((event) -> {
            totalCollectedPointsValue = totalCollectedPointsValue - pickedCar.getPointsRequiredToUnlock();
            if(pickedCar!=null)
            {
                GameViewManager gameViewManager = new GameViewManager();
                gameViewManager.createNewGame(mainStage,pickedCar);
            }
            else{
                JOptionPane.showMessageDialog(null,"Not choose car");
            }

        });
        carPickerSubscene.getPane().getChildren().add(playButton);
        }


    private void createMainMenuSubscenes() throws FileNotFoundException
    {
        creditsSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(creditsSubscene);



        createHelpSubScene();
        createCarPickerSubscene();
        createLoginSubScene();
        createLevelPickerSubScene();
        createScoreSubscene();
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

        startButton.setOnAction(e -> {
            if(userNick != "Not logged in") {
                showSubscene(carPickerSubscene);
                buttonClickSound.play();
            }
            else {
                showSubscene((loginSubscene));
                buttonClickSound.play();
            }
        });

    }
    private void createHelpButton() throws FileNotFoundException {
        GameButton helpButton = new GameButton("HELP");
        addMenuButton(helpButton);

        helpButton.setOnAction(e->{
            showSubscene(helpSubscene);
            buttonClickSound.play();
        });

    }
    private void createCreditsButton() throws FileNotFoundException {
        GameButton creditsButton = new GameButton("CREDITS");
        addMenuButton(creditsButton);

        creditsButton.setOnAction(e->{
            showSubscene(creditsSubscene);
            buttonClickSound.play();
        });
        //creditsButton.setOnAction(e->mainMenuButtonSound.play());
    }
    private void createLoginButton() throws FileNotFoundException {
        GameButton loginButton = new GameButton("LOGIN");
        addMenuButton(loginButton);

        loginButton.setOnAction(e->{
            showSubscene(loginSubscene);
            buttonClickSound.play();
        });
        //loginButton.setOnAction(e->mainMenuButtonSound.play());
    }
    private void createExitButton() throws FileNotFoundException {

        GameButton exitButton = new GameButton("EXIT");
        addMenuButton(exitButton);
        exitButton.setOnAction((event) -> {
            buttonClickSound.play();
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                if (entry.getKey().equals(userNick)) {
                    entry.setValue(totalCollectedPointsValue);
                }
            }

            PrintWriter zapis = null;
            try {
                zapis = new PrintWriter("src/config.test");
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
            zapis.println("");
            zapis.println("[Score]");
            for (Map.Entry<String, Integer> entry : scoreSave.entrySet()) {
                zapis.println("<"+entry.getKey()+">");
                zapis.println(entry.getValue());
                zapis.println("</"+entry.getKey()+">");
                zapis.println("");
            }

            zapis.println("[endScore]");
            zapis.close();

            mainStage.close();
            System.exit(1);
        });

    }
    private void createScoreButton() throws FileNotFoundException
    {
        GameButton scoreButton = new GameButton("SCORE");
        addMenuButton(scoreButton);

        scoreButton.setOnAction(e->
        {   scoreSubscene = null;
            createScoreSubscene();
            showSubscene(scoreSubscene);
            buttonClickSound.play();
        });

    }
    private void createLevel_01Button() throws FileNotFoundException
    {
        GameButton level01Button = new GameButton("RAID");
        addLevelButton(level01Button);

        level01Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(pickedCar!=null)
                {
                    GameViewManager gameViewManager = new GameViewManager();
                    gameViewManager.createNewGame(mainStage,pickedCar);
                }
                if(pickedCar==null)
                {
                    showSubscene(carPickerSubscene);
                }
            }
        });

    }



    private void createButtons() throws FileNotFoundException {
        createLoginButton();
        createStartButton();
        createScoreButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();



        createLevel_01Button();

    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public void createHelpSubScene()
    {
        helpSubscene = new MainMenuSubscenes();
        mainPane.getChildren().add(helpSubscene);

        //Setting and displaying stars and bonuses - collectable
        InfoLabel itemsToCollectLabel = new InfoLabel("Items to collect");
        itemsToCollectLabel.setLayoutX(100);
        itemsToCollectLabel.setLayoutY(50);

        HBox itemsToCollectHbox = new HBox();
        itemsToCollectHbox.setSpacing(30);
        itemsToCollectHbox.setLayoutX(70);
        itemsToCollectHbox.setLayoutY(100+20);
        //itemsToCollectHbox.setPrefSize(540,200);

        helpSubscene.getPane().getChildren().add(itemsToCollectHbox);
        helpSubscene.getPane().getChildren().addAll(itemsToCollectLabel);

        int resizeValue = 100;
        ImageView goldStar = Config.goldStar;   goldStar.setFitHeight(resizeValue);  goldStar.setFitWidth(resizeValue);
        ImageView blueArrow = Config.blueArrow; blueArrow.setFitHeight(resizeValue);    blueArrow.setFitWidth(resizeValue);
        ImageView greenArrow = Config.greenArrow; greenArrow.setFitHeight(resizeValue); greenArrow.setFitWidth(resizeValue);
        itemsToCollectHbox.getChildren().addAll(goldStar,blueArrow,greenArrow);

        //Setting and displaying obstacles - items ought to be avoided and destroyed
        InfoLabel obstaclesLabel = new InfoLabel("Obstacles");
        helpSubscene.getPane().getChildren().add(obstaclesLabel);
        obstaclesLabel.setLayoutX(100);
        obstaclesLabel.setLayoutY(250);
        HBox obstaclesHbox = new HBox();
        obstaclesHbox.setSpacing(30);
        obstaclesHbox.setLayoutX(70);
        obstaclesHbox.setLayoutY(300+70);
        helpSubscene.getPane().getChildren().add(obstaclesHbox);
        ImageView rock = Config.smallObstacleRock;  rock.setFitHeight(70);  rock.setFitWidth(100);
        ImageView roadBlock = Config.smallObstacleRoadBlock;    roadBlock.setFitHeight(80); roadBlock.setFitWidth(100);
        ImageView vendingMachine = Config.bigObstacleVendingMachine;    vendingMachine.setFitHeight(150);   vendingMachine.setFitWidth(100);
        obstaclesHbox.getChildren().addAll(rock,roadBlock,vendingMachine);

        //Setting and displaying controls
        InfoLabel controlsLabel = new InfoLabel("Controls");
        ImageView controlsImage = Config.controlsImage;    controlsImage.setFitHeight(120);    controlsImage.setFitWidth(200);
        controlsImage.setLayoutX(250-20);
        controlsImage.setLayoutY(630);
        helpSubscene.getPane().getChildren().add(controlsLabel);
        helpSubscene.getPane().getChildren().add(controlsImage);
        controlsLabel.setLayoutX(100);
        controlsLabel.setLayoutY(550);

        ImageView fireKey = new ImageView("view/resources/fireKey.png");
        fireKey.setLayoutX(150-15-20);
        fireKey.setLayoutY(630+5);
        fireKey.setFitHeight(65);
        fireKey.setFitWidth(65);
        helpSubscene.getPane().getChildren().add(fireKey);





    }
}


