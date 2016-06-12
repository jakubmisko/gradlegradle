package hello;

import static  org.junit.Assert.assertTrue;
import org.junit.Test;

public class HelloWorldTest {
    @Test
    public void helloTest(){
        String hello = HelloWorld.hello("Gradle");
        assertTrue("Hello gradle! was expected but returned "+hello, hello.equals("Hello Gradle!"));
    }
}
