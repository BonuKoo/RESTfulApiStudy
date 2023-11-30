package kr.co.consulting.MyRESTfulService.Controller;


import jakarta.validation.Valid;
import kr.co.consulting.MyRESTfulService.exception.UserNotFoundException;
import kr.co.consulting.MyRESTfulService.bean.User;
import kr.co.consulting.MyRESTfulService.dao.UserDaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private UserDaoService service;

    private UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    /*
    유저 Class가 검색되지 않았을 경우 - 해당 id의 주소가 없을 경우
    FoundException을 반환
     */
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {

        User user = service.findOne(id);

        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        }

        return user;

    }

    /* CREATES
        input = details of user
        output = CREATED &  Return the created URI
     */

//    @PostMapping("/users")
//    public void createUser(@RequestBody User user) {
//        User savedUser = service.save(user);
//    }

    @PostMapping("/users")
    public ResponseEntity<User> createResponseEntity(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
        //주소값을 포함시켜서 반환, 이 주소값은
        // 사용자의 ID 값을 가지고 상세보기 할 수 있도록 만들어져 있는 정보다.

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable int id){

        User deleteUser = service.deleteById(id);

        if (deleteUser == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        }

        return ResponseEntity.noContent().build();
    }

}