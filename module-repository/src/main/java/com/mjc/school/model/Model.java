package com.mjc.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Model implements Cloneable {
    private Long id;

    public Model() {}

    @JsonCreator()
    public Model(
            @JsonProperty("id") Long id
    ) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
