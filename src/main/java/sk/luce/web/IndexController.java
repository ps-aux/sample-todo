package sk.luce.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.luce.security.DbUserDetailService;

import java.util.Base64;

@Controller
public class IndexController {

    @Autowired
    private DbUserDetailService userService;

    @RequestMapping("/")
    public String appEntryPoint(Authentication auth, Model model) {
        String token = buildToken(auth);
        model.addAttribute("token", token);

        return "app";
    }

    /**
     * Just simple basic authentication with
     * bcrypted password
     *
     * Can be replaced by JWT if needed
     */
    private String buildToken(Authentication auth) {
        UserDetails user = userService.loadUserByUsername(auth.getName());

        String creds = user.getUsername() + ":" + user.getPassword();

        return "Basic " + new String(Base64.getEncoder()
                .encode(creds.getBytes()));
    }
}
