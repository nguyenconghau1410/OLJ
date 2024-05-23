package com.example.oj.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Data
public class NotificationsDocument {
    @Id
    private String id;
    private String email;
    private String title;
    private String description;

}
