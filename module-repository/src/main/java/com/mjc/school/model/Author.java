package com.mjc.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author extends Model {
    private String name;

    public Author() {

    }

    @JsonCreator()
    public Author(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name
    ) {
        this.setId(id);
        this.name = name;
    }
}
