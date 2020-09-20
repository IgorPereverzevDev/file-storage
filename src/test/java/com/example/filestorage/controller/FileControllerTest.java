package com.example.filestorage.controller;

import com.example.filestorage.entity.Record;
import com.example.filestorage.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void uploadFile() throws Exception {
        String response = "Your file was uploaded successfully";
        String data = "abc";
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", data.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().is(202))
                .andExpect(content().string(response));
    }

    @Test
    void uploadFileWhenFileIsEmpty() throws Exception {
        String response = "Empty file";
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", "".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void removedRecord() throws Exception {
        String id = "1";
        String response = "Record removed";
        mockMvc.perform(MockMvcRequestBuilders.delete("/remove")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id", id))
                .andExpect(status().is(202))
                .andExpect(content().string(response));
    }

    @Test
    void removedRecordWhenIdInvalid() throws Exception {
        String id = "abc";
        String response = "Incorrect id: " + id;
        mockMvc.perform(MockMvcRequestBuilders.delete("/remove")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id", id))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecord() throws Exception {
        String id = "1";
        Optional<Record> record = Optional.of(new Record());
        Mockito.when(fileService.getRecord(id)).thenReturn(record);
        mockMvc.perform(MockMvcRequestBuilders.get("/record")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id", id))
                .andExpect(status().is(200));
    }

    @Test
    void getRecordWhenIdInvalid() throws Exception {
        String id = "abc";
        String response = "Incorrect id: " + id;
        mockMvc.perform(MockMvcRequestBuilders.get("/record")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id", id))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordWhenRecordIsNotFound() throws Exception {
        String id = "1";
        String response = "Record not found";
        Mockito.when(fileService.getRecord(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/record")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id", id))
                .andExpect(status().is(404))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenTimeFromAndTimeTo() throws Exception {
        String timeFrom = "2020-09-20 14:54:00.121";
        String timeTo = "2020-09-20 14:54:00.122";
        Mockito.when(fileService.getRecordsByTimeStampBetweenTimeFromAndTimeTo(timeFrom, timeTo, 0, 10)).thenReturn(Page.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(200));
    }

    @Test
    void getRecordsByTimeStampBetweenWhenTimeFromIncorrect() throws Exception {
        String timeFrom = "abc";
        String timeTo = "2020-09-20 14:54:00.122";
        String response = "Incorrect timeFrom: " + timeFrom;
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenWhenTimeToIncorrect() throws Exception {
        String timeFrom = "2020-09-20 14:54:00.122";
        String timeTo = "abc";
        String response = "Incorrect timeTo: " + timeTo;
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenTimeToAndTimeFromWhenTimeFromMoreThanTimeTo() throws Exception {
        String timeFrom = "2020-09-20 14:54:00.123";
        String timeTo = "2020-09-20 14:54:00.122";
        String response = "timeFrom must be less than timeTo";
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("timeFrom", timeFrom)
                .param("timeTo", timeTo))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }
}