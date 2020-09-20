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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_generator")
    @SequenceGenerator(name="record_generator", sequenceName = "record_seq")
    private Long id;
    private String name;
    private String description;
    private Timestamp updatedTimeStamp;
}
