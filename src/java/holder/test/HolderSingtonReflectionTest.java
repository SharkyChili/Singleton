package holder.test;

import doubleCheckLock.DclSingleton;
import holder.HolderSington;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class HolderSingtonReflectionTest {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        HolderSington s1 = HolderSington.getInstance();
        HolderSington s2 = HolderSington.getInstance();

        Constructor<?>[] constructors = HolderSington.class.getDeclaredConstructors();
        Constructor<HolderSington> constructor = (Constructor<HolderSington>)constructors[0];
        constructor.setAccessible(true);
        HolderSington s3 = constructor.newInstance();
        System.out.println("非反射下的两个实例是否相同："+ (s1==s2));
        System.out.println("反射下的两个实例是否相同："+ (s1==s3));
    }
}
