package kr.co.consulting.MyRESTfulService.Controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.validation.Valid;
import kr.co.consulting.MyRESTfulService.bean.AdminUser;
import kr.co.consulting.MyRESTfulService.bean.User;
import kr.co.consulting.MyRESTfulService.dao.UserDaoService;
import kr.co.consulting.MyRESTfulService.exception.UserNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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


}