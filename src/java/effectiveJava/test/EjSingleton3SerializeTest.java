package effectiveJava.test;

import doubleCheckLock.DclSingleton;
import effectiveJava.EjSingleton3;

import java.io.*;

public class EjSingleton3SerializeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        EjSingleton3 s1 = EjSingleton3.INSTANCE;
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("EjSingleton3.obj"));
        outputStream.writeObject(s1);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("EjSingleton3.obj"));
        EjSingleton3 s2 = (EjSingleton3) inputStream.readObject();
        inputStream.close();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println("序列化前后是否为同一个：" + (s1==s2));
    }
}
