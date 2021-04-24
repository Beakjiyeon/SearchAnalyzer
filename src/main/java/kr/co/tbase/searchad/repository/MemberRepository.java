package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByUserId(String userId);
    Page<Members> findAllByUserType(String userType, Pageable pageable);

    Optional<List<Members>> findByUserNameContaining(String userName);
    Optional<List<Members>> findByUserNameLike(String userName);

    String deleteByUserId(String userId);
}