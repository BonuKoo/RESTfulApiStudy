package kr.co.consulting.MyRESTfulService.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.consulting.MyRESTfulService.exception.UserNotFoundException;
import kr.co.consulting.MyRESTfulService.bean.User;
import kr.co.consulting.MyRESTfulService.dao.UserDaoService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러")
public class UserController {

    private UserDaoService service;

    private UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }


    @Operation(summary = "사용자 정보 조회 API", description = "사용자 ID를 이용해서 사용자 상세 정보 조회를 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!, 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST, 입력파라미터 잘못 되었다."),
            @ApiResponse(responseCode = "404", description = "USER NOT FOUND, 자료 없다"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR , 서버 오류")
    })
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@Parameter(description = "사용자 ID", required = true,example = "1") @PathVariable int id) {

        User user = service.findOne(id);

        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        //HATEOAS
        EntityModel<User> entityModel = EntityModel.of(user);
        //WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        //this.getClass를 통해 현재 클래스의 Class 객체를 가져오고,
        //그 후에 methodOn을 통해 매서드에 대한 참조를 생성하는데
        //이 과정에서 CGlib이 프록시 객체를 생성하는 데 문제가 발생했을 가능성이 있다고 한다.

        // 해결 방법 : 메서드 참조 변경
        //현재 클래스의 Class 객체를 가져오는 것 대신, 직접 클래스를 지정하여 메소드 참조를 생성한다.

        //WebMvcLinkBuilder linkTo = linkTo(UserController.class, UserController::retrieveAllUsers);
        WebMvcLinkBuilder linkTo = linkTo(UserController.class).slash("users");

        entityModel.add(linkTo.withRel("all-users"));

        return entityModel;

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