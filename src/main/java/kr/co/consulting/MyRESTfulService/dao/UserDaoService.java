package kr.co.consulting.MyRESTfulService.dao;

import kr.co.consulting.MyRESTfulService.bean.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<>();         //저장소로 사용 될 List , 임시 메모리에 저장

    //전체 사용자의 인원수를 저장하기 위한 변수
    private static int usersCount = 3;

    //초기 데이터 값 저장
    static{
        users.add(new User(1,"Kenneth", new Date()));
        users.add(new User(2,"Alice", new Date()));
        users.add(new User(3,"Elena", new Date()));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if (user.getId() == null){      //만약 유저 데이터 안에 ID 값이 들어가 있지 않다면
            user.setId(++usersCount);   //유저의 setId 값에 usersCount 값을 하나 증가해서, 4번쩨 데이터가 되도록
        }

        if (user.getJoinDate() == null){
            user.setJoinDate(new Date());
        }

        users.add(user);

        return user;
    }

    public User findOne(int id){
        for (User user : users) {
            if (user.getId() == id){
                return user;
            }
        }

        return null;
    }
    /*
   이 강의의 현재 단계에선 데이터베이스를 사용하지 않는다. 그래서 select문을 사용 할 수 없다.
   순차적으로 되어있는 컬렉션의 값을 , iterator 데이터 값으로 변경한 다음
   하나씩 검색 할 수 있도록 만들어보자.
   그래서, user의 Id가 일치하면 iterator.remove로 제거하는 기능
     */


    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()){
            User user = iterator.next();

            if (user.getId()==id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }


}
