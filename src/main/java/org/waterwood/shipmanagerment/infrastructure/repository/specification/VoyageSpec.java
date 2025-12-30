package org.waterwood.shipmanagerment.infrastructure.repository.specification;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.waterwood.shipmanagerment.entity.Voyage;

import jakarta.persistence.criteria.Predicate;
import org.waterwood.shipmanagerment.infrastructure.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class VoyageSpec {
    /**
     * 构建航行记录查询条件
     * @param shipId 船舶ID
     * @param departurePortId 出发港口ID
     * @param arrivalPortId 到达港口ID
     * @return Specification<Voyage>
     */
    public static Specification<Voyage> of(Long shipId, Long departurePortId, Long arrivalPortId, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            if (shipId != null) {
                predicates.add(criteriaBuilder.equal(root.get("ship").get("id"), shipId));
            }
            if (departurePortId != null) {
                predicates.add(criteriaBuilder.equal(root.get("departurePort").get("id"), departurePortId));
            }

            if (StringUtil.isNotBlank(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (arrivalPortId != null) {
                predicates.add(criteriaBuilder.equal(root.get("arrivalPort").get("id"), arrivalPortId));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}