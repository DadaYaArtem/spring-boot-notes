package com.groupone.notes;

import com.groupone.users.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Notes {
    @Id
    @GeneratedValue
    private UUID id;

    private String nameNotes;

    private String content;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne
    private UserEntity user;

    public Notes() {
    }

    public Notes(UUID id, String nameNotes, String content, Visibility visibility) {
        this.id = id;
        this.nameNotes = nameNotes;
        this.content = content;
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", nameNotes='" + nameNotes + '\'' +
                ", content='" + content + '\'' +
                ", visibility=" + visibility +
                '}';
    }
}
