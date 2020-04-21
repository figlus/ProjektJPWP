package model;

public enum CAR
{
    CAR_LEVEL_01("view/resources/cars/redCarBigger.png","view/resources/lifeIndicator.png",3,1,0,false,false,
            23,25,35,23,25,94,23,-10),
    CAR_LEVEL_02("view/resources/cars/yellowCar1.png","view/resources/lifeIndicator.png",5,2,1000,false,false,
            23,25,35,23,25,94,25,-10),
    CAR_LEVEL_03("view/resources/cars/blueCar.png","view/resources/lifeIndicator.png",3,5,3000,false,false,
            23,25,25,23,25,92,23,4),
    CAR_LEVEL_04("view/resources/cars/greyCar.png","view/resources/lifeIndicator.png",7,1,5000,false,true,
            22,24,33,23,24,77,23,0);


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

    int RADIUS2;
    int plusX2;
    int plusY2;

    int muzzleX;
    int muzzleY;


    private CAR(String urlCar, String urlLifeIndicator, int maxHealth, int gunDemage, int pointsRequiredToUnlock, boolean isBought, boolean isBoat,
                int RADIUS,int plusX, int plusY, int RADIUS2, int plusX2, int plusY2,int muzzleX, int muzzleY)
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

        this.RADIUS2 = RADIUS2;
        this.plusX2 = plusX2;
        this.plusY2 = plusY2;

        this.muzzleX = muzzleX;
        this.muzzleY = muzzleY;
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

    public int getRADIUS2(){return this.RADIUS2;}
    public int getPlusX2(){return this.plusX2;}
    public int getPlusY2(){return  this.plusY2;}

    public int getMuzzleX(){return this.muzzleX;}
    public int getMuzzleY(){return this.muzzleY;}



}
