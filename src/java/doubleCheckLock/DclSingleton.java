package doubleCheckLock;

import java.io.Serializable;

public class DclSingleton implements Serializable {
    private volatile static DclSingleton instance = null;

    private DclSingleton(){}

    public static DclSingleton getInstance() {
        if (instance == null) {
            synchronized (DclSingleton.class) {
                if (instance == null) {
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }

}
