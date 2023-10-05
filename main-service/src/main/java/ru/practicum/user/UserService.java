package ru.practicum.user;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class UserService {
    /*
    UserRepository userRepository;

    public List<User> findBy(UserParam param, Pageable pageable) {
        Specification<User> spec = new UserSpecification(param); // часть для where на java коде
        return userRepository.findBy(spec, pageable);
    }
     */
}
