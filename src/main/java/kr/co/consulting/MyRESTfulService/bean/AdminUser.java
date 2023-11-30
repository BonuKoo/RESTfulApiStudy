package kr.co.consulting.MyRESTfulService.bean;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor //기본 생성자
@JsonFilter("UserInfo")
public class AdminUser {

    /*
    유효성 체크
     */

    private Integer id;

    @Size(min = 2, message = "Name은 2글자 이상 입력해주세요.")
    private String name;

    @Past(message = "등록일은 미래 날짜를 입력하실 수 없습니다.")
    private Date joinDate;

    private String password;

    //주민번호
    private String ssn;

}
