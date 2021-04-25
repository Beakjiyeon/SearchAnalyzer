package kr.co.tbase.searchad.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto {
    private Integer[] pageList;
    private int prev;
    private int next;

    public PageDto(Integer[] pageList, int prev, int next) {
        this.pageList = pageList;
        this.prev = prev;
        this.next = next;
    }
}
