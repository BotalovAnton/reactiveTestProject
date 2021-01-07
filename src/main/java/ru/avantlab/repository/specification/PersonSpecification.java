package ru.avantlab.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.avantlab.model.Person;

@Component
public class PersonSpecification {

    public Specification<Person> filterPerson(PersonFilter filter) {

        Specification<Person> specification = Specification.where(null);

        if (filter.getId() != 0) {
            specification = specification.and(searchById(filter.getId()));
        }

        if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
            specification = specification.and(searchByFirstName(filter.getFirstName()));
        }

        if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
            specification = specification.and(searchByLastName(filter.getLastName()));
        }

        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            specification = specification.and(searchByEmail(filter.getEmail()));
        }

        if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isEmpty()) {
            specification = specification.and(searchByPhoneNumber(filter.getPhoneNumber()));
        }

        return specification;
    }

    private Specification<Person> searchById(long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    private Specification<Person> searchByFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName);
    }

    private Specification<Person> searchByLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastName"), lastName);
    }

    private Specification<Person> searchByEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    private Specification<Person> searchByPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
    }
}
