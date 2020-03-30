package view;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import model.CAR;
import model.InfoLabel;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameViewManagerLevel_01 extends Thread
{
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private Stage menuStage;
    private ImageView car;
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 1000;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    private  int carForwardSpeed = 4;
    private int carSideSpeed = 4;

    private AnimationTimer gameTimer;

    private final static String BACKGROUND_ROAD_IMAGE_PATH = "view/resources/blackRoadWithCurb.png";
    private GridPane gridPane1;
    private GridPane gridPane2;

    private final int backgroundRollingSpeed = 3;
    private int playerLife;
    private ImageView goldStar;
    private ImageView playerLifes[];
    private ImageView smallObstacleRoadBlock[];
    private ImageView smallObstacleRock[];
    private ImageView smallObstacleBranch[];
    private ImageView bigObstacleVendingMachine[];
    private final static String smallObstacleRoadBlock_PATH = "view/resources/smallObstacles/smallObstacleRoadBlock.png";
    private final static String smallObstacleRock_PATH = "view/resources/smallObstacles/smallObstacleRock.png";
    private final static String smallObstacleBranch_PATH = "view/resources/smallObstacles/smallObstacleBranch.png";
    private final static String bigObstacleVendingMachine_PATH="view/resources/bigObstacles/bigObstacleVendingMachine.png";
    private Random randomPositionGenerator;

    public GameViewManagerLevel_01()
    {
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    public void createNewGame(Stage menuStage, CAR pickedCar)
    {
        this.menuStage = menuStage;
        this.menuStage.hide();


        createBackground();
        createGameElements();
        createCar(pickedCar);





        createGameLoop();
        gameStage.show();
    }

    public void createGameLoop()
    {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                moveBackground();
                moveGameElements();
                checkIfElementsAreBehindScreenAndRelocateThem();
                moveCar();
            }
        };

        gameTimer.start();
    }

    private void initializeStage()
    {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane,GAME_WIDTH,GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    private void createCar(CAR pickedCar)
    {
        car = new ImageView(pickedCar.getCarUrl());
        car.setLayoutX(GAME_WIDTH/2);
        car.setLayoutY(GAME_HEIGHT-200);
        car.prefHeight(120);
        car.prefWidth(50);
        gamePane.getChildren().add(car);

    }

    public void createBackground()
    {
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        ImageView backgroundImage1 = new ImageView(BACKGROUND_ROAD_IMAGE_PATH);
        ImageView backgroundImage2 = new ImageView(BACKGROUND_ROAD_IMAGE_PATH);

        gridPane1.getChildren().add(backgroundImage1);
        gridPane2.getChildren().add(backgroundImage2);

        gridPane2.setLayoutY(-1200);

        gamePane.getChildren().add(gridPane1);
        gamePane.getChildren().add(gridPane2);
    }


   public void createGameElements()
   {
        smallObstacleRock = new ImageView[3];
        for(int i=0; i<smallObstacleRock.length; i++)
        {
            smallObstacleRock[i] = new ImageView(smallObstacleRock_PATH);
            setNewElementPosition(smallObstacleRock[i]);
            gamePane.getChildren().add(smallObstacleRock[i]);
        }

        smallObstacleRoadBlock = new ImageView[3];
        for(int i=0; i<smallObstacleRoadBlock.length; i++)
        {
            smallObstacleRoadBlock[i] = new ImageView(smallObstacleRoadBlock_PATH);
            setNewElementPosition(smallObstacleRoadBlock[i]);
            gamePane.getChildren().add(smallObstacleRoadBlock[i]);
        }

        bigObstacleVendingMachine = new ImageView[3];
        for(int i=0; i<bigObstacleVendingMachine.length; i++)
        {
            bigObstacleVendingMachine[i] = new ImageView(bigObstacleVendingMachine_PATH);
            setNewElementPosition(bigObstacleVendingMachine[i]);
            gamePane.getChildren().add(bigObstacleVendingMachine[i]);
        }
   }

   public void moveGameElements()
   {
       for(int i=0; i<smallObstacleRock.length; i++)
       {
           smallObstacleRock[i].setLayoutY(smallObstacleRock[i].getLayoutY()+backgroundRollingSpeed);
       }
       for(int i=0; i<smallObstacleRoadBlock.length; i++)
       {
           smallObstacleRoadBlock[i].setLayoutY(smallObstacleRoadBlock[i].getLayoutY()+backgroundRollingSpeed);
       }
       for(int i=0; i<bigObstacleVendingMachine.length; i++)
       {
           bigObstacleVendingMachine[i].setLayoutY(bigObstacleVendingMachine[i].getLayoutY()+backgroundRollingSpeed);
       }

   }

   public void checkIfElementsAreBehindScreenAndRelocateThem()
   {
       for(int i=0; i<smallObstacleRock.length; i++)
       {
           if(smallObstacleRock[i].getLayoutY()>1010)
           {
               setNewElementPosition(smallObstacleRock[i]);
           }

       }
       for(int i=0; i<smallObstacleRock.length; i++)
       {
           if(smallObstacleRoadBlock[i].getLayoutY()>1010)
           {
               setNewElementPosition(smallObstacleRoadBlock[i]);
           }

       }
       for(int i=0; i<bigObstacleVendingMachine.length; i++)
       {
           if(bigObstacleVendingMachine[i].getLayoutY()>1010)
           {
               setNewElementPosition(bigObstacleVendingMachine[i]);
           }

       }
   }

    private void createKeyListeners()
    {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if(keyEvent.getCode()==KeyCode.LEFT)
                {
                    isLeftKeyPressed =true;
                }
                else if(keyEvent.getCode()==KeyCode.RIGHT)
                {
                    isRightKeyPressed=true;
                }

                if(keyEvent.getCode()==KeyCode.UP)
                {
                    isUpKeyPressed=true;
                }
                else if(keyEvent.getCode()==KeyCode.DOWN)
                {
                    isDownKeyPressed=true;
                }

            }
        });
        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()== KeyCode.LEFT)
                {
                    isLeftKeyPressed=false;
                }
                else if (keyEvent.getCode()==KeyCode.RIGHT)
                {
                    isRightKeyPressed=false;
                }

                if(keyEvent.getCode()==KeyCode.UP)
                {
                    isUpKeyPressed=false;
                }
                else if(keyEvent.getCode()==KeyCode.DOWN)
                {
                    isDownKeyPressed=false;

                }
            }
        });

    }

    private void setNewElementPosition(ImageView imageView) //ta funkcja wyrzuca elementy poza zasieg widzenia na drodze przejazdu samochodu ! !
    {
        imageView.setLayoutX(randomPositionGenerator.nextInt(650)+50);
        imageView.setLayoutY(-(randomPositionGenerator.nextInt(3000)+600));
    }


    private void moveCar()
    {

        if(isLeftKeyPressed && !isRightKeyPressed)
        {


            //car.setRotate(angle);
            //shotFire.setRotate(angle);
            if(car.getLayoutX()>50)
            {
                car.setLayoutX(car.getLayoutX()-carSideSpeed);
            }
        }
        if(isRightKeyPressed && !isLeftKeyPressed)
        {


            //car.setRotate(angle);
            //shotFire.setRotate(angle);
            if(car.getLayoutX()<700)
            {
                car.setLayoutX(car.getLayoutX()+carSideSpeed);
            }

        }
        if(!isLeftKeyPressed && !isRightKeyPressed)
        {


            //car.setRotate(angle);
            //shotFire.setRotate(angle);
        }
        if(isLeftKeyPressed && isRightKeyPressed)
        {

            //car.setRotate(angle);
            //shotFire.setRotate(angle);
        }
        if(isUpKeyPressed && !isDownKeyPressed)
        {
            if(car.getLayoutY()>0)
            {
                car.setLayoutY(car.getLayoutY() - carForwardSpeed);
            }
        }

        if(!isUpKeyPressed && isDownKeyPressed)
        {
            if(car.getLayoutY()<900) {
                car.setLayoutY(car.getLayoutY() + carForwardSpeed);
            }
        }
        if (isDownKeyPressed && isUpKeyPressed)
        {

            //cos jest wpisane ale ogolnie to ma sie dziac nic
            car.setLayoutY(car.getLayoutY()+0);
        }
        if (!isDownKeyPressed && !isUpKeyPressed)
        {

            //nic sie nie dzieje gdy nie sa wcisniete klawisze xd
            car.setLayoutY(car.getLayoutY()+0);
        }

    }


    private void moveBackground()
    {
        gridPane1.setLayoutY(gridPane1.getLayoutY()+backgroundRollingSpeed);
        gridPane2.setLayoutY(gridPane2.getLayoutY()+backgroundRollingSpeed);

        if(gridPane1.getLayoutY()>=1200)
        {
            gridPane1.setLayoutY(-1200);
        }
        if(gridPane2.getLayoutY()>=1200)
        {
            gridPane2.setLayoutY(-1200);
        }
    }

}
