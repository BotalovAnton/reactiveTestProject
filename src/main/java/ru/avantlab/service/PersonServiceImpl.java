package ru.avantlab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avantlab.model.Person;
import ru.avantlab.model.PersonDto;
import ru.avantlab.repository.PersonRepository;
import ru.avantlab.repository.specification.PersonFilter;
import ru.avantlab.repository.specification.PersonSpecification;
import ru.avantlab.service.exception.EntityNotFoundException;
import ru.avantlab.utility.PersonMapper;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
    private final PersonRepository repository;
    private final PersonMapper mapper;
    private final PersonSpecification personSpecification;

    @Autowired
    public PersonServiceImpl(PersonRepository repository, PersonMapper mapper, PersonSpecification specification) {
        this.repository = repository;
        this.mapper = mapper;
        this.personSpecification = specification;
    }

    @Override
    public PersonDto save(PersonDto personDto) {
        Person person = mapper.personDtoToPerson(personDto);

        person =repository.save(person);

        return mapper.personToPersonDto(person);
    }

    @Override
    public PersonDto findById(long id) {
        Person person;

        person = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person with id %s not found", id)));

        return mapper.personToPersonDto(person);
    }

    @Override
    public Page<PersonDto> findAll(Pageable pageable) {
        Page<Person> page = repository.findAll(pageable);

        return new PageImpl<>(mapper.personToPersonDto(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public void update(PersonDto personDto) {
        repository.save(mapper.personDtoToPerson(personDto));
    }

    @Override
    public void delete(PersonDto personDto) {
        repository.delete(new Person(personDto.getId()));
    }

    @Override
    public Page<PersonDto> getFilteredPersons(PersonFilter filter, Pageable pageable) {
        Specification<Person> specification = personSpecification.filterPerson(filter);

        Page<Person> page = repository.findAll(specification, pageable);

        return new PageImpl<>(mapper.personToPersonDto(page.getContent()), pageable, page.getTotalElements());
    }
}
