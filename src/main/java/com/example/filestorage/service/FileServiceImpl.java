package com.example.filestorage.service;

import com.example.filestorage.constant.FileConstant;
import com.example.filestorage.entity.Record;
import com.example.filestorage.repository.FileRepository;
import com.example.filestorage.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void uploadFile(MultipartFile file) {
        retrieveRecords(file).forEach(fileRepository::save);
    }

    private List<Record> retrieveRecords(MultipartFile file) {
        List<Record> records = new ArrayList<>();
        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null && Validator.isValidLine(line)) {
                var data = line.split(FileConstant.SEPARATOR);
                if (Validator.isValid(data)) {
                    var record = Record.builder()
                            .key(data[0])
                            .name(data[1])
                            .description(data[2])
                            .updatedTimeStamp(Timestamp.valueOf(data[3]))
                            .build();
                    records.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Invalid data format");
        }
        return records;
    }


    @Override
    public Optional<Record> getRecord(String key) {
        return fileRepository.findByKey(key);
    }

    @Override
    public Page<Record> getRecordsByTimeStampBetweenTimeFromAndTimeTo(String TimeFrom, String TimeTo, int page, int size) {
        return fileRepository.findAllRecordsByUpdateTimeStampBetween(
                Timestamp.valueOf(TimeFrom), Timestamp.valueOf(TimeTo), PageRequest.of(page, size));
    }

    @Override
    public void removeRecord(String key) {
        fileRepository.deleteByKey(key);
    }

}
