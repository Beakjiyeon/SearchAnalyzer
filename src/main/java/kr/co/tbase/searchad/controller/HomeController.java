package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @Autowired
    HomeService homeService;

    @GetMapping(value = {"", "/"})
    public String index(Model models) {
        log.debug("call index.");

        models.addAttribute("date", homeService.getStringCurrentDate());

        return "index.html";
    }
}
