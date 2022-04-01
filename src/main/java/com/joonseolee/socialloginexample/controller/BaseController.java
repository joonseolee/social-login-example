package com.joonseolee.socialloginexample.controller;

import com.joonseolee.socialloginexample.config.TestConfigurationProperties;
import com.joonseolee.socialloginexample.entity.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

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

    @GetMapping("/youtube")
    public String youtube() {
        String url = "https://content-youtube.googleapis.com/youtube/v3/subscriptions?order=relevance&part=snippet&key=AIzaSyBY7aajVAkj6BVz9U5HGLZqyaj0P5HpXjg&channelId=UCsJ6RuBiTVWRX156FVbeaGg";
        RestTemplate restTemplate = new RestTemplate();
        Object obj = restTemplate.getForObject(url, Object.class);
        return "youtube";
    }

//    @GetMapping("/redirect")
//    public String redirect() {
//        return "redirect";
//    }
}
