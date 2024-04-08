package com.mjc.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Author extends Model {
    private String name;

    public Author() {

    }

    public Author(String name) {
        this.setId(0L);
        this.name = name;
    }

    @JsonCreator()
    public Author(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name
    ) {
        this.setId(id);
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Author)) {
            return false;
        }

        Author a = (Author) obj;
        return Objects.equals(this.getId(), a.getId())
                && Objects.equals(this.getName(), a.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), name);
    }

    @Override
    public Object clone() {
        return new Author(this.getId(), this.getName());
    }
}
