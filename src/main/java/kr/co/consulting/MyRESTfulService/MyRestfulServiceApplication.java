package kr.co.consulting.MyRESTfulService;

import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

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

	@Bean
	public LocaleResolver localResolver(){
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		//일단 기본 값을 영어로 설정
		//Resource에 담긴 messages.properties에 따라 다른 언어가 나옴
		//fr.properties 는 불어, kr. 는 한국어, jp. 는 일본어

		return localeResolver;
	}

}
