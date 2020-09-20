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
    MockMvc mockMvc;

    @MockBean
    FileService fileService;

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
    void getRecordsByTimeStampBetweenFromDateAndToDate() throws Exception {
        String fromDate = "2020-09-20 14:54:00.121";
        String toDate = "2020-09-20 14:54:00.122";
        Mockito.when(fileService.getRecordsByTimeStampBetweenFromDateAndToDate(fromDate, toDate, 0, 10)).thenReturn(Page.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("fromDate", fromDate)
                .param("toDate", toDate))
                .andExpect(status().is(200));
    }

    @Test
    void getRecordsByTimeStampBetweenWhenFromDateIncorrect() throws Exception {
        String fromDate = "abc";
        String toDate = "2020-09-20 14:54:00.122";
        String response = "Incorrect fromDate: " + fromDate;
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("fromDate", fromDate)
                .param("toDate", toDate))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenWhenToDateIncorrect() throws Exception {
        String fromDate = "2020-09-20 14:54:00.122";
        String toDate = "abc";
        String response = "Incorrect toDate: " + toDate;
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("fromDate", fromDate)
                .param("toDate", toDate))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }

    @Test
    void getRecordsByTimeStampBetweenToDateAndFromDateWhenFromDateMoreLessToDate() throws Exception {
        String fromDate = "2020-09-20 14:54:00.123";
        String toDate = "2020-09-20 14:54:00.122";
        String response = "fromDate must be less than toDate";
        mockMvc.perform(MockMvcRequestBuilders.get("/records")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("fromDate", fromDate)
                .param("toDate", toDate))
                .andExpect(status().is(400))
                .andExpect(content().string(response));
    }
}