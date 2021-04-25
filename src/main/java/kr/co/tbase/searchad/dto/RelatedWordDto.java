package kr.co.tbase.searchad.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RelatedWordDto {
    private String keyword;
    private String relatedwords;

    public RelatedWordDto(String keyword, String relatedwords) {
        this.keyword =keyword;
        this.relatedwords =relatedwords;
    }

}
