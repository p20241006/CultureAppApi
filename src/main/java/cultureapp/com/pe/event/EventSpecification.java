package cultureapp.com.pe.event;

import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {

    public static Specification<Event> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
