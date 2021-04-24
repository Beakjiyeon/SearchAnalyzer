package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.service.KeywordService;
import kr.co.tbase.searchad.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class KeywordController {
    private final KeywordService keywordService;
    private final MemberService memberService;

    // 실시간 검색 결과 페이지
    // 메인 페이지
    @GetMapping("/search")
    public String search() {
        return "search";
    }

    // 키워드 검색 : 크롤링
    @RequestMapping(value = "/search/keyword", method = RequestMethod.POST)
    public String searchKeyword(String keyword, Model models) throws Exception {
        models.addAttribute("data", keywordService.getKeywordByGoogle(keyword));
        return "search";
    }

    // 호스트 통계
    @GetMapping("/host")
    public String getHosts(Model models,
                           @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {

        Integer[] pageList = memberService.getPageList(pageNum, "host");
        models.addAttribute("pageList", pageList);
        models.addAttribute("start", (pageNum - 1) * 10 + 1);
        models.addAttribute("data", keywordService.getHostList(null, pageNum));

        return "host";
    }

    // 호스트 검색어
    @RequestMapping(value = "/search/host", method = RequestMethod.POST)
    public String filterHostList(String keyword, Model models) {
        Integer[] pageList = memberService.getPageList(1, "host");

        models.addAttribute("pageList", pageList);
        models.addAttribute("start", 1);
        // host 테이블에서 키워드가 keyword인 객체를 찾기
        models.addAttribute("data", keywordService.getHostList(keyword, 1));//findHostByKeyword(keyword, 1)

        return "host";
    }


    // 연관 검색어
    @GetMapping("/relatedwords")
    public String getWords(Model models,
                           @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {

        Integer[] pageList = memberService.getPageList(pageNum, "relate");
        models.addAttribute("pageList", pageList);
        models.addAttribute("start", (pageNum - 1) * 10 + 1);

        models.addAttribute("data", keywordService.getWordsList(null, pageNum));

        return "relatedWords";
    }

    // 연관 검색어 페이지 검색
    @RequestMapping(value = "/search/relatedWords", method = RequestMethod.POST)
    public String filterRelatedWordsList(String keyword, Model models) {
        Integer[] pageList = memberService.getPageList(1, "related");
        models.addAttribute("pageList", pageList);
        models.addAttribute("start", 1);


        models.addAttribute("data", keywordService.getWordsList(keyword, 1)); //findRelatedtByKeyword(keyword, 1));
        return "relatedWords";
    }


}
