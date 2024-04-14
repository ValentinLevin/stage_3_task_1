package com.mjc.school.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
public class News extends Entity {
    @NotEmpty(message = "News title is required")
    @Size(min = 5, message = "The title of the news must be at least 5 characters")
    @Size(max = 30, message = "The title of the news must be no more than 30 characters")
    private String title;

    @NotEmpty(message = "News content is required")
    @Size(min = 5, message = "The content of the news must be at least 5 characters")
    @Size(max = 30, message = "The content of the news must be no more than 255 characters")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @NotNull(message = "Author id is required")
    private Long authorId;

    public News() { }

    @Override
    public Object clone() {
        return new News(
                this.getId(),
                this.getTitle(),
                this.getContent(),
                this.getCreateDate(),
                this.getLastUpdateDate(),
                this.getAuthorId()
        );
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
        this.authorId = authorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getId(),
                title,
                content,
                createDate,
                lastUpdateDate,
                authorId
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof News)) {
            return false;
        }

        News n = (News) obj;

        return Objects.equals(this.getId(), n.getId())
                && Objects.equals(this.getTitle(), n.getTitle())
                && Objects.equals(this.getContent(), n.getContent())
                && Objects.equals(this.getCreateDate(), n.getCreateDate())
                && Objects.equals(this.getLastUpdateDate(), n.getLastUpdateDate())
                && Objects.equals(this.getAuthorId(), n.getAuthorId());
    }
}
