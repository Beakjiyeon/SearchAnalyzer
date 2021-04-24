package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.Hosts;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.entity.Words;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HostsRepository extends JpaRepository<Hosts, Long> {
    // Optional<Hosts> findByName(String keyword);

    @Modifying // select 문이 아님을 나타낸다
    @Query(value = "UPDATE host h set h.count = :count where h.domain = :domain and h.keyword = :keyword", nativeQuery = true)
    void changeCount(@Param("count")int count, @Param("domain")String domain, @Param("keyword")String keyword) throws Exception;

    List<Hosts> findAllByName(String keyword);

    @Query(value = "SELECT * from host h where h.keyword =:keyword and h.domain = :domain", nativeQuery = true)
    List<Hosts> findAllByKeywordName(String keyword, String domain);

    //@Query(value = "select * from host where h.keyword = :keyword and h.domain = :domain", nativeQuery = true)
    // Optional<Hosts> findByDomainKeyword(String Domain, String keyword);
    // Optional<List<Hosts>> findByNameLike(String keyword);
    Page<Hosts> findByNameLike(String keyword, Pageable pageable);
}

