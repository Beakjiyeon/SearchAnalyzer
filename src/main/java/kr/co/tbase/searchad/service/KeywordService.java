package kr.co.tbase.searchad.service;

import kr.co.tbase.searchad.dto.MemberDto;
import kr.co.tbase.searchad.dto.RelatedWordDto;
import kr.co.tbase.searchad.entity.*;
import kr.co.tbase.searchad.repository.ContentsRepository;
import kr.co.tbase.searchad.repository.HostsRepository;
import kr.co.tbase.searchad.repository.KeywordsReprository;
import kr.co.tbase.searchad.repository.WordsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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


    private static final int PAGE_POST_COUNT = 10 ;

    KeywordsReprository keywordsReprository;
    HostsRepository hostsRepository;
    WordsRepository wordsRepository;
    ContentsRepository contentsRepository;

    @Transactional
    public List<Keywords> getKeywordByGoogle(String keyword) throws Exception {

        String url = "https://www.google.com/search?q=" + keyword;
        Document doc = Jsoup.connect(url).get();

        Elements shortDoc = doc.select(".g"); //tF2Cxc
        String link, title, content;
        int len;
        int count = 0;

        List<Keywords> keywords = new ArrayList<Keywords>();
        List<Hosts> hosts = new ArrayList<Hosts>();
        Keywords temp;
        Hosts temp2;
        String domain = null;
        int original, now;
        Map<String, Integer> map = new HashMap<String, Integer>();

        String totalContent = "";
        for(Element e : shortDoc){

            link = e.select(".yuRUbf").select("a").attr("href");
            title = e.select("h3").select(".LC20lb.DKV0Md").text();
            content = e.select(".aCOpRe").text();
            totalContent = totalContent + content +" ";

            String[] sarr = content.split(" ");
            len = sarr.length;
            count = 0;

            for(int i = 0; i<len; i++){
                System.out.println(sarr[i]+" / "+keyword);
                if(sarr[i].contains(keyword)){
                    count++;
                }
            }

            temp = new Keywords(keyword, link, title, content, count);
            if(link!=null && title!=null){
                keywords.add(temp);
            }

            keywordsReprository.saveAll(keywords);
            System.out.println("111");


            // link -> domain
            if(link.length() == 0){
                domain = link;
                System.out.println("ㅋㅋㅋㅋ" + link +"zzz");
            }else{
                System.out.println("ㅋㅋㅋㅋ2" + link +"zzz");
                URL aURL = new URL(link);
                domain = aURL.getAuthority();
                // 각 element의 도메인을 map에 넣어 개수 count
                if(map.containsKey(domain)){
                    original = map.get(domain);
                    now = original + 1;
                    map.put(domain, now);
                }else {
                    map.put(domain, 1);
                }
            }

            System.out.println("도메인"+ domain);



        }
        System.out.println("222");

        // 호스트 테이블 누적
        int age = 100;
        boolean flag =false; // data에 키워드랑 같은 게 있는지
        Iterator<String> keys = map.keySet().iterator();
        while( keys.hasNext() ){

            String key = keys.next();
            System.out.println("333 "+ key);
            int dbCount = 0;

            List<Hosts> hostList = hostsRepository.findAllByKeywordName(keyword, key);
            if(hostList.size() !=0 ){
                // update
                Hosts host = hostList.get(0);
                int c = map.get(key) + host.getCount();
                hostsRepository.changeCount(c, key, keyword);
            }else{
                // insert
                hostsRepository.save(new Hosts(keyword, key, map.get(key)));
            }

            /*
            List<Hosts> data = hostsRepository.findAllByName(keyword);


            if(data!=null){
                for(Hosts e : data){
                    if(e.getDomain().equals(domain)){
                        dbCount = e.getCount();
                        flag = true;
                    }
                }
                System.out.println("444 ");
                if(flag){
                    hostsRepository.changeCount(map.get(key)+dbCount, key, keyword);
                }
                else{
                    hostsRepository.save(new Hosts(keyword, key, map.get(key)));
                }
            }
            */


        }

        // 워드 테이블 누적
        // 엔티티 본문을 모두 합친 String totalContent
        System.out.println("total = "+ totalContent);
        String[] arr = totalContent.split(" ");
        for (String s : arr){
            // 조사 제거

        }
        // 각 단어의 개수를 count;
        Map<String, Integer> wordMap = new HashMap<String, Integer>();
        int original2;
        for (String s : arr){
            if(wordMap.containsKey(s)){
                original2 = wordMap.get(s) + 1;
            }else{
                wordMap.put(s, 1);
            }
        }
        // 맵을 순회하며 db에 (keyword, word)가 있는지 확인
        Iterator<String> wkeys = wordMap.keySet().iterator();
        while( wkeys.hasNext() ){

            String key = wkeys.next();

            int originalSearchCount = 0;
            int originalCount = 0;
            List<Words> data = wordsRepository.findAllByName(keyword);

            if(data!=null){
                for(Words e : data){
                    if(e.getWord().equals(key)){
                        //
                        originalSearchCount = e.getSearchCount();
                        originalCount = e.getCount();
                        flag = true;
                    }
                }
                String relatedFlag = "";
                if(flag){
                    // List<Words> word =  wordsRepository.getSearchCountByNameWord(keyword, key);
                    // int originalSearchCount = word.get(0).getSearchCount();
                    int searchCount = originalSearchCount + 1;
                    int wordCount = originalCount + 1;
                    double d = (double) wordCount/searchCount;
                    System.out.println("ㅋㅋㅋㅋㅋㅋ"+d);
                    if( d >= 0.5){
                        relatedFlag = "related";
                    }else{
                        relatedFlag = "notRelated";
                    }
                    // 연관 검색어 인지 여부 계산
                    wordsRepository.changeCount(wordCount, searchCount, key, keyword, relatedFlag);


                }
                else{
                    relatedFlag = "related"; // 처음로 1번 검색했을 때 출현 여부가 true이므로!
                    wordsRepository.save(new Words(keyword, key, 1, 1,relatedFlag));
                }
            }

        }





        return keywords;
    }
    @Transactional
    public List<Hosts> getHosts(String keyword) {

        List<Hosts> hosts = new ArrayList<Hosts>();
        if(keyword == null){
            hosts = hostsRepository.findAll();
        }
        else{
            // 수정해야 함
            hosts = hostsRepository.findAll();
        }

        return hosts;
    }
    @Transactional
    public List<RelatedWordDto> getWords(String keyword) {
        // keyword에 대한 db를 가져와 dto 생성 후 return
        List<RelatedWordDto> dtoList = new ArrayList<RelatedWordDto>();
        if(keyword != null){

            //Page<Words> page = wordsRepository.findByNameLike(keyword, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "name")));
            // List<Words> list = page.getContent();
            // System.out.println("==================================================pagex>> "+list.size());
            List<Words> list = wordsRepository.findAllByName(keyword);
            String temp="";
            for(Words w : list){

                temp = temp + ", " + w.getWord();

            }
            RelatedWordDto relatedWordDto = new RelatedWordDto(keyword,temp);
            dtoList.add(relatedWordDto);
        }else{

            Map<String, String> map = new HashMap<String, String>();
            // Page<Words> page = wordsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "name")));

            //List<Words> list = page.getContent();
            //System.out.println("==================================================page>> "+list.size());
            List<Words> list = wordsRepository.findAll();
            String temp="";
            for(Words w : list){
                if(map.containsKey(w.getName())){
                    String s = map.get(w.getName()) + w.getWord();
                    map.put(w.getName(), s);
                }else{
                    map.put(w.getName(), w.getWord());
                }
            }//endfor

            // map에 있는 dto 리스트 삽입
            Iterator<String> x = map.keySet().iterator();
            while( x.hasNext() ) {
                String key = x.next();
                dtoList.add(new RelatedWordDto(key, map.get(key)));
            }
        }

        List<Contents> clist = new ArrayList<Contents>();
        for(RelatedWordDto l : dtoList){
            if(contentsRepository.findByKeyword(l.getKeyword()).get().size() != 0){
                // if(l.getRelatedWords().equals("") || l.getRelatedWords()!=null)
                contentsRepository.changeRelatedWords(l.getKeyword(), l.getRelatedWords());
            }else{
                clist.add(new Contents(l.getKeyword(), l.getRelatedWords()));
                // contentsRepository.save(new Contents(l.getKeyword(), l.getRelatedWords()));
            }
        }
        System.out.println("========================================================="+clist);
        contentsRepository.saveAll(clist);
        return dtoList;
    }
    /*
    @Transactional
    public List<Hosts> findHostByKeyword(String keyword) {
        List<Hosts> hosts = hostsRepository.findByNameLike(keyword).get();
        return hosts;
    }

     */
    @Transactional
    public List<Contents> findRelatedtByKeyword(String keyword) {
        List<Contents> words = contentsRepository.findByKeywordLike(keyword).get();
        return words;
    }


    @Transactional
    public List<Hosts> getHostList(String keyword, Integer pageNum){
        Page<Hosts> page;

        if(keyword == null){
            page = hostsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "count")));

        }else{
            page = hostsRepository.findByNameLike(keyword, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "count")));

        }
        return page.getContent();
        /*
        for (Iterator<Hosts> iter = hostsEntities.iterator(); iter.hasNext(); ) {
            Hosts hosts = iter.next();

            if (hosts.getUserType().equals("member")){
                memberList.add(hosts);
                log.info("dddddd", hosts.getId());
            }
        }
        log.info("member리스트 개수", memberList.size());
        // List<Members> membersList = memberRepository.findAllByUserType("member").get();

        return memberList;

         */
    }
    @Transactional
    public List<Contents> getWordsList(String keyword, Integer pageNum) {
        getWords(keyword);// db 업데이트
        Page<Contents> page;
        if(keyword != null){
            page = contentsRepository.findByKeywordLike(keyword, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "keyword")));
        }else{
            page = contentsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "keyword")));
        }
        return page.getContent();
    }
/*
    @Transactional
    public List<Contents> getRelatedList(Integer pageNum){
        //PAGE_POST_COUNT
        //Page<Members> page = memberRepository
        //        .findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "userId")));

        //Page<Members> page = memberRepository.findAllByUserType("member", PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "userId")));
        //List<Members> memberEntities = page.getContent();
        // List<MemberDto> memberDtoList = new ArrayList<>();

        /*
        for (Members members : memberEntities) {
            memberDtoList.add(this.convertEntityToDto(members));
        }


        List<Members> memberList = new ArrayList<Members>();
        for (Iterator<Members> iter = memberEntities.iterator(); iter.hasNext(); ) {
            Members members = iter.next();

            if (members.getUserType().equals("member")){
                memberList.add(members);
                log.info("dddddd", members.getId());
            }
        }
        log.info("member리스트 개수", memberList.size());
        // List<Members> membersList = memberRepository.findAllByUserType("member").get();

        return memberList;
    }
    */
}
