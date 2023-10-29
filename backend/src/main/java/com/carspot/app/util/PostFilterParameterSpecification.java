package com.carspot.app.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PostFilterParameterSpecification implements Specification {


    private final PostFilterParameters params;

    public PostFilterParameterSpecification(PostFilterParameters params) {
        this.params = params;
    }

    // Root is the model class (Post)
    // CriteriaQuery
    // CriteriaBuilder is used to construct criteria queries, compound selections, expressions, predicates, orderings (and, or, between, like, equals etc)
    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // min/max is never not set as they have default values
        predicates.add(builder.between(root.get("price"), params.getMinValue(), params.getMaxValue()));


        if (params.getProvince() != null) {
            predicates.add(builder.equal(root.get("province"), params.getProvince()));
        }


        if (!CollectionUtils.isEmpty(params.getCity())) {
            Expression<String> userExpression = root.get("city");
            Predicate p = userExpression.in(params.getCity());
            predicates.add(p);
        }

        if (!CollectionUtils.isEmpty(params.getBrand())) {
            Expression<String> userExpression = root.get("brand");
            Predicate p = userExpression.in(params.getBrand());
            predicates.add(p);
        }

        // only get active posts
        predicates.add(builder.isTrue(root.get("active")));
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}