package kr.co.consulting.MyRESTfulService.Controller;

import kr.co.consulting.MyRESTfulService.bean.HelloWorldBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    //GET
    //URI - /hello-world
    //@RequestMapping(method=RequestMethod.GET, path="/hello-world")
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    //위와는 다르게 반환 값을 Object 타입, Bean 타입으로 전달하게 된다.
    //Bean 타입으로 전달하게 되면, 스프링부트에서 저절로 Response Body로 변환시켜주기 때문에
    //JSON 형태로 값을 반환하게 된다.

    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean(("Hello World!"));
    }

    //가변 변수를 적용하기 위한 PathVariable
    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBeanPathVariable(@PathVariable String name){
        return new HelloWorldBean(String.format("Hello World!, %s",name));
    }

}
