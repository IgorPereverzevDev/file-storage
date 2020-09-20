package com.example.filestorage.service;

import com.example.filestorage.entity.Record;
import com.example.filestorage.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        String record = "1,name,description,2020-09-20 14:54:00.121";

        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", record.getBytes());
        Record expectRecord = new Record(1L, "name", "description", Timestamp.valueOf("2020-09-20 14:54:00.121"));

        Mockito.when(fileRepository.save(expectRecord)).thenReturn(expectRecord);
        Mockito.when(fileRepository.findById(1L)).thenReturn(Optional.of(expectRecord));

        fileService.uploadFile(file);

        Optional<Record> actualRecord = fileService.getRecord("1");
        assertTrue(actualRecord.isPresent());
        assertEquals(expectRecord, actualRecord.get());
    }

    @Test
    void getRecord() {
        Record expectRecord = new Record(1L, "name", "description", Timestamp.valueOf("2020-09-20 14:54:00.121"));

        Mockito.when(fileRepository.save(expectRecord)).thenReturn(expectRecord);
        Mockito.when(fileRepository.findById(1L)).thenReturn(Optional.of(expectRecord));

        Optional<Record> actualRecord = fileService.getRecord("1");

        assertTrue(actualRecord.isPresent());
        assertEquals(expectRecord, actualRecord.get());
    }

    @Test
    void getRecordsByTimeStampBetweenFromDateAndToDate() {
        String timeFrom = "2020-09-20 14:54:00.121";
        String timeTo = "2020-09-20 14:54:00.122";

        Record expectRecordOne = new Record(1L, "name", "description", Timestamp.valueOf(timeFrom));
        Record expectRecordTwo = new Record(2L, "name", "description", Timestamp.valueOf(timeTo));

        Page<Record> page = new PageImpl<>(List.of(expectRecordOne, expectRecordTwo));

        Mockito.when(fileRepository.save(expectRecordOne)).thenReturn(expectRecordOne);
        Mockito.when(fileRepository.save(expectRecordTwo)).thenReturn(expectRecordTwo);
        Mockito.when(fileRepository.findAllRecordsByUpdateTimeStampBetween(Timestamp.valueOf(timeFrom),
                Timestamp.valueOf(timeTo), PageRequest.of(0, 10))).thenReturn(page);

        Page<Record> actualRecords = fileService.getRecordsByTimeStampBetweenTimeFromAndTimeTo(timeFrom, timeTo, 0, 10);

        Long expectTotal = 2L;
        Long actualTotal = actualRecords.getTotalElements();
        assertEquals(expectTotal, actualTotal);

        List<Record> expectRecords = List.of(expectRecordOne, expectRecordTwo);
        assertEquals(expectRecords, actualRecords.getContent());
    }

    @Test
    void removeRecord() {
        Record record = new Record(1L, "name", "description", Timestamp.valueOf("2020-09-20 14:54:00.121"));

        Mockito.when(fileRepository.save(record)).thenReturn(record);
        Mockito.when(fileRepository.findById(1L)).thenReturn(Optional.empty());

        fileService.removeRecord("1");
        assertTrue(fileService.getRecord("1").isEmpty());
    }
}