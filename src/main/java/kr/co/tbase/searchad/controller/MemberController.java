package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.dto.MemberDto;
import kr.co.tbase.searchad.dto.PageDto;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 메인 페이지
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String pageIndex() {
        return "index";
    }

    // 회원가입 페이지
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String pageJoin() {
        return "join";
    }

    // 회원가입 처리
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registering(@Valid MemberDto memberDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("userDto", memberDto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
        }
        memberService.joinUser(memberDto);
        return "redirect:/login";
    }


    // 로그인 페이지
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String dispLogin() {
        return "login";
    }

    // 접근 거부 페이지
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String dispDenied() {
        return "denied";
    }

    // 내 정보 페이지
    @RequestMapping(value = "/myInfo", method = RequestMethod.GET)
    public String dispMyInfo() {
        return "myInfo";
    }

    // 어드민 페이지
    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String dispAdmin(Model models, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {

        List<Members> data = memberService.getMemberList(pageNum);
        PageDto pageDto = memberService.getPageList(pageNum, "user");

        models.addAttribute("prev", pageDto.getPrev());
        models.addAttribute("next", pageDto.getNext());
        models.addAttribute("pageList", pageDto.getPageList());

        models.addAttribute("data", data);
        models.addAttribute("start", (pageNum - 1) * 10 + 1);

        return "admin";
    }

    // 어드민 페이지 삭제
    @RequestMapping(value = "/admin/user/delete", method = RequestMethod.GET)
    public String removeMember(Model models, @RequestParam(value = "user-id") String userId) {
        if (userId != null) {
            boolean result = memberService.removeMemberByUserId(userId);
        }
        List<Members> data = memberService.getMemberList(1);
        PageDto pageDto = memberService.getPageList(1, "user");
        models.addAttribute("prev", pageDto.getPrev());
        models.addAttribute("next", pageDto.getNext());

        models.addAttribute("pageList", pageDto.getPageList());
        models.addAttribute("start", 1);
        models.addAttribute("data", data);

        return "admin";
    }

    // 어드민 페이지 검색
    @RequestMapping(value = "/admin/search/member", method = RequestMethod.POST)
    public String filterMemberList(String type, String value, Model models) {
        PageDto pageDto = memberService.getPageList(1, "user");
        models.addAttribute("pageList", pageDto.getPageList());
        models.addAttribute("prev", pageDto.getPrev());
        models.addAttribute("next", pageDto.getNext());
        models.addAttribute("start", 1);
        models.addAttribute("data", memberService.findByType(type, value));

        return "admin";
    }


}