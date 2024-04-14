package com.mjc.school.mapper;

import com.mjc.school.dto.AuthorDTO;
import com.mjc.school.model.Author;
import org.modelmapper.ModelMapper;

public class AuthorDTOMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Author toAuthor(AuthorDTO authorDTO) {
        return authorDTO == null ? null : modelMapper.map(authorDTO, Author.class);
    }

    public static AuthorDTO fromAuthor(Author author) {
        return author == null ? null : modelMapper.map(author, AuthorDTO.class);
    }
}
