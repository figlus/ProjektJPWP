package model;

public enum CAR
{
    CAR_LEVEL_01("view/resources/cars/redCarBigger.png","view/resources/lifeIndicator.png",3,1,100,false),
    CAR_LEVEL_02("view/resources/cars/redCarBigger_02.png","view/resources/lifeIndicator.png",5,2,200,false),
    CAR_LEVEL_03("view/resources/cars/redCarCannon.png","view/resources/lifeIndicator.png",3,5,300,false);


    String urlLifeIndicator;
    String urlCar;
    int maxHelath;
    int gunDemage;
    int pointsRequiredToUnlock; //trzeba dodac jakaz zaleznosc odblokowywania kolejnych pojazdow zaleznie od poziomu/ zdobytych w grze punktow
    boolean isBought;


    private CAR(String urlCar, String urlLifeIndicator, int maxHealth, int gunDemage, int pointsRequiredToUnlock, boolean isBought)
    {
        this.urlCar = urlCar;
        this.urlLifeIndicator = urlLifeIndicator;
        this.maxHelath = maxHealth;
        this.gunDemage = gunDemage;
        this.pointsRequiredToUnlock = pointsRequiredToUnlock;
        this.isBought = isBought;
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

    //public void setIsBoughtToTrue



}
