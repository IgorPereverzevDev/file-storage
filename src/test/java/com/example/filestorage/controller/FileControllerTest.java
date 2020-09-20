package com.example.filestorage.controller;

import com.example.filestorage.entity.Record;
import com.example.filestorage.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;


    @Test
    void uploadFile() throws Exception {
        var response = "Your file was uploaded successfully";
        var data = "abc";
        var file = new MockMultipartFile("file", "", "text/plain", data.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().is(202))
                .andExpect(content().string(response));
    }

    @Test
    void uploadFileWhenFileIsEmpty() throws Exception {
        var response = "Empty file";
        var file = new MockMultipartFile("file", "", "text/plain", "".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void removedRecord() throws Exception {
        var key = "1";
        var response = "Record removed";
        mockMvc.perform(MockMvcRequestBuilders.delete("/remove")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("key", key))
                .andExpect(status().is(202))
                .andExpect(content().string(response));
    }


    @Test
    void getRecord() throws Exception {
        var key = "1";
        var record = Optional.of(new Record());
        Mockito.when(fileService.getRecord(key)).thenReturn(record);
        mockMvc.perform(MockMvcRequestBuilders.get("/record")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("key", key))
                .andExpect(status().is(200));
    }


    @Test
    void recordIsNotFound() throws Exception {
        var key = "1";
        var response = "Record not found";
        Mockito.when(fileService.getRecord(key)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/record")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("key", key))
                .andExpect(status().is(404))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenTimeFromAndTimeTo() throws Exception {
        var timeFrom = "2020-09-20 14:54:00.121";
        var timeTo = "2020-09-20 14:54:00.122";
        Mockito.when(fileService.getRecordsByTimeStampBetweenTimeFromAndTimeTo(timeFrom, timeTo, 0, 10))
                .thenReturn(Page.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(200));
    }

    @Test
    void getRecordsByTimeStampBetweenWhenTimeFromIncorrect() throws Exception {
        var timeFrom = "abc";
        var timeTo = "2020-09-20 14:54:00.122";
        var response = "Incorrect timeFrom: " + timeFrom;
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenWhenTimeToIncorrect() throws Exception {
        var timeFrom = "2020-09-20 14:54:00.122";
        var timeTo = "abc";
        var response = "Incorrect timeTo: " + timeTo;
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenTimeToAndTimeFromWhenTimeFromMoreThanTimeTo() throws Exception {
        var timeFrom = "2020-09-20 14:54:00.123";
        var timeTo = "2020-09-20 14:54:00.122";
        var response = "timeFrom must be less than timeTo";
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }


}