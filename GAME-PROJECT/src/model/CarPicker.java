package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CarPicker extends HBox
{
    private ImageView circleImage;

    private ImageView carImage;
    private String circleNotChoosen = "view/resources/circleNotChoosen.png";
    private String circleChoosen = "view/resources/circleChoosen.png";

    private CAR car;

    private boolean isCircleChoosen;

    private InfoLabel carStats;






    public CarPicker (CAR car)
    {
        circleImage = new ImageView(circleNotChoosen);
        carImage = new ImageView(car.getCarUrl());

        this.car = car;
        isCircleChoosen = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        carStats = new InfoLabel("Price:"+this.car.getPointsRequiredToUnlock()+"  Lifes:"+this.car.getMaxHelath()+"  Demage:"+this.car.getGunDemage());
        carStats.setLabelFontSize(15);
        this.getChildren().add(carStats);
        this.getChildren().add(carImage);
        this.getChildren().add(circleImage);


    }

    public CAR getCar()
    {
        return car;
    }
    public boolean getIsCircleChoosen()
    {
        return isCircleChoosen;
    }
    public void setIsCircleChoosen(boolean isCircleChoosen)
    {
        this.isCircleChoosen = isCircleChoosen;
        String imageToSet = this.isCircleChoosen ? circleChoosen: circleNotChoosen;
        circleImage.setImage(new Image(imageToSet));
    }
}


