package library;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class RandomHello {
    public String randomHello(int length){
        return "Hello "+ RandomStringUtils.random(length, true, true)+"!";
    }
}
