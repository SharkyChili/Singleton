package effectiveJava;

public class EjSingleton2 {
    //private
    private static final EjSingleton2 instance = new EjSingleton2();

    private EjSingleton2(){}

    public static EjSingleton2 getInstance(){
        return instance;
    }

    public void leaveTheBuilding(){}
}
