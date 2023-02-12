package com.groupone.notes;

import com.groupone.users.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    private Users users;

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
