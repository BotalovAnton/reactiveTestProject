package ru.avantlab.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.avantlab.model.PersonDto;
import ru.avantlab.repository.specification.PersonFilter;

public interface PersonService {
    PersonDto save(PersonDto personDto);

    PersonDto findById(long id);

    Page<PersonDto> findAll(Pageable pageable);

    void update(PersonDto personDto);

    void delete(PersonDto personDto);

    Page<PersonDto> getFilteredPersons(PersonFilter filter, Pageable pageable);
}
