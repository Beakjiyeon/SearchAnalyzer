package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.Hosts;
import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.entity.Words;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordsRepository extends JpaRepository<Words, Long> {
    List<Words> findAllByName(String keyword);

    @Query(value = "SELECT * from word where w.name = :keyword and w.word = :word", nativeQuery = true)
    List<Words> getSearchCountByNameWord(String keyword, String word);


    @Modifying // select 문이 아님을 나타낸다searchCount
    @Query(value = "UPDATE word w set (w.count, w.search_count, w.flag) = (:count ,:searchCount, :flag) where w.keyword = :keyword and w.word = :word", nativeQuery = true)
    void changeCount(@Param("count")int count, @Param("searchCount") int searchCount, @Param("word") String word, @Param("keyword") String keyword, @Param("flag")String flag) throws Exception;

    Page<Words> findByNameLike(String keyword, Pageable pageable); // remove

    // Optional<List<Words>> findByNameLike(String keyword);
}
