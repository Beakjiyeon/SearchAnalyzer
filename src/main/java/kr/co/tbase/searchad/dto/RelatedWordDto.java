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
    private String relatedWords;

    public RelatedWordDto(String keyword, String relatedWords) {
        this.keyword =keyword;
        this.relatedWords =relatedWords;
    }

}
