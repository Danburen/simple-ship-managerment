package org.waterwood.shipmanagerment.infrastructure.repository.specification;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.entity.Port;

import jakarta.persistence.criteria.Predicate;
import org.waterwood.shipmanagerment.infrastructure.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class PortSpec {
    public static Specification<Port> of(String name, String location) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(StringUtil.isNotBlank(name)){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if(StringUtil.isNotBlank(location)){
                predicates.add(criteriaBuilder.like(root.get("location"), "%" + location + "%"));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}