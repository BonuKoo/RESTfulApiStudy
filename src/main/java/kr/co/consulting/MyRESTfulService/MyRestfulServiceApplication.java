package kr.co.consulting.MyRESTfulService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MyRestfulServiceApplication {

	public static void main(String[] args) {

//		ApplicationContext ac =
				SpringApplication.run(MyRestfulServiceApplication.class, args);

		//Run을 실행했을 때, 반환값으로 ApplicationContext를 불러온다.
		/*ApplicationContext 변수 반환을 통해, 현재 SpringContext에 등록된 Bean들의 목록 확인
		String [] allBeanNames = ac.getBeanDefinitionNames();
		for (String beanName : allBeanNames) {
			System.out.println(beanName);
		}
		 */

	}

}
