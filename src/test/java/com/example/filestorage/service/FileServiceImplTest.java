package com.example.filestorage.service;

import com.example.filestorage.entity.Record;
import com.example.filestorage.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class FileServiceImplTest {

    @MockBean
    private FileRepository fileRepository;

    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileServiceImpl(fileRepository);
    }

    @Test
    void uploadFile() {
        var record = "aaa,name,description,2020-09-20 14:54:00.121";

        var file = new MockMultipartFile("file", "", "text/plain", record.getBytes());
        var expectRecord = Record.builder()
                .key("aaa")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.121"))
                .build();

        Mockito.when(fileRepository.save(expectRecord)).thenReturn(expectRecord);
        Mockito.when(fileRepository.findById(1L)).thenReturn(Optional.of(expectRecord));

        fileService.uploadFile(file);

        var actualRecord = fileService.getRecord("1");
        assertTrue(actualRecord.isPresent());
        assertEquals(expectRecord, actualRecord.get());
    }

    @Test
    void getRecord() {
        var expectRecord = Record.builder()
                .key("aaa")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.121"))
                .build();

        Mockito.when(fileRepository.save(expectRecord)).thenReturn(expectRecord);
        Mockito.when(fileRepository.findById(1L)).thenReturn(Optional.of(expectRecord));

        var actualRecord = fileService.getRecord("1");

        assertTrue(actualRecord.isPresent());
        assertEquals(expectRecord, actualRecord.get());
    }

    @Test
    void getRecordsByTimeStampBetweenFromDateAndToDate() {
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

        var page = new PageImpl<>(List.of(expectRecordOne, expectRecordTwo));

        Mockito.when(fileRepository.save(expectRecordOne)).thenReturn(expectRecordOne);
        Mockito.when(fileRepository.save(expectRecordTwo)).thenReturn(expectRecordTwo);
        Mockito.when(fileRepository.findAllRecordsByUpdateTimeStampBetween(Timestamp.valueOf(timeFrom),
                Timestamp.valueOf(timeTo), PageRequest.of(0, 10))).thenReturn(page);

        var actualRecords = fileService.getRecordsByTimeStampBetweenTimeFromAndTimeTo(timeFrom, timeTo, 0, 10);

        var expectTotal = 2L;
        var actualTotal = actualRecords.getTotalElements();
        assertEquals(expectTotal, actualTotal);

        var expectRecords = List.of(expectRecordOne, expectRecordTwo);
        assertEquals(expectRecords, actualRecords.getContent());
    }

    @Test
    void removeRecord() {
        var record = Record.builder()
                .key("aaa")
                .name("name")
                .description("description")
                .updatedTimeStamp(Timestamp.valueOf("2020-09-20 14:54:00.121"))
                .build();

        Mockito.when(fileRepository.save(record)).thenReturn(record);
        Mockito.when(fileRepository.findById(1L)).thenReturn(Optional.empty());

        fileService.removeRecord("1");
        assertTrue(fileService.getRecord("1").isEmpty());
    }
}