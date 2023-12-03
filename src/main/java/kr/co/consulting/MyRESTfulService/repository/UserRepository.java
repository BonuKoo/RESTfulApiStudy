package kr.co.consulting.MyRESTfulService.repository;

import kr.co.consulting.MyRESTfulService.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
