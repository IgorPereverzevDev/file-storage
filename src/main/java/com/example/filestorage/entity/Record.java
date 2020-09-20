package com.example.filestorage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Builder
@Getter
public class Record {

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String key;

    private String name;
    private String description;
    private Timestamp updatedTimeStamp;

    @Tolerate
    public Record() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;
        var record = (Record) o;
        return Objects.equals(getKey(), record.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

}
