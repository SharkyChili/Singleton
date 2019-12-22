package holder;

import java.io.Serializable;

public class HolderSington implements Serializable {
    /**
     * 静态的成员内部类
     * 内部类的实例与外部类的实例没有绑定关系
     * 也就是只有被调用到才会装载，也就是实现了延迟加载
     */
    private static class InnerHolder{
        /**
         * 静态初始化，由JVM来保证安全
         */
        private static HolderSington instance = new HolderSington();
    }

    private HolderSington(){}

    public static HolderSington getInstance(){
        return InnerHolder.instance;
    }
}
