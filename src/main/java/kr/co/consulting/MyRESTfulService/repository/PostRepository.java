package kr.co.consulting.MyRESTfulService.repository;

import kr.co.consulting.MyRESTfulService.bean.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
