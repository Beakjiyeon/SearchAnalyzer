package kr.co.tbase.searchad.service;

import kr.co.tbase.searchad.dto.RelatedWordDto;
import kr.co.tbase.searchad.entity.Contents;
import kr.co.tbase.searchad.entity.Hosts;
import kr.co.tbase.searchad.entity.Keywords;
import kr.co.tbase.searchad.entity.Words;
import kr.co.tbase.searchad.repository.ContentsRepository;
import kr.co.tbase.searchad.repository.HostsRepository;
import kr.co.tbase.searchad.repository.KeywordsRepository;
import kr.co.tbase.searchad.repository.WordsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class KeywordService {


    private static final int PAGE_POST_COUNT = 10;

    KeywordsRepository keywordsRepository;
    HostsRepository hostsRepository;
    WordsRepository wordsRepository;
    ContentsRepository contentsRepository;

    @Transactional
    public List<Keywords> getKeywordByGoogle(String keyword) throws Exception {

        if (keyword.replace(" ", "").equals("")) {
            throw new Exception();
        }

        String url = "https://www.google.com/search?q=" + keyword;
        Document doc = Jsoup.connect(url).get();

        Elements shortDoc = doc.select(".g"); //tF2Cxc
        String link, title, content;
        int len;
        int count;

        List<Keywords> keywords = new ArrayList<>();
        Keywords temp;
        String domain;
        int original, now;
        Map<String, Integer> map = new HashMap<>();

        StringBuilder totalContent = new StringBuilder();
        for (Element e : shortDoc) {

            link = e.select(".yuRUbf").select("a").attr("href");
            title = e.select("h3").select(".LC20lb.DKV0Md").text();
            content = e.select(".aCOpRe").text();
            totalContent.append(content).append(" ");

            String[] sarr = content.split(" ");
            len = sarr.length;
            count = 0;

            // 본문에서 검색어 출현 횟수 세기
            for (int i = 0; i < len; i++) {
                if (sarr[i].contains(keyword)) {
                    count++;
                }
            }

            temp = new Keywords(keyword, link, title, content, count);
            if (link != null && title != null) {
                keywords.add(temp);
            }
            // 검색 결과 DB 저장
            keywordsRepository.saveAll(keywords);


            assert link != null;
            if (link.length() == 0) {
                domain = link;
            } else {
                URL aURL = new URL(link);
                domain = aURL.getAuthority();
                // 각 element의 도메인을 map에 넣어 개수 count
                if (map.containsKey(domain)) {
                    original = map.get(domain);
                    now = original + 1;
                    map.put(domain, now);
                } else {
                    map.put(domain, 1);
                }
            }

        }

        // 호스트 테이블 누적
        boolean flag = false; // data에 키워드랑 같은 게 있는지
        for (String key : map.keySet()) {

            List<Hosts> hostList = hostsRepository.findAllByKeywordName(keyword, key);
            if (hostList.size() != 0) {
                // update
                Hosts host = hostList.get(0);
                int c = map.get(key) + host.getCount();
                hostsRepository.changeCount(c, key, keyword);
            } else {
                // insert
                hostsRepository.save(new Hosts(keyword, key, map.get(key)));
            }
        }

        String[] arr = totalContent.toString().split(" "); //extracted.toString().split(" ");

        Map<String, Integer> wordMap = new HashMap<>();
        int original2;
        for (String s : arr) {
            if (wordMap.containsKey(s)) {
                original2 = wordMap.get(s) + 1;

            } else {
                wordMap.put(s, 1);
            }
        }
        // 맵을 순회하며 db에 (keyword, word)가 있는지 확인
        Iterator<String> wkeys = wordMap.keySet().iterator();
        while (wkeys.hasNext()) {
            String key = wkeys.next();
            int originalSearchCount = 0;
            int originalCount = 0;
            List<Words> data = wordsRepository.findAllByName(keyword);

            if (data != null) {
                for (Words e : data) {
                    if (e.getWord().equals(key)) {
                        //
                        originalSearchCount = e.getSearchCount();
                        originalCount = e.getCount();
                        flag = true;
                    }
                }
                String relatedFlag;
                if (flag) {
                    // List<Words> word =  wordsRepository.getSearchCountByNameWord(keyword, key);
                    // int originalSearchCount = word.get(0).getSearchCount();
                    int searchCount = originalSearchCount + 1;
                    int wordCount = originalCount + 1;
                    double d = (double) wordCount / searchCount;
                    if (d >= 0.5) {
                        relatedFlag = "related";
                    } else {
                        relatedFlag = "notRelated";
                    }
                    // 연관 검색어 인지 여부 계산
                    wordsRepository.changeCount(wordCount, searchCount, key, keyword, relatedFlag);


                } else {
                    relatedFlag = "related"; // 처음로 1번 검색했을 때 출현 여부가 true이므로!
                    wordsRepository.save(new Words(keyword, key, 1, 1, relatedFlag));
                }
            }

        }

        return keywords;
    }

    @Transactional
    public void getWords(String keyword) {
        List<RelatedWordDto> dtoList = new ArrayList<>();
        if (keyword != null) {
            List<Words> list = wordsRepository.findAllByName(keyword);
            StringBuilder temp = new StringBuilder();
            for (Words w : list) {
                temp.append(", ").append(w.getWord());
            }
            RelatedWordDto relatedwordDto = new RelatedWordDto(keyword, temp.toString());
            dtoList.add(relatedwordDto);
        } else {
            Map<String, String> map = new HashMap<>();
            List<Words> list = wordsRepository.findAll();
            for (Words w : list) {
                if (map.containsKey(w.getName())) {
                    String s = map.get(w.getName()) + w.getWord();
                    map.put(w.getName(), s);
                } else {
                    map.put(w.getName(), w.getWord());
                }
            }

            // map에 있는 dto 리스트 삽입
            for (String key : map.keySet()) {
                dtoList.add(new RelatedWordDto(key, map.get(key)));
            }
        }

        List<Contents> clist = new ArrayList<>();

        for (RelatedWordDto l : dtoList) {

            // l에서 명사 추출
            String strTemp = extractContents(l.getRelatedwords());
            //strtemp.replaceFirst(",", "");
            strTemp = strTemp.substring(1, strTemp.length() - 1);
            l.setRelatedwords(strTemp);
            if (contentsRepository.findByKeyword(l.getKeyword()).get().size() != 0) {
                contentsRepository.changeRelatedWords(l.getKeyword(), l.getRelatedwords());
            } else {
                clist.add(new Contents(l.getKeyword(), l.getRelatedwords()));
            }
        }
        contentsRepository.saveAll(clist);
    }

    @Transactional
    public List<Hosts> getHostList(String keyword, Integer pageNum, int sortAsc) {
        Page<Hosts> page;

        if (keyword == null) {
            if (sortAsc == 1) {
                page = hostsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "count")));
            } else {
                page = hostsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "count")));
            }
        } else {
            if (sortAsc == 1) {
                page = hostsRepository.findByNameLike(keyword, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "count")));
            } else {
                page = hostsRepository.findByNameLike(keyword, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "count")));
            }
        }
        return page.getContent();
    }

    @Transactional
    public List<Contents> getWordsList(String keyword, Integer pageNum) {
        getWords(keyword);
        Page<Contents> page;
        if (keyword != null && keyword.length() > 0) {
            page = contentsRepository.findByKeywordLike(keyword, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "keyword")));
        } else {
            page = contentsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "keyword")));
        }
        return page.getContent();
    }

    // 라이브러리 테스트
    public String extractContents(String content) {
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(content, true);
        StringBuilder result = new StringBuilder();
        for (Keyword kwrd : kl) {
            result.append(", ").append(kwrd.getString());
        }
        return result.toString();

    }


}
