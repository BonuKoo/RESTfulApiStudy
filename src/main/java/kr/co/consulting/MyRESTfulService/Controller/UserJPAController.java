package kr.co.consulting.MyRESTfulService.Controller;

import jakarta.validation.Valid;
import kr.co.consulting.MyRESTfulService.bean.Post;
import kr.co.consulting.MyRESTfulService.bean.User;
import kr.co.consulting.MyRESTfulService.exception.UserNotFoundException;
import kr.co.consulting.MyRESTfulService.repository.PostRepository;
import kr.co.consulting.MyRESTfulService.repository.UserRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/jpa")
public class UserJPAController {

    private UserRepository userRepository;

    private PostRepository postRepository;

    public UserJPAController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    //  /jpa/users/{id}
    @GetMapping("/users/{id}")
    public ResponseEntity retrieveUserById(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()){
            throw new UserNotFoundException("id-" + id);
        }

        EntityModel entityModel = EntityModel.of(user.get());


        WebMvcLinkBuilder linkTo = linkTo(UserController.class).slash("users");

        entityModel.add(linkTo.withRel("all-users"));


        return ResponseEntity.ok(entityModel);
    }

    //jpa/users
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
        //주소값을 포함시켜서 반환, 이 주소값은
        // 사용자의 ID 값을 가지고 상세보기 할 수 있도록 만들어져 있는 정보다.
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new UserNotFoundException("id-" + id);
        }

        return user.get().getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        User user = userOptional.get();

        post.setUser(user);

        postRepository.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

}
