package kr.co.tbase.searchad.repository;


import kr.co.tbase.searchad.entity.Contents;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.entity.Words;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Optional<List<Contents>> findByKeywordLike(String keyword);

    Optional<List<Contents>> findByKeyword(String keyword);

    @Modifying
    @Query(value = "UPDATE content c set c.related_words = :relatedWords where c.keyword = :keyword", nativeQuery = true)
    void changeRelatedWords(String keyword, String relatedWords);

    Page<Contents> findByKeywordLike(String keyword, Pageable pageable);
}
