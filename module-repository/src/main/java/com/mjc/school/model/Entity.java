package com.mjc.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity implements Cloneable {
    private Long id;

    protected Entity() {}

    @JsonCreator()
    protected Entity(
            @JsonProperty("id") Long id
    ) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
