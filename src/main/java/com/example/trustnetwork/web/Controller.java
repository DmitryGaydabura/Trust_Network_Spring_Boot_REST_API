package com.example.trustnetwork.web;

import com.example.trustnetwork.domain.Message;
import com.example.trustnetwork.domain.Person;
import com.example.trustnetwork.dto.PersonPostDTO;
import com.example.trustnetwork.service.Service;
import com.example.trustnetwork.util.config.mapstruct.PersonToDTOMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class Controller {

    private final Service service;
    private final PersonToDTOMapper mapper;

    /**
     * The function takes a Person object as a parameter, saves it to the database, and returns a PersonPostDTO object
     *
     * @param person The person object that will be saved.
     * @return A PersonPostDTO object
     */
    @PostMapping(value = "/people")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonPostDTO savePerson(@RequestBody Person person) {
        return mapper.personToPostDTO(service.create(person));
    }

    /**
     * This function takes in a name and a list of relations, and returns a map of the number of people that the person
     * trusts and the number of people that trust the person
     *
     * @param name The name of the person whose trust connections you want to update.
     * @param relations A comma separated list of people that the person trusts.
     * @return A HashMap with the name of the person and the number of trust connections.
     */
    @PostMapping(value = "/people/{name}/trust_connections")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,Integer> updateRelations(@PathVariable("name") String name, @RequestParam("relations") String relations) {
        return service.updateRelations(name, relations);
    }

    /**
     * The function takes a message object as a parameter, and returns a set of receivers
     *
     * @param message The message to be sent.
     * @return A HashSet of Strings
     */
    @PostMapping(value = "/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public HashSet<String > sendMessage(@RequestBody Message message) {
        return service.sendMessage(message);
    }


    /**
     * This function takes in a message object, and returns a set of receivers
     *
     * @param message the message to be sent
     * @return A HashSet of Strings
     */
    @PostMapping(value = "/path")
    @ResponseStatus(HttpStatus.CREATED)
    public HashSet<String > sendMessageBonus(@RequestBody Message message) {
        return service.sendMessageBonus(message);
    }
}
