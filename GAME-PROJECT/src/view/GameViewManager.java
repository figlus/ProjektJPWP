package view;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;



import model.CAR;
import model.GameButton;
import model.InGameLabel;


import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class GameViewManager
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
    private boolean isFireKeyReleased;

    private  int carForwardSpeed = 4;
    private int carSideSpeed = 4;

    private AnimationTimer gameTimer;

    private final static String BACKGROUND_ROAD_IMAGE_PATH = "view/resources/blackRoadWithCurb.png";
    private GridPane gridPane1;
    private GridPane gridPane2;

    private  double backgroundRollingSpeed = 3;
    private int playerLife;
    private ImageView goldStar;
    private ImageView playerLifes[];
    private ImageView smallObstacleRoadBlock[];
    private ImageView smallObstacleRock[];
    private ImageView bigObstacleVendingMachine[];
    private int smallObstacleRoadBlockHP[];
    private int smallObstacleRockHP[];
    private int bigObstacleVendingMachineHP[];
    private final static int smallObstacleHealthPoints = 3;
    private final static int bigObstacleHealthPoints = 5;
    private final static String smallObstacleRoadBlock_PATH = "view/resources/smallObstacles/smallObstacleRoadBlock.png";
    private final static String smallObstacleRock_PATH = "view/resources/smallObstacles/smallObstacleRock.png";
    private final static String bigObstacleVendingMachine_PATH="view/resources/bigObstacles/bigObstacleVendingMachine.png";

    private Random randomPositionGenerator;

    private final static int ROCK_RADIUS = 21;
    private final static int ROAD_BLOCK_RADIUS = 11;
    private final static int VENDING_MACHINE_RADIUS_01 = 15;
    private final static int VENDING_MACHINE_RADIUS_02 = 30;

    private final static int GOLD_STAR_RADIUS = 19;
    private final static int GREEN_ARROW_RADIUS = 20;
    private final static int BLUE_ARROW_RADIUS = 18;
    private final static String GOLD_STAR_PATH = "view/resources/goldStar.png";
    private InGameLabel pointsLabel;
    private int inGamePoints;


    private ImageView bulletImage;
    private final static String BULLET_PATH = "view/resources/bullet.png";
    private List<ImageView> ammoBox = new ArrayList<>();
    private int bulletSpeed = 9;


    private AudioClip gunFireSoundEffect;
    private AudioClip collisionSoundEffect;
    private AudioClip starCollestedSoundEffect;
    private AudioClip greenArrowCollectedSoundEffect;
    private AudioClip blueArrowCollectedSoundEffect;
    private AudioClip bonusLostSoundEffect;
    private AudioClip lowHPalarmSoundEffect;
    private AudioClip gameOverSoundEffect;
    private AudioClip carLoop;
    private AudioClip carBrake;
    private AudioClip buttonClick;


    private final static String FIRE_IMAGE_PATH = "view/resources/fire.png";
    private ImageView fireImage;

    private java.util.Timer timer;
    private TimerTask speedUp;
    private TimerTask hideMuzzleFlash;

    private int muzzleX;
    private int muzzleY;


    private final static String greenArrow_PATH = "view/resources/greenArrow.png";
    private final static String blueArrow_PATH = "view/resources/blueArrow.png";
    private ImageView blueArrow;
    private ImageView greenArrow;


    private int bulletsFly= 0;
    private AnimationTimer bulletsTimer;

    private boolean isLeftKeyTyped;
    private boolean isRightKeyTyped;
    private boolean isDownKeyTyped;

    private GameOverSubscene gameOverSubscene;
    private GameButton next;
    private GameButton retry;


    public GameViewManager()
    {
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    public void createNewGame(Stage menuStage, CAR pickedCar)
    {
        this.menuStage = menuStage;
        this.menuStage.hide();

        muzzleX = pickedCar.getMuzzleX();
        muzzleY = pickedCar.getMuzzleY();
        createBackground();
        createGameSoundEffects();
        createCar(pickedCar);
        createGameElements(pickedCar);
        createAmmoBox();
        createGameOverSubscene(menuStage,pickedCar);





        speedUpTheGame();
        createGameLoop(pickedCar);
        createBulletLoop(pickedCar);
        gameStage.show();
    }

    private void createBulletLoop(CAR pickedCar)
    {
        bulletsTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                fireGun();
                //checkIfCarMakesBrakingSound();
            }
        };
        bulletsTimer.start();
    }

    public void createGameLoop(CAR pickedCar)
    {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                moveBackground();
                moveGameElements();
                checkIfElementsAreBehindScreenAndRelocateThem();
                checkIfElementsCollide(pickedCar);
                checkIfBulletsAndElementsCollide(pickedCar);

                moveCar();
                showMuzzleFlash(pickedCar);
                //fire(pickedCar);



            }
        };

        gameTimer.start();

    }
    private void fireGun()
    {
        for(int i=0; i<ammoBox.size(); i++)
        {

            if(isFireKeyReleased==true && ammoBox.get(i).getLayoutY()==1000 ) //wystrzal
            {
                ammoBox.get(i).setLayoutY(car.getLayoutY()+muzzleY);
                ammoBox.get(i).setLayoutX(car.getLayoutX()+muzzleX);
                bulletsFly+=1;
                //fireImage.setLayoutX(car.getLayoutX()+muzzleX-23);
                //fireImage.setLayoutY(car.getLayoutY()+muzzleY-24);

                gunFireSoundEffect.play(0.3);
                isFireKeyReleased=false;

            }

            if(ammoBox.get(i).getLayoutY()>-50 && ammoBox.get(i).getLayoutY()!=1000)
            {
                ammoBox.get(i).setLayoutY(ammoBox.get(i).getLayoutY() - (bulletSpeed)); //bullet speed is declared above as -5
            }
            if(ammoBox.get(i).getLayoutY()<=-50)
            {
                ammoBox.get(i).setLayoutY(1000);
                ammoBox.get(i).setLayoutX(1000);
                bulletsFly-=1;

            }
        }



    }

    private void createGameSoundEffects()
    {

        gunFireSoundEffect = new AudioClip(Paths.get("gunFireSoundEffect.wav").toUri().toString());
        gunFireSoundEffect.setVolume(0.5);
        collisionSoundEffect = new AudioClip(Paths.get("collision.wav").toUri().toString());
        collisionSoundEffect.setVolume(0.2);
        starCollestedSoundEffect = new AudioClip(Paths.get("starCollected.wav").toUri().toString());
        greenArrowCollectedSoundEffect = new AudioClip(Paths.get("arrowCollected.wav").toUri().toString());
        greenArrowCollectedSoundEffect.setVolume(0.1);
        blueArrowCollectedSoundEffect = new AudioClip(Paths.get("blueArrow.wav").toUri().toString());
        lowHPalarmSoundEffect = new AudioClip(Paths.get("lowHPalarm.wav").toUri().toString());
        lowHPalarmSoundEffect.setVolume(0.5);
        bonusLostSoundEffect = new AudioClip(Paths.get("bonusLost.wav").toUri().toString());
        bonusLostSoundEffect.setVolume(0.2);
        gameOverSoundEffect = new AudioClip(Paths.get("gameOver.mp3").toUri().toString());
        gameOverSoundEffect.setVolume(0.1);
        carLoop = new AudioClip(Paths.get("carLoop2.wav").toUri().toString());
        carBrake = new AudioClip(Paths.get("carBrake.wav").toUri().toString());
        carBrake.setPriority(5);
        buttonClick = new AudioClip(Paths.get("menuButtonSoundVolume3.wav").toUri().toString());

        //Media backgroundSoundEffect = new Media(Paths.get("C:\\Users\\rafal\\Pulpit\\Repozytorium\\ProjektJPWP\\GAME-PROJECT\\carLoop2.wav").toUri().toString());
        //MediaPlayer mediaPlayer = new MediaPlayer(backgroundSoundEffect);
        //mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        //mediaPlayer.play();

        carLoop.setCycleCount(AudioClip.INDEFINITE);
        carLoop.setPriority(10);
        carLoop.play(0.5);




        //carSound = new Media(Paths.get("8bitCarSound.wav").toUri().toString());
        //mediaPlayer = new MediaPlayer(carSound);
        //mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        //mediaPlayer.play();


    }



    private void createAmmoBox()
    {
        //tworze ammobox z 5 pociskami
        ammoBox=new ArrayList<>();

        for(int i=0; i<5; i++)
        {
            bulletImage = new ImageView(BULLET_PATH);
            bulletImage.setLayoutY(1000);
            bulletImage .setLayoutX(1000);
            gamePane.getChildren().add(bulletImage);
            ammoBox.add(bulletImage);
        }
    }

    private void checkIfCarMakesBrakingSound()
    {
        if(isLeftKeyTyped==true && carBrake.isPlaying()==false)
        {
            carBrake.play();
            isLeftKeyTyped=false;
        }
        if(isRightKeyPressed==true && carBrake.isPlaying()==false)
        {
            carBrake.play();
            isRightKeyTyped=false;
        }
        if(isDownKeyPressed==true && carBrake.isPlaying())
        {
            carBrake.play();
            isDownKeyTyped=false;
        }
        if(isLeftKeyPressed==false && isRightKeyPressed==false && isDownKeyPressed==false)
        {
            carBrake.stop();
        }
    }

   private void showMuzzleFlash(CAR pickedCar)
    {
        if(isFireKeyReleased==true)
        {
            if(bulletsFly!=5) {
                fireImage.setLayoutX(car.getLayoutX() + pickedCar.getMuzzleX() - 23);
                fireImage.setLayoutY(car.getLayoutY() + pickedCar.getMuzzleY() - 24);
            }
        }

        else
        {
            fireImage.setLayoutX(3000);

        }
    }

    private void checkIfBulletsAndElementsCollide(CAR pickedCar)
    {
        for(int k=0; k<ammoBox.size(); k++)
        {
            if(GOLD_STAR_RADIUS>calculateDistance(goldStar.getLayoutX()+24,ammoBox.get(k).getLayoutX(),goldStar.getLayoutY()+30,ammoBox.get(k).getLayoutY()))
            {
                setNewElementPosition(goldStar);
                ammoBox.get(k).setLayoutY(-60);
                bonusLostSoundEffect.play();
            }
        }

        for(int k=0; k<ammoBox.size(); k++)
        {
            if(GREEN_ARROW_RADIUS>calculateDistance(greenArrow.getLayoutX()+25,ammoBox.get(k).getLayoutX(),greenArrow.getLayoutY()+25,ammoBox.get(k).getLayoutY()))
            {
                setRareElementPosition(greenArrow);
                ammoBox.get(k).setLayoutY(-60);
                bonusLostSoundEffect.play();
            }
        }
        for(int k=0; k<ammoBox.size();k++)
        {
            if(BLUE_ARROW_RADIUS>calculateDistance(blueArrow.getLayoutX()+25,ammoBox.get(k).getLayoutX(),blueArrow.getLayoutY()+26,ammoBox.get(k).getLayoutY()))
            {
                setReallyRareElementsPosition(blueArrow);
                ammoBox.get(k).setLayoutY(-60);
                bonusLostSoundEffect.play();
            }
        }


        for(int i=0; i<smallObstacleRock.length; i++)
        {
            for(int k=0; k<ammoBox.size(); k++)
            {
                if(ROCK_RADIUS>calculateDistance(smallObstacleRock[i].getLayoutX()+25,ammoBox.get(k).getLayoutX(),
                        smallObstacleRock[i].getLayoutY()+20,ammoBox.get(k).getLayoutY()))
                {

                    if(smallObstacleRockHP[i]<=pickedCar.getGunDemage())
                    {
                        inGamePoints++;
                        MainMenu.totalCollectedPointsValue++;
                        String textToSet = "POINTS  :";
                        if (inGamePoints < 10) {
                            textToSet = textToSet + "  0";
                        }
                        pointsLabel.setText(textToSet + inGamePoints);

                        setNewElementPosition(smallObstacleRock[i]);
                        ammoBox.get(k).setLayoutY(-60);
                        collisionSoundEffect.play();
                        smallObstacleRockHP[i]=smallObstacleHealthPoints;
                    }
                    else
                    {
                        smallObstacleRockHP[i]-=pickedCar.getGunDemage();
                        collisionSoundEffect.play();
                        ammoBox.get(k).setLayoutY(-60);

                    }
                }
            }
        }
        for(int i=0; i<smallObstacleRoadBlock.length; i++)
        {
            for(int k=0; k<ammoBox.size(); k++)
            {
                if(     ROAD_BLOCK_RADIUS>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+10,ammoBox.get(k).getLayoutX(),
                        smallObstacleRoadBlock[i].getLayoutY()+38,ammoBox.get(k).getLayoutY())||
                        ROAD_BLOCK_RADIUS>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+40,ammoBox.get(k).getLayoutX(),
                        smallObstacleRoadBlock[i].getLayoutY()+38,ammoBox.get(k).getLayoutY())||
                        ROAD_BLOCK_RADIUS>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+25,ammoBox.get(k).getLayoutX(),
                                smallObstacleRoadBlock[i].getLayoutY()+38,ammoBox.get(k).getLayoutY()))
                {
                    if(smallObstacleRoadBlockHP[i]<=pickedCar.getGunDemage())
                    {
                        inGamePoints++;
                        MainMenu.totalCollectedPointsValue++;
                        String textToSet = "POINTS  : ";
                        if (inGamePoints < 10) {
                            textToSet = textToSet + " 0";
                        }
                        pointsLabel.setText(textToSet + inGamePoints);

                        setNewElementPosition(smallObstacleRoadBlock[i]);
                        ammoBox.get(k).setLayoutY(-60);
                        collisionSoundEffect.play();
                        smallObstacleRoadBlockHP[i]=smallObstacleHealthPoints;
                    }
                    else
                    {
                        smallObstacleRoadBlockHP[i]-=pickedCar.getGunDemage();
                        collisionSoundEffect.play();

                        ammoBox.get(k).setLayoutY(-60);

                    }
                }
            }
        }
        for(int i=0; i<bigObstacleVendingMachine.length; i++)
        {
            for(int k=0; k<ammoBox.size(); k++)
            {
                if(VENDING_MACHINE_RADIUS_02>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+30,ammoBox.get(k).getLayoutX(),
                        bigObstacleVendingMachine[i].getLayoutY()+30,ammoBox.get(k).getLayoutY()))
                {
                    if(bigObstacleVendingMachineHP[i]<=pickedCar.getGunDemage()) {
                        inGamePoints += 5;
                        MainMenu.totalCollectedPointsValue += 5;
                        String textToSet = "POINTS  : ";
                        if (inGamePoints < 10) {
                            textToSet = textToSet + " 0";
                        }
                        pointsLabel.setText(textToSet + inGamePoints);

                        setNewElementPosition(bigObstacleVendingMachine[i]);
                        ammoBox.get(k).setLayoutY(-60);
                        collisionSoundEffect.play();
                        bigObstacleVendingMachineHP[i]=bigObstacleHealthPoints;
                    }
                    else{
                        collisionSoundEffect.play();
                        bigObstacleVendingMachineHP[i]-=pickedCar.getGunDemage();
                        ammoBox.get(k).setLayoutY(-60);
                    }
                }
            }
        }


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

    private void speedUpTheGame()
    {
        timer = new Timer();
        speedUp = new TimerTask() {
            @Override
            public void run() {
                backgroundRollingSpeed+=0.5;
                //bulletSpeed+=1;
            }
        };
        timer.scheduleAtFixedRate(speedUp,5000l,5000l);


    }



    private double calculateDistance(double x1, double x2, double y1, double y2)
    {
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }

    private void checkIfElementsCollide(CAR pickedCar)
    {

        if(pickedCar.getRADIUS()+BLUE_ARROW_RADIUS>calculateDistance(blueArrow.getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX(),
                blueArrow.getLayoutY()+26,car.getLayoutY()+pickedCar.getPlusY())||
        pickedCar.getRADIUS2()+BLUE_ARROW_RADIUS>calculateDistance(blueArrow.getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX2(),
                blueArrow.getLayoutY()+26,car.getLayoutY()+pickedCar.getPlusY2()))
        {
            setReallyRareElementsPosition(blueArrow);
            blueArrowCollectedSoundEffect.play();
            System.out.println(backgroundRollingSpeed);
            backgroundRollingSpeed-=0.5;
            System.out.println(backgroundRollingSpeed);

            //increasing points for collecting item
            MainMenu.totalCollectedPointsValue += 30; //zwiekszamy liczbe punktow w MainMenu o wartosc gwiazdy czyli 10 :)
            inGamePoints = inGamePoints + 30;
            String scoreToSet = "POINTS  :  ";
            if(inGamePoints<10)
            {
                scoreToSet = scoreToSet + "0";
            }
            pointsLabel.setText(scoreToSet+inGamePoints);


        }

        if(pickedCar.getRADIUS()+GREEN_ARROW_RADIUS>calculateDistance(greenArrow.getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX(),
                greenArrow.getLayoutY()+25,car.getLayoutY()+pickedCar.getPlusY()) ||
        pickedCar.getRADIUS2()+GREEN_ARROW_RADIUS>calculateDistance(greenArrow.getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX2(),
                greenArrow.getLayoutY()+25,car.getLayoutY()+pickedCar.getPlusY2()))
        {
            greenArrowCollectedSoundEffect.play();
            setRareElementPosition(greenArrow);
            carSideSpeed+=2;
            carForwardSpeed+=1;
            timer = new Timer();
            TimerTask normalizeCarSpeed = new TimerTask() {
                @Override
                public void run() {
                    carSideSpeed -=2;
                    carForwardSpeed -=1;
                }
            };
            timer.schedule(normalizeCarSpeed,6000l);//decreasing speed values to normal after 6 seconds

            //increasing points for collecting item
            MainMenu.totalCollectedPointsValue += 20; //zwiekszamy liczbe punktow w MainMenu o wartosc gwiazdy czyli 10 :)
            inGamePoints = inGamePoints + 20;
            String scoreToSet = "POINTS  :  ";
            if(inGamePoints<10)
            {
                scoreToSet = scoreToSet + "0";
            }
            pointsLabel.setText(scoreToSet+inGamePoints);

        }

        if(pickedCar.getRADIUS()+GOLD_STAR_RADIUS>calculateDistance(goldStar.getLayoutX()+24,car.getLayoutX()+pickedCar.getPlusX(),
                goldStar.getLayoutY()+30,car.getLayoutY()+pickedCar.getPlusY())
        || pickedCar.getRADIUS2()+GOLD_STAR_RADIUS>calculateDistance(goldStar.getLayoutX()+24,car.getLayoutX()+pickedCar.getPlusX2(),
                goldStar.getLayoutY()+30,car.getLayoutY()+pickedCar.getPlusY2()))

        {
            starCollestedSoundEffect.play(1);
            setNewElementPosition(goldStar);

            //increasing points for collecting item
            MainMenu.totalCollectedPointsValue += 100; //zwiekszamy liczbe punktow w MainMenu o wartosc gwiazdy czyli 10 :)
            inGamePoints = inGamePoints + 100;
            String scoreToSet = "POINTS  :  ";
            if(inGamePoints<10)
            {
                scoreToSet = scoreToSet + "0";
            }
            pointsLabel.setText(scoreToSet+inGamePoints);
        }




        for(int i=0; i<smallObstacleRoadBlock.length; i++)
        {
            if(ROAD_BLOCK_RADIUS+pickedCar.getRADIUS()>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+10,car.getLayoutX()+pickedCar.getPlusX(),
                    smallObstacleRoadBlock[i].getLayoutY()+38,car.getLayoutY()+pickedCar.getPlusY())
                                 || //jesli to wyzej lub to nizej jest spelnione to:
                    ROAD_BLOCK_RADIUS+pickedCar.getRADIUS()>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+40,car.getLayoutX()+pickedCar.getPlusX(),
                    smallObstacleRoadBlock[i].getLayoutY()+38,car.getLayoutY()+pickedCar.getPlusY())||
                    ROAD_BLOCK_RADIUS+pickedCar.getRADIUS2()>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+10,car.getLayoutX()+pickedCar.getPlusX2(),
                            smallObstacleRoadBlock[i].getLayoutY()+38,car.getLayoutY()+pickedCar.getPlusY2())
                    || //jesli to wyzej lub to nizej jest spelnione to:
                    ROAD_BLOCK_RADIUS+pickedCar.getRADIUS2()>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+40,car.getLayoutX()+pickedCar.getPlusX2(),
                            smallObstacleRoadBlock[i].getLayoutY()+38,car.getLayoutY()+pickedCar.getPlusY2())||
                    ROAD_BLOCK_RADIUS+pickedCar.getRADIUS()>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX(),
                            smallObstacleRoadBlock[i].getLayoutY()+38,car.getLayoutY()+pickedCar.getPlusY())||
                    ROAD_BLOCK_RADIUS+pickedCar.getRADIUS2()>calculateDistance(smallObstacleRoadBlock[i].getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX2(),
                            smallObstacleRoadBlock[i].getLayoutY()+38,car.getLayoutY()+pickedCar.getPlusY2()))

            {

                collisionSoundEffect.play();
                removeLife();
                setNewElementPosition(smallObstacleRoadBlock[i]);
            }
        }

        for(int i=0; i<smallObstacleRock.length; i++)
        {
            if(ROCK_RADIUS+pickedCar.getRADIUS()>calculateDistance(smallObstacleRock[i].getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX(),
                    smallObstacleRock[i].getLayoutY()+20,car.getLayoutY()+pickedCar.getPlusY())||
            ROCK_RADIUS+pickedCar.getRADIUS2()>calculateDistance(smallObstacleRock[i].getLayoutX()+25,car.getLayoutX()+pickedCar.getPlusX2(),
                    smallObstacleRock[i].getLayoutY()+20,car.getLayoutY()+pickedCar.getPlusY2()))
            {

                collisionSoundEffect.play();
                removeLife();
                setNewElementPosition(smallObstacleRock[i]);
            }
        }

        for(int i=0; i<bigObstacleVendingMachine.length; i++)
        {
            if(VENDING_MACHINE_RADIUS_01+pickedCar.getRADIUS()>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+15,car.getLayoutX()+pickedCar.getPlusX(),
                    bigObstacleVendingMachine[i].getLayoutY()+75,car.getLayoutY()+pickedCar.getPlusY()) ||
            VENDING_MACHINE_RADIUS_01+pickedCar.getRADIUS()>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+45,car.getLayoutX()+pickedCar.getPlusX(),
                    bigObstacleVendingMachine[i].getLayoutY()+75,car.getLayoutY()+pickedCar.getPlusY()) ||
            VENDING_MACHINE_RADIUS_02+pickedCar.getRADIUS()>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+30,car.getLayoutX()+pickedCar.getPlusX(),
                    bigObstacleVendingMachine[i].getLayoutY()+30,car.getLayoutY()+pickedCar.getPlusY())||
                    VENDING_MACHINE_RADIUS_01+pickedCar.getRADIUS2()>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+15,car.getLayoutX()+pickedCar.getPlusX2(),
                            bigObstacleVendingMachine[i].getLayoutY()+75,car.getLayoutY()+pickedCar.getPlusY2()) ||
                    VENDING_MACHINE_RADIUS_01+pickedCar.getRADIUS2()>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+45,car.getLayoutX()+pickedCar.getPlusX2(),
                            bigObstacleVendingMachine[i].getLayoutY()+75,car.getLayoutY()+pickedCar.getPlusY2()) ||
                    VENDING_MACHINE_RADIUS_02+pickedCar.getRADIUS2()>calculateDistance(bigObstacleVendingMachine[i].getLayoutX()+30,car.getLayoutX()+pickedCar.getPlusX2(),
                            bigObstacleVendingMachine[i].getLayoutY()+30,car.getLayoutY()+pickedCar.getPlusY2()))
            {

                collisionSoundEffect.play();
                removeLife();
                setNewElementPosition(bigObstacleVendingMachine[i]);
            }
        }


    }

    private void removeLife()
    {
        gamePane.getChildren().remove(playerLifes[playerLife]);
        playerLife--;
        if(playerLife==0)
        {
            lowHPalarmSoundEffect.play(0.5);

        }

        if(playerLife <0)
        {
            carLoop.stop();
            gameTimer.stop();
            bulletsTimer.stop();
            gameOverSoundEffect.play();
            gameOverSubscene.setVisible(true);
        }

    }

    private void saveScore()
    {
        for (Map.Entry<String, Integer> entry : MainMenu.scoreSave.entrySet()) {
            if(entry.getKey().equals(MainMenu.userNick)){
                if (entry.getValue() < inGamePoints)
                    entry.setValue(inGamePoints);
            }
        }
    }

    private void endRound()
    {

        saveScore();
        gameStage.close();
        menuStage.show();
    }

   private void createGameElements(CAR pickedCar)
   {
       blueArrow = new ImageView(blueArrow_PATH);
       setRareElementPosition(blueArrow);
       setReallyRareElementsPosition(blueArrow);
       gamePane.getChildren().add(blueArrow);

       greenArrow = new ImageView(greenArrow_PATH);
       setRareElementPosition(greenArrow);
       gamePane.getChildren().add(greenArrow);

       goldStar = new ImageView(GOLD_STAR_PATH);
       setNewElementPosition(goldStar);
       gamePane.getChildren().add(goldStar);

       pointsLabel = new InGameLabel("  POINTS : 00");
       pointsLabel.setLayoutX(600);
       pointsLabel.setLayoutY(10);
       gamePane.getChildren().add(pointsLabel);

       //creating fire image
       fireImage = new ImageView(FIRE_IMAGE_PATH);
       gamePane.getChildren().add(fireImage);
       fireImage.setLayoutY(3000);
       fireImage.setLayoutX(3000);


       //creating road obstacles
        smallObstacleRock = new ImageView[8];
        for(int i=0; i<smallObstacleRock.length; i++)
        {
            smallObstacleRock[i] = new ImageView(smallObstacleRock_PATH);
            setNewElementPosition(smallObstacleRock[i]);
            gamePane.getChildren().add(smallObstacleRock[i]);
        }
        //creating HP array for Rocks
        smallObstacleRockHP = new int[8];
        for(int i=0; i<smallObstacleRockHP.length; i++)
        {
            smallObstacleRockHP[i]=smallObstacleHealthPoints;
        }


        smallObstacleRoadBlock = new ImageView[7];
        for(int i=0; i<smallObstacleRoadBlock.length; i++)
        {
            smallObstacleRoadBlock[i] = new ImageView(smallObstacleRoadBlock_PATH);
            setNewElementPosition(smallObstacleRoadBlock[i]);
            gamePane.getChildren().add(smallObstacleRoadBlock[i]);
        }
        //creating HP array for road blocks
        smallObstacleRoadBlockHP = new int[7];
       for(int i=0; i<smallObstacleRoadBlockHP.length; i++)
       {
           smallObstacleRoadBlockHP[i]=smallObstacleHealthPoints;
       }

        bigObstacleVendingMachine = new ImageView[3];
        for(int i=0; i<bigObstacleVendingMachine.length; i++)
        {
            bigObstacleVendingMachine[i] = new ImageView(bigObstacleVendingMachine_PATH);
            setNewElementPosition(bigObstacleVendingMachine[i]);
            gamePane.getChildren().add(bigObstacleVendingMachine[i]);
        }
        //creating HP array for vending machines
        bigObstacleVendingMachineHP = new int[3];
       for(int i=0; i<bigObstacleVendingMachineHP.length; i++)
       {
          bigObstacleVendingMachineHP[i]=bigObstacleHealthPoints;
       }

       playerLife = pickedCar.getMaxHelath()-1;
       playerLifes = new ImageView[pickedCar.getMaxHelath()];

       for(int i=0; i<playerLifes.length; i++)
       {
           playerLifes[i] = new ImageView(pickedCar.getUrlLifeIndicator());
           playerLifes[i].setLayoutX(20+(i*50));
           playerLifes[i].setLayoutY(20);
           gamePane.getChildren().add(playerLifes[i]);
       }
   }

   public void moveGameElements()
   {
       goldStar.setLayoutY(goldStar.getLayoutY()+backgroundRollingSpeed);
       greenArrow.setLayoutY(greenArrow.getLayoutY()+backgroundRollingSpeed);
       blueArrow.setLayoutY(blueArrow.getLayoutY()+backgroundRollingSpeed);

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

       if(goldStar.getLayoutY()>1020)
       {
           setNewElementPosition(goldStar);
       }
       if(greenArrow.getLayoutY()>1200)
       {
           setRareElementPosition(greenArrow);
       }
       if(blueArrow.getLayoutY()>1200)
       {
           setReallyRareElementsPosition(blueArrow);
       }

       for(int i=0; i<smallObstacleRock.length; i++)
       {
           if(smallObstacleRock[i].getLayoutY()>1010)
           {
               setNewElementPosition(smallObstacleRock[i]);
           }

       }
       for(int i=0; i<smallObstacleRoadBlock.length; i++)
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

    private void createKeyListeners()//controller
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

                if(keyEvent.getCode()==KeyCode.F)
                {
                    isFireKeyReleased = true;
                }
            }
        });
        gameScene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()==KeyCode.LEFT)
                {
                    isLeftKeyTyped=true;
                }
                if(keyEvent.getCode()==KeyCode.RIGHT)
                {
                   isRightKeyTyped=true;
                }
                if(keyEvent.getCode()==KeyCode.DOWN)
                {
                    isDownKeyTyped=true;
                }
            }
        });

    }

    private void setNewElementPosition(ImageView imageView) //ta funkcja wyrzuca elementy poza zasieg widzenia na drodze przejazdu samochodu ! !
    {
        imageView.setLayoutX(randomPositionGenerator.nextInt(650)+50);
        imageView.setLayoutY(-(randomPositionGenerator.nextInt(6000)+1000));
    }
    private void setRareElementPosition(ImageView imageView)
    {
        imageView.setLayoutX(randomPositionGenerator.nextInt(650)+50);
        imageView.setLayoutY(-(randomPositionGenerator.nextInt(1000)+3000));
    }
    private void setReallyRareElementsPosition(ImageView imageView)
    {
        imageView.setLayoutX(randomPositionGenerator.nextInt(650)+50);
        imageView.setLayoutY(-(randomPositionGenerator.nextInt(1000)+15000));
    }


    private void moveCar()//controller
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


    private void moveBackground()//controller
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
        if(Math.abs(gridPane1.getLayoutY()-gridPane2.getLayoutY())!=1200)
        {
            gridPane1.setLayoutY(0);
            gridPane2.setLayoutY(-1200);
        }
    }

    private void createGameOverSubscene(Stage mainStage,CAR pickedCar)
    {
        gameOverSubscene = new GameOverSubscene();

        gamePane.getChildren().add(gameOverSubscene);
        gameOverSubscene.setVisible(false);
        gameOverSubscene.setLayoutY(350);
        gameOverSubscene.setLayoutX(100);


        try {
            next = new GameButton("NEXT");
            retry = new GameButton("RETRY");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonClick.play();
                gameOverSoundEffect.stop();
                endRound();
            }
        });
        retry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonClick.play();
                saveScore();
                gameStage.close();
                gameOverSoundEffect.stop(); //end current round and start another one !
                GameViewManager gameViewManager = new GameViewManager();
                gameViewManager.createNewGame(mainStage,pickedCar);
            }
        });

        gameOverSubscene.getPane().getChildren().add(next);
        gameOverSubscene.getPane().getChildren().add(retry);
        next.setLayoutX(220);
        next.setLayoutY(75);
        retry.setLayoutX(220);
        retry.setLayoutY(200-30);


    }




}
