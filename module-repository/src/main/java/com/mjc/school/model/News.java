package com.mjc.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class News extends Model {
    private String title;
    private String content;

    @Setter(AccessLevel.NONE)
    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;
    private Long authorId;

    public News() {
        this.createDate = LocalDateTime.now();
    }

    @JsonCreator()
    public News(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("content") String content,
            @JsonProperty("createDate") @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss") LocalDateTime createDate,
            @JsonProperty("lastUpdateDate") @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss") LocalDateTime lastUpdateDate,
            @JsonProperty("authorId") Long authorId
    ) {
        this.setId(id);
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.authorId = getAuthorId();
    }
}
