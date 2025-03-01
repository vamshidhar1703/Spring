package com.project.journalApp.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;
    @NonNull @Indexed(unique = true)
    private String userName;
    private String email;
    private boolean sentimentAnalysis;
    @NonNull
    private String password;
    private List<String> roles;

    @DBRef
    private List<JournalEntry> journalEntries=new ArrayList<>();
    
}
