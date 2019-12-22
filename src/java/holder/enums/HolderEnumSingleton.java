package holder.enums;

import java.io.Serializable;

public class HolderEnumSingleton implements Serializable {
    private HolderEnumSingleton(){}

    private enum InnerHolder{
        INSTANCE;
        private static HolderEnumSingleton instance = new HolderEnumSingleton();

        private HolderEnumSingleton getInstance(){
            return instance;
        }
    }

    public static HolderEnumSingleton getInstance(){
        return InnerHolder.INSTANCE.getInstance();
    }
}
