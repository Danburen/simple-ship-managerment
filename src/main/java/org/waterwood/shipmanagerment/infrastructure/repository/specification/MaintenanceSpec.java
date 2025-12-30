package org.waterwood.shipmanagerment.infrastructure.repository.specification;

import jakarta.persistence.criteria.Join;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.entity.Maintenance;

import jakarta.persistence.criteria.Predicate;
import org.waterwood.shipmanagerment.entity.Ship;
import org.waterwood.shipmanagerment.infrastructure.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class MaintenanceSpec {
    public static Specification<Maintenance> of(Long shipId, String shipName, String description, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(shipId != null){
                predicates.add(criteriaBuilder.equal(root.get("ship").get("id"), shipId));
            }

            if(StringUtil.isNotBlank(shipName)){
                Join<Maintenance, Ship> ship = root.join("ship");
                predicates.add(criteriaBuilder.like(ship.get("name"), "%" + shipName + "%"));
            }
            if(StringUtil.isNotBlank(description)){
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
            }
            if(StringUtil.isNotBlank(status)){
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}