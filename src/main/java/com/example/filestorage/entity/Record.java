package com.example.filestorage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @Id
    private Long id;
    private String name;
    private String description;
    private Timestamp updatedTimeStamp;
}
