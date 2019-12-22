package doubleCheckLock.test;

import doubleCheckLock.DclSingleton;

import java.io.*;

public class DclSingletonSerializeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DclSingleton s1 = DclSingleton.getInstance();
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Singleton.obj"));
        outputStream.writeObject(s1);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Singleton.obj"));
        DclSingleton s2 = (DclSingleton) inputStream.readObject();
        inputStream.close();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println("序列化前后是否为同一个：" + (s1==s2));
    }
}
