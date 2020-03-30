package model;

public enum CAR
{
    CAR_LEVEL_01("view/resources/cars/redCarBigger.png","view/resources/lifeIndicator.png",3,1,0,false,false,23,25,35),
    CAR_LEVEL_02("view/resources/cars/yellowCar1.png","view/resources/lifeIndicator.png",5,2,200,false,false,23,25,35),
    CAR_LEVEL_03("view/resources/cars/blueCar.png","view/resources/lifeIndicator.png",3,5,300,false,false,23,25,25),
    MOTORBOAT_01("view/resources/cars/motorBoat.png","view/resources/lifeIndicator.png",5,2,500,false,true,22,24,33);


    String urlLifeIndicator;
    String urlCar;
    int maxHelath;
    int gunDemage;
    int pointsRequiredToUnlock; //trzeba dodac jakaz zaleznosc odblokowywania kolejnych pojazdow zaleznie od poziomu/ zdobytych w grze punktow
    boolean isBought;
    boolean isBoat;
    int RADIUS;
    int plusX;
    int plusY;


    private CAR(String urlCar, String urlLifeIndicator, int maxHealth, int gunDemage, int pointsRequiredToUnlock, boolean isBought, boolean isBoat,int RADIUS,int plusX, int plusY)
    {
        this.urlCar = urlCar;
        this.urlLifeIndicator = urlLifeIndicator;
        this.maxHelath = maxHealth;
        this.gunDemage = gunDemage;
        this.pointsRequiredToUnlock = pointsRequiredToUnlock;
        this.isBought = isBought;
        this.isBoat = isBoat;
        this.RADIUS = RADIUS;
        this.plusX = plusX;
        this.plusY = plusY;
    }

    public String getCarUrl()
    {
        return this.urlCar;
    }
    public String getUrlLifeIndicator()
    {
        return this.urlLifeIndicator;
    }
    public int getMaxHelath()
    {
        return this.maxHelath;
    }
    public int getGunDemage()
    {
        return this.gunDemage;
    }
    public int getPointsRequiredToUnlock()
    {
        return this.pointsRequiredToUnlock;
    }
    public boolean getIsBought()
    {
        return this.isBought;
    }

    public void setIsBoughtToTrue()
    {
        this.isBought=true;
    }
    public int getRADIUS(){return this.RADIUS;}
    public int getPlusX(){return this.plusX;}
    public int getPlusY(){return  this.plusY;}



}
