package cn.shenyanchao.image.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by shenyanchao on 14-1-17.
 * @author shenyanchao
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String autonavi() {
        return "index";
    }

    @RequestMapping(value = "/{jsp}", method = RequestMethod.GET)
    public String autonavi(@PathVariable String jsp) {
        return jsp;
    }


}
