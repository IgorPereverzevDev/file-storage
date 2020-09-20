package com.example.filestorage.repository;

import com.example.filestorage.entity.Record;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FileRepositoryTest {

    @Autowired
    FileRepository fileRepository;

    @Test
    void findAllRecordsByUpdateTimeStampBetween() {
        var timeFrom = "2020-09-20 14:54:00.121";
        var timeTo = "2020-09-20 14:54:00.122";

        var expectRecordOne = Record.builder()
                .key("aaa")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.121"))
                .build();
        var expectRecordTwo = Record.builder()
                .key("bbb")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.122"))
                .build();

        fileRepository.save(expectRecordOne);
        fileRepository.save(expectRecordTwo);

        var actualRecords = fileRepository.findAllRecordsByUpdateTimeStampBetween(Timestamp.valueOf(timeFrom),
                Timestamp.valueOf(timeTo), PageRequest.of(0, 10));

        assertFalse(actualRecords.isEmpty());

        var expectRecords = List.of(expectRecordOne, expectRecordTwo);
        assertEquals(expectRecords, actualRecords.getContent());
    }


    @Test
    void deleteByKey() {
        var expectRecord = Record.builder()
                .key("aaa")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.121"))
                .build();

        fileRepository.save(expectRecord);
        assertFalse(fileRepository.findByKey("aaa").isEmpty());

        fileRepository.deleteByKey("aaa");
        assertTrue(fileRepository.findByKey("aaa").isEmpty());

    }

    @Test
    void findByKey() {
        var expectRecord = Record.builder()
                .key("aaa")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.121"))
                .build();

        fileRepository.save(expectRecord);
        assertFalse(fileRepository.findByKey("aaa").isEmpty());
    }
}