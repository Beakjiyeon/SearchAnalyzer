package kr.co.tbase.searchad.dto;
import java.time.LocalDateTime;

import com.sun.istack.NotNull;
import kr.co.tbase.searchad.entity.Members;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
    private int id;

    @Valid
    @NotEmpty(message = "유저 ID는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[a-zA-Z]).{1,10}",
            message = "유저 ID는 영문으로만 10자 이내여야 합니다.")
    private String userId;


    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}",
            message = "비밀번호는 8자 이상이고, 2개 이상 중복된 문자 불가하고, 특수문자/숫자/영어로 구성되어야 합니다.")
            // 불가비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String pwd;

    @NotEmpty(message = "유저 이름은 필수 입력 값입니다.")
    private String userName;

    @NotEmpty(message = "유저 타입은 필수 입력 값입니다.")
    private String userType;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Members toEntity(){
        return Members.builder()
                .id(id)
                .userId(userId)
                .pwd(pwd)
                .userName(userName)
                .userType(userType)
                .build();
    }

}