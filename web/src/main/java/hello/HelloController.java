package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jakub on 11.06.2016.
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }
}
