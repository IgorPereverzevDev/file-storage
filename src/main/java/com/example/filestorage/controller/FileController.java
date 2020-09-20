package com.example.filestorage.controller;

import com.example.filestorage.entity.Record;
import com.example.filestorage.service.FileService;
import com.example.filestorage.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Empty file", HttpStatus.BAD_REQUEST);
        }
        fileService.uploadFile(file);
        return new ResponseEntity<>("Your file was uploaded successfully", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public ResponseEntity<String> removedRecord(@RequestParam("id") String id) {
        if (!Validator.isValidId(id)) {
            return new ResponseEntity<>("Incorrect id: " + id, HttpStatus.BAD_REQUEST);
        }
        fileService.removeRecord(id);
        return new ResponseEntity<>("Record removed", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public ResponseEntity<?> getRecord(@RequestParam("id") String id) {
        if (!Validator.isValidId(id)) {
            return new ResponseEntity<>("Incorrect id: " + id, HttpStatus.BAD_REQUEST);
        }
        Optional<Record> record = fileService.getRecord(id);
        return record.isPresent() ? new ResponseEntity<>(record.get(), HttpStatus.OK) :
                new ResponseEntity<>("Record not found", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/records", method = RequestMethod.GET)
    public ResponseEntity<?> getRecordsByTimeStampBetweenTimeFromAndTimeTo(@RequestParam(value = "timeFrom") String timeFrom,
                                                                           @RequestParam(value = "timeTo") String timeTo,
                                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        if (!Validator.isValidTimeStamp(timeFrom)) {
            return new ResponseEntity<>("Incorrect timeFrom: " + timeFrom, HttpStatus.BAD_REQUEST);
        } else if (!Validator.isValidTimeStamp(timeTo)) {
            return new ResponseEntity<>("Incorrect timeTo: " + timeTo, HttpStatus.BAD_REQUEST);
        }else if(Timestamp.valueOf(timeFrom).after(Timestamp.valueOf(timeTo))){
            return new ResponseEntity<>("timeFrom must be less than timeTo", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(fileService.getRecordsByTimeStampBetweenTimeFromAndTimeTo(timeFrom, timeTo, page, size), HttpStatus.OK);
    }

}
