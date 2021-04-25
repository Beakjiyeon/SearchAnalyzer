package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.dto.PageDto;
import kr.co.tbase.searchad.service.KeywordService;
import kr.co.tbase.searchad.service.MemberService;
import lombok.AllArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public String searchKeyword(String keyword, Model models) {
        try {
            models.addAttribute("data", keywordService.getKeywordByGoogle(keyword));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "search";
    }

    // 호스트 통계
    @GetMapping("/host")
    public String getHosts(Model models, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        try {
            PageDto pageDto = memberService.getPageList(pageNum, "host");

            models.addAttribute("pageList", pageDto.getPageList());
            models.addAttribute("prev", pageDto.getPrev());
            models.addAttribute("next", pageDto.getNext());
            models.addAttribute("start", (pageNum - 1) * 10 + 1);
            models.addAttribute("data", keywordService.getHostList(null, pageNum, 1));
            models.addAttribute("sortAsc", 1);

        } catch (Exception e) {
            e.getMessage();
        }

        return "host";
    }

    // 호스트 검색어
    @RequestMapping(value = "/search/host", method = RequestMethod.POST)
    public Model filterHostList(String keyword, int sortAsc, Model models) {

        if (keyword.equals("")) {
            keyword = null;
        }

        PageDto pageDto = memberService.getPageList(1, "host");
        models.addAttribute("prev", pageDto.getPrev());
        models.addAttribute("next", pageDto.getNext());
        models.addAttribute("pageList", pageDto.getPageList());
        models.addAttribute("start", 1);
        models.addAttribute("data", keywordService.getHostList(keyword, 1, sortAsc));
        models.addAttribute("sortAsc", sortAsc);

        return models;
    }

    // 연관 검색어
    @GetMapping("/relatedwords")
    public String getWords(Model models, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        try {
            models.addAttribute("data", keywordService.getWordsList(null, pageNum));
            PageDto pageDto = memberService.getPageList(pageNum, "relate");
            models.addAttribute("pageList", pageDto.getPageList());
            models.addAttribute("prev", pageDto.getPrev());
            models.addAttribute("next", pageDto.getNext());
            models.addAttribute("start", (pageNum - 1) * 10 + 1);

        } catch (Exception e) {
            e.getMessage();
        }

        return "relatedwords";
    }

    // 연관 검색어 페이지 검색
    @RequestMapping(value = "/search/relatedwords", method = RequestMethod.POST)
    public String filterRelatedWordsList(String keyword, Model models) {

        PageDto pageDto = memberService.getPageList(1, "related");

        models.addAttribute("pageList", pageDto.getPageList());
        models.addAttribute("prev", pageDto.getPrev());
        models.addAttribute("next", pageDto.getNext());
        models.addAttribute("start", 1);
        try {
            models.addAttribute("data", keywordService.getWordsList(keyword, 1));
        } catch (NullPointerException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "relatedwords";
    }


}
