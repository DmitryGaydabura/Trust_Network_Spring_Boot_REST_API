package com.example.trustnetwork.util.config.mapstruct;

import com.example.trustnetwork.domain.Person;
import com.example.trustnetwork.dto.PersonPostDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonToDTOMapper {
    /**
     * Convert a Person to a PersonPostDTO.
     *
     * @param person The person object to be converted.
     * @return A PersonPostDTO object.
     */
    PersonPostDTO personToPostDTO(Person person);
}
