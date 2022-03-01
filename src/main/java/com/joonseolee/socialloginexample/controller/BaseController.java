package com.joonseolee.socialloginexample.controller;

import com.joonseolee.socialloginexample.config.TestConfigurationProperties;
import com.joonseolee.socialloginexample.entity.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@EnableConfigurationProperties(value = TestConfigurationProperties.class)
public class BaseController {

    private final HttpSession httpSession;
    private final TestConfigurationProperties testConfigurationProperties;

    @GetMapping
    public String hello(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        } else {
            model.addAttribute("userName", "");
        }

        return "index";
    }

    @GetMapping("/properties")
    public TestConfigurationProperties getProperties() {
        return this.testConfigurationProperties;
    }

}
