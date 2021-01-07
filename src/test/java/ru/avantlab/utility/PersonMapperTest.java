package ru.avantlab.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.avantlab.model.Person;
import ru.avantlab.model.PersonDto;

@SpringJUnitConfig(PersonMapperImpl.class)
public class PersonMapperTest {
    private final PersonMapper mapper;

    private final long id = 1;
    private final String firstName = "Ivan";
    private final String lastName = "Ivanov";
    private final String email = "ivan@mail.ru";
    private final String phoneNumber = "+7-927-1888927";

    @Autowired
    public PersonMapperTest(PersonMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void PersonToPersonDtoTest() {
        PersonDto expected = new PersonDto();
        expected.setId(id);
        expected.setFirstName(firstName);
        expected.setLastName(lastName);
        expected.setEmail(email);
        expected.setPhoneNumber(phoneNumber);

        Person person = new Person();
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);

        Assertions.assertEquals(expected, mapper.personToPersonDto(person));
    }

    @Test
    public void PersonDtoToPersonTest() {
        Person expected = new Person();
        expected.setId(id);
        expected.setFirstName(firstName);
        expected.setLastName(lastName);
        expected.setEmail(email);
        expected.setPhoneNumber(phoneNumber);

        PersonDto personDto = new PersonDto();
        personDto.setId(id);
        personDto.setFirstName(firstName);
        personDto.setLastName(lastName);
        personDto.setEmail(email);
        personDto.setPhoneNumber(phoneNumber);

        Assertions.assertEquals(expected, mapper.personDtoToPerson(personDto));
    }
}
