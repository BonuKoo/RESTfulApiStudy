package kr.co.consulting.MyRESTfulService.Controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.validation.Valid;
import kr.co.consulting.MyRESTfulService.bean.AdminUser;
import kr.co.consulting.MyRESTfulService.bean.AdminUserV2;
import kr.co.consulting.MyRESTfulService.bean.User;
import kr.co.consulting.MyRESTfulService.dao.UserDaoService;
import kr.co.consulting.MyRESTfulService.exception.UserNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin") //AdminUserController에서 호출되는 모든 이름은 제일 앞에 Admin으로 시작하게 된다.
public class AdminUserController {

    private UserDaoService service;

    private AdminUserController(UserDaoService service) {
        this.service = service;
    }


    // /admin/users/{id}
    // 모든 엔드포인트에 'admin' 이라는 prefix가 존재한다.

    //매핑은 "/users/{id}"지만, 위에 걸어둔 리퀘스트 매핑 prefix 때문에,
    // 실 사용할 땐 /admin/users/{}
    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser4Admin(@PathVariable int id) {

        User user = service.findOne(id);

        AdminUser adminUser = new AdminUser();

        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        } else {
            //예를 들어서, adminUser에 프로퍼티에 해당하는 값을 모두 반환하는 짓을 반복해야 함.
            //프로퍼티가 한두개일 경우 괜찮지만 수백 수천건이면 개노답
            //adminUser.setName(user.getName());

            //user : source, adminUser : target.   user에서 adminUser로 복사
            //37번째 줄 User user 객체의 값이 39줄 adminUser로 복사 될 것이다.
            BeanUtils.copyProperties(user,adminUser);
        }
        //Json필터 적용
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");

        //필터와 갖고 있는 유저 정보를 적용
        //위에서 만든 filter에, 스프링 컨텍스트에 등록되어있는 필터 이름 중 UserInfo 라는 필터를 적용
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

       MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
       mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers4Admin() {

        List<User> users = service.findAll();

        List<AdminUser> adminUsers = new ArrayList<>();
        AdminUser adminUser = null;

        //유저 데이터에 포함되어 있는 형태를
        //AdminUser로 바꿀 것
        for (User user : users) {
            adminUser = new AdminUser();
            BeanUtils.copyProperties(user,adminUser);

            //복사 작업이 잘 되었을 때, adminUsers에 값을 저장
            adminUsers.add(adminUser);
        }

        SimpleBeanPropertyFilter filter
                = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");


        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUsers);
        mapping.setFilters(filters);

        return mapping;
    }


//    @GetMapping("/v1/users/{id}")
//    @GetMapping(value = "/users/{id}",params = "version=1")
//    @GetMapping(value = "/users/{id}",headers = "X-API-VERSION=1")  //소문자->대문자 ctrl+shift+u
@GetMapping(value = "/users/{id}",produces = "application/vnd.company.appv1+json")

public MappingJacksonValue retrieveUserV14Admin(@PathVariable int id) {

        User user = service.findOne(id);

        AdminUser adminUser = new AdminUser();

        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        } else {

            BeanUtils.copyProperties(user,adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");


        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);

        return mapping;
    }

    //@GetMapping("/v2/users/{id}")
    //@GetMapping(value = "/users/{id}",params = "version=2")
    //@GetMapping(value = "/users/{id}",headers = "X-API-VERSION=2")
    @GetMapping(value = "/users/{id}",produces = "application/vnd.company.appv2.json")

    public MappingJacksonValue retrieveUserV24Admin(@PathVariable int id) {

        User user = service.findOne(id);

        AdminUserV2 adminUser = new AdminUserV2();

        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        } else {

            BeanUtils.copyProperties(user,adminUser);
            adminUser.setGrade("VIP");  //기존 User에 Grade가 생김
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","grade");

                                                                    //AdminUserV2 필터링
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);

        return mapping;
    }



}