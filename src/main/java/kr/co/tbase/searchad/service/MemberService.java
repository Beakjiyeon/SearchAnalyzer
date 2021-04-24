package kr.co.tbase.searchad.service;

import kr.co.tbase.searchad.domain.Role;
import kr.co.tbase.searchad.dto.MemberDto;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.repository.ContentsRepository;
import kr.co.tbase.searchad.repository.HostsRepository;
import kr.co.tbase.searchad.repository.MemberRepository;
import kr.co.tbase.searchad.repository.WordsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;
    HostsRepository hostsRepository;
    ContentsRepository contentsRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5;  // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 10;       // 한 페이지에 존재하는 게시글 수

    @Transactional
    public List<Members> getMemberList(Integer pageNum){
        //PAGE_POST_COUNT
        //Page<Members> page = memberRepository
        //        .findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "userId")));


        Page<Members> page = memberRepository.findAllByUserType("member", PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "userId")));
        List<Members> memberEntities = page.getContent();
        List<MemberDto> memberDtoList = new ArrayList<>();
        /*
        for (Members members : memberEntities) {
            memberDtoList.add(this.convertEntityToDto(members));
        }

         */
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
/*
    private MemberDto convertEntityToDto(Members members) {
        return members.builder()
                .id(members.getId())
                .userId(members.getUserId())
                .pwd(members.getPwd())
                .userName(members.getUserName())
                .userType(members.getUserType())
                .build();
    }
*/

    @Transactional
    public Long getBoardCount(String usage) {
        if(usage.equals("host"))
            return hostsRepository.count();
        else if(usage.equals("user"))
            return memberRepository.count();
        else// (usage.equals("related"))
            return contentsRepository.count();
    }


    public Integer[] getPageList(Integer curPageNum, String usage) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 갯수
        Double postsTotalCount = Double.valueOf(this.getBoardCount(usage));

        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        Integer postsTotal = (int)Math.ceil(postsTotalCount/10);


        int calc = curPageNum / 5;
        Integer startPageNum = (curPageNum -1) * 5 + 1; //calc * 5 + 1;
        System.out.println("야2========================" + startPageNum);
        Integer blockLastPageNum = postsTotal > startPageNum + 4//(postsTotal % 5 < 4)
                ? startPageNum + 4 //postsTotal//(pt / 10) + 1
                : postsTotal; //startPageNum + 4;
        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        /*
        Integer blockLastPageNum = (postsTotalCount % 5) < 4 ?  > ((curPageNum/BLOCK_PAGE_NUM_COUNT + 1) * 5))
                ? ((curPageNum/BLOCK_PAGE_NUM_COUNT + 1) * 10)
                : totalLastPageNum;
        /*
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;
         */

        // 페이지 시작 번호 조정
        // Integer startPageNum = curPageNum/5;// (curPageNum <= 3) ? 1 : curPageNum - 2; // //

        // 페이지 번호 할당
        System.out.println("야========================" + blockLastPageNum);
        for (int val = startPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }

    @Transactional
    public List<Members> findNameLike(String userName){
        List<Members> membersList = memberRepository.findByUserNameContaining(userName).get();
        log.info("서비스", membersList.size());
        for(Members m : membersList){
            log.info("서비d스", m);
        }
        List<Members> membersList2 = memberRepository.findByUserNameLike(userName).get();
        log.info("서비sf스", membersList2.size());
        return membersList2;
    }


    @Transactional
    public Long joinUser(MemberDto memberDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPwd(passwordEncoder.encode(memberDto.getPwd()));

        return memberRepository.save(memberDto.toEntity()).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info(">>>>>sd>>>", userId);
        Optional<Members> userEntityWrapper = memberRepository.findByUserId(userId);

        Members userEntity = userEntityWrapper.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin").equals(userId)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            log.info(">>>>>>>>", userId);
            log.info(">>>>>>>>", userEntity.getPwd());

        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(userEntity.getUserId(), userEntity.getPwd(), authorities);

    }
    // 회원가입 시, 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        log.info("오류오류", errors.getFieldErrors());
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }
    @Transactional
    public boolean removeMemberByUserId(String userId) {
        String result = memberRepository.deleteByUserId(userId);
        log.info("리무브>>>>",result);
        if(result.equals(userId)){
            return true;
        }
        return false;

    }


}