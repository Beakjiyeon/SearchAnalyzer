package kr.co.tbase.searchad.service;

import kr.co.tbase.searchad.domain.Role;
import kr.co.tbase.searchad.dto.MemberDto;
import kr.co.tbase.searchad.dto.PageDto;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.repository.ContentsRepository;
import kr.co.tbase.searchad.repository.HostsRepository;
import kr.co.tbase.searchad.repository.MemberRepository;
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

    private final MemberRepository memberRepository;
    HostsRepository hostsRepository;
    ContentsRepository contentsRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5;  // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 10;       // 한 페이지에 존재하는 게시글 수

    @Transactional
    public List<Members> getMemberList(Integer pageNum) {

        Page<Members> page = memberRepository.findAllByUserType("member", PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "userId")));
        List<Members> memberEntities = page.getContent();

        List<Members> memberList = new ArrayList<>();
        for (Members members : memberEntities) {
            if (members.getUserType().equals("member")) {
                memberList.add(members);
            }
        }

        return memberList;
    }

    @Transactional
    public Long getBoardCount(String usage) {
        if (usage.equals("host")) {
            return hostsRepository.count();
        } else if (usage.equals("user")) {
            return memberRepository.count();
        } else {
            return contentsRepository.count();
        }
    }


    public PageDto getPageList(Integer curPageNum, String usage) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        Double postsTotalCount;
        postsTotalCount = Double.valueOf(this.getBoardCount(usage));

        // 총 게시글 갯수
        if (usage.equals("user")) {
            postsTotalCount -= 1;
        }

        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        int postsTotal = (int) Math.ceil(postsTotalCount / PAGE_POST_COUNT);
        int calc = curPageNum % BLOCK_PAGE_NUM_COUNT;

        if (calc == 0) {
            calc = 5;
        }

        int startPageNum = curPageNum - calc + 1;
        int blockLastPageNum = Math.min(postsTotal, startPageNum + (BLOCK_PAGE_NUM_COUNT - 1));

        // 페이지 번호 할당
        for (int val = startPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        int prev = 0;
        int next;

        if (pageList[0] != null) {
            if (pageList[0] == 1) {
                prev = 0;
            } else {
                prev = pageList[0] - BLOCK_PAGE_NUM_COUNT;
            }
        }

        if (blockLastPageNum < postsTotal) {
            next = blockLastPageNum + 1;
        } else {
            next = 0;
        }
        return new PageDto(pageList, prev, next);

    }

    @Transactional
    public List<Members> findByType(String type, String value) {
        List<Members> membersList;
        switch (type) {
            case "name":
                membersList = memberRepository.findByUserNameContaining(value).get();
                break;
            case "id":
                membersList = memberRepository.findByUserIdContaining(value).get();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        return membersList;
    }


    @Transactional
    public void joinUser(MemberDto memberDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPwd(passwordEncoder.encode(memberDto.getPwd()));

        memberRepository.save(memberDto.toEntity());
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Members> userEntityWrapper = memberRepository.findByUserId(userId);

        Members userEntity = userEntityWrapper.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin").equals(userId)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));

        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(userEntity.getUserId(), userEntity.getPwd(), authorities);

    }

    // 회원가입 시, 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Transactional
    public boolean removeMemberByUserId(String userId) {
        String result = memberRepository.deleteByUserId(userId);

        return result.equals(userId);
    }


}