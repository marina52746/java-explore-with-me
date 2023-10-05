package ru.practicum.user;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {
    /*
    public UserSpecification(UserParam param) {
        StringBuilder stringBuilder = new StringBuilder("select * from where 1=1");
        if (param.getParam1() != null) {
            // ...
            stringBuilder.append(" and param1 = :param1");
        }
        HashMap<String, Object> paramValues = new HashMap<>();
        if (param.getParam1() != null) {
            paramValues.put("param1", param.getParam1());
        }
        String sql = stringBuilder.toString();
        //лучше - CriteriaApi
    }
    */
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }

}
