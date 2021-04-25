package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.dto.ApiDto;
import kr.co.tbase.searchad.service.MemberService;
import kr.co.tbase.searchad.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class MemberRestController {

    MemberService memberService;

    @RequestMapping(value = "/check/id", method = RequestMethod.GET)
    public ApiDto checkId(String userId) {
        ApiDto apiDto = new ApiDto();

        if (userId.equals("")) {
            apiDto.setResult(CommonConstant.FAIL_CODE);
            apiDto.setMessage(CommonConstant.NONE_TARGET_ERROR_MSG);
        } else {
            try {
                memberService.loadUserByUsername(userId);
                apiDto.setResult(CommonConstant.FAIL_CODE);
                apiDto.setMessage(CommonConstant.DUPLICATE_TARGET_ERROR_MSG);
            } catch (Exception e) {
                apiDto.setResult(CommonConstant.SUCCESS_CODE);
                apiDto.setMessage(CommonConstant.SUCCESS_MSG);
            }
        }

        return apiDto;
    }

}
