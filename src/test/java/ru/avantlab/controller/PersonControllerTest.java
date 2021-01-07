package ru.avantlab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.avantlab.model.PersonDto;
import ru.avantlab.repository.specification.PersonFilter;
import ru.avantlab.service.PersonService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)

public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService service;

    private final long id = 1;
    private final String firstName = "Ivan";
    private final String lastName = "Ivanov";
    private final String email = "ivan@mail.ru";
    private final String phoneNumber = "+7-927-1888927";

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/persons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":[],\"pageable\":\"pageNumber\":0,\"pageSize\":20,"))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).findAll(PageRequest.of(0, 20));
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/persons/1"))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).findById(id);
    }

    @Test
    public void add() throws Exception {
        PersonDto personDto = new PersonDto();

        personDto.setFirstName(firstName);
        personDto.setLastName(lastName);
        personDto.setEmail(email);
        personDto.setPhoneNumber(phoneNumber);

        Mockito.when(service.save(personDto)).thenReturn(personDto);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(personDto);

        mockMvc.perform(post("/persons/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isCreated());

        Mockito.verify(service, Mockito.times(1)).save(personDto);
    }

    @Test
    public void update() throws Exception {
        PersonDto personDto = new PersonDto();

        personDto.setId(id);
        personDto.setFirstName(firstName);
        personDto.setLastName(lastName);
        personDto.setEmail(email);
        personDto.setPhoneNumber(phoneNumber);

        Mockito.when(service.save(personDto)).thenReturn(personDto);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(personDto);

        mockMvc.perform(put("/persons/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).update(any());
    }

    @Test
    public void deletePerson() throws Exception {
        mockMvc.perform(delete("/persons/delete/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).delete(new PersonDto(id));
    }

    @Test
    public void getFilteredPerson() throws Exception {
        PersonFilter personFilter = new PersonFilter();
        personFilter.setFirstName(firstName);

        Pageable pageable = PageRequest.of(0, 20);

        ObjectMapper mapper = new ObjectMapper();

        String pageableJson = mapper.writeValueAsString(pageable);

        String personFilterJson = mapper.writeValueAsString(personFilter);

        mockMvc.perform(get("/persons/filtered")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pageableJson)
                .content(personFilterJson))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).getFilteredPersons(personFilter, pageable);
    }
}
