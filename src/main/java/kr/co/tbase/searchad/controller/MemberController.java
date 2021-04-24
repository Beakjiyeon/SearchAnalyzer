package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.dto.MemberDto;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String dispSignup() {
        return "join";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String execSignup(MemberDto memberDto, Errors errors, Model model) {
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
    @GetMapping("/login")
    public String dispLogin() {
        return "login.html";
    }

    // 로그인 결과 페이지
    @GetMapping("/login/result")
    public String dispLoginResult() {
        return "loginSuccess.html";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/logout/result")
    public String dispLogout() {
        return "logout.html";
    }

    // 접근 거부 페이지
    @GetMapping("/denied")
    public String dispDenied() {
        return "denied.html";
    }

    // 내 정보 페이지
    @GetMapping("/info")
    public String dispMyInfo() {
        return "myinfo.html";
    }

    // 어드민 페이지
    @GetMapping("/admin/users")
    public String dispAdmin(Model models,
                            @RequestParam(value="page", defaultValue = "1") Integer pageNum) {


        List<Members> data = memberService.getMemberList(pageNum);
        Integer[] pageList = memberService.getPageList(pageNum, "user");

        //models.addAttribute("boardList", boardList);
        models.addAttribute("pageList", pageList);

        models.addAttribute("data", data);
        models.addAttribute("start", (pageNum-1) * 10 + 1);
        return "admin.html";
    }

    // 어드민 페이지 삭제
    @RequestMapping(value = "/admin/user")
    public String removeMember(Model models,
                               @RequestParam(value="user-id") String userId) {
        System.out.println("======================"+userId);
        if(userId != null){
            boolean result = memberService.removeMemberByUserId(userId);

            /*
            if(result == false){
                return "error.html";
            }

             */
        }
        List<Members> data = memberService.getMemberList(1);
        Integer[] pageList = memberService.getPageList(1, "user");
        models.addAttribute("pageList", pageList);
        models.addAttribute("start", 1);
        models.addAttribute("data", data);
        return "admin.html";
    }

    // 어드민 페이지 검색
    @RequestMapping(value = "/admin/search/member", method = RequestMethod.POST)
    public String filterMemberList(String userName, Model models) {
        Integer[] pageList = memberService.getPageList(1 ,"user");
        models.addAttribute("pageList", pageList);
        models.addAttribute("start", 1);
        models.addAttribute("data", memberService.findNameLike(userName));
        return "admin.html";
    }


}