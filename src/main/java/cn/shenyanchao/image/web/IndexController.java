package cn.shenyanchao.image.web;

import cn.shenyanchao.image.entity.ImageForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @date 14-1-17.
 * @author shenyanchao
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String autonavi(Model model) {
        model.addAttribute("imageForm",new ImageForm());
        return "index";
    }

    @RequestMapping(value = "/{jsp}", method = RequestMethod.GET)
    public String autonavi(@PathVariable String jsp,Model model) {
        model.addAttribute("imageForm",new ImageForm());
        return jsp;
    }


}
