package com.example.filestorage.service;

import com.example.filestorage.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {

    void uploadFile(MultipartFile file);

    Optional<Record> getRecord(String key);

    Page<Record> getRecordsByTimeStampBetweenTimeFromAndTimeTo(String TimeFrom, String TimeTo, int page, int size);

    void removeRecord(String key);

}
