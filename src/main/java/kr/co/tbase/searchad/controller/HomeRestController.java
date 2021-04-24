package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.dto.ApiDto;
import kr.co.tbase.searchad.service.HomeService;
import kr.co.tbase.searchad.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="/api", produces = "application/json")
public class HomeRestController {

    @Autowired
    HomeService homeService;

    @RequestMapping (value = "/member/info", method = RequestMethod.GET) //REST API 방법
    public ApiDto memberInfo (Model models) {
        log.info("call member.");

        ApiDto apiDto = new ApiDto();

        // Member member = homeService.memberInfo();

        // apiDto.setData(member);
        log.info(String.valueOf(apiDto));
        apiDto.setStatus(CommonConstant.HTTP_OK);
        apiDto.setMessage(CommonConstant.HTTP_OK_MESSAGE);
        models.addAttribute("date", homeService.getStringCurrentDate());

        return apiDto;
    }
}
