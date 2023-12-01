package kr.co.consulting.MyRESTfulService.bean;


import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor //기본 생성자
@JsonFilter("UserInfoV2")
public class AdminUserV2 extends AdminUser{

    private String grade;

}
