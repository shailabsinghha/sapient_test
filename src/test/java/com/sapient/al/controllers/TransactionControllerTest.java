package com.sapient.al.controllers;

import com.sapient.al.dto.ProcessingData;
import com.sapient.al.model.Transaction;
import com.sapient.al.service.files.CsvFileServiceImpl;
import com.sapient.al.service.files.FileService;
import com.sapient.al.service.files.FileServiceFactory;
import com.sapient.al.service.rules.TransactionRulesImpl;
import com.sapient.al.service.transactions.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.*;
import java.util.*;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@Slf4j
class TransactionControllerTest {

        MockMvc mockMvc;
        private List<Transaction> transactionList = new ArrayList<>();
        private Set<ProcessingData> processingData = new HashSet<>();


        @InjectMocks
        TransactionController controller;

        @Mock
        TransactionService transactionService;

        @Mock
        TransactionRulesImpl trasactionRules;

        @Mock
        FileServiceFactory fileServiceFactory;

        public TransactionControllerTest(){


                controller = new TransactionController(transactionService,
                        trasactionRules, fileServiceFactory);

                mockMvc = MockMvcBuilders
                        .standaloneSetup(controller).build();

                Transaction transaction1 = new Transaction("2133", "34",
                        "343", "buy", "3454", 1.4f, "Y");

                Transaction transaction2 = new Transaction("2134", "34",
                        "343", "sell", "3454", 1.4f, "Y");

                transactionList.add(transaction1);
                transactionList.add(transaction2);

                ProcessingData pd1 = new ProcessingData("34", "buy",
                        "3454", "y", 10.0f);
                ProcessingData pd2 = new ProcessingData("34", "sell",
                        "3454", "y", 10.0f);


                processingData.add(pd1);
                processingData.add(pd2);


        }

        @Test
        public void getTransaction() throws Exception {


                FileService fileService = new CsvFileServiceImpl();
                byte[] test =  IOUtils.toByteArray(new FileInputStream( new File("temp.csv")));

                Mockito.lenient().when(transactionService
                        .getAllData())
                        .thenReturn(transactionList);


                Mockito.lenient().when(trasactionRules
                        .getProcessingFees(transactionList))
                        .thenReturn(processingData);

                Mockito.lenient().when(fileServiceFactory
                        .getInstance("csv"))
                        .thenReturn(fileService);

                MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/transaction")
                        .param("type", "csv")
                        .accept(MediaType.APPLICATION_OCTET_STREAM))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

                Assert.assertNotNull(test);

                Assert.assertEquals(200, result.getResponse().getStatus());
                Assert.assertEquals( test.length,
                        result.getResponse().getContentAsByteArray().length);
                Assert.assertEquals("application/octet-stream", result.getResponse().getContentType());

        }

        @Test
        public void putTransaction() throws Exception {


                FileService fileService = new CsvFileServiceImpl();
                InputStream is = new FileInputStream("file_upload_in.csv");

                MockMultipartFile file =
                        new MockMultipartFile("file", "temp_in.csv",
                                MediaType.TEXT_PLAIN_VALUE, is);


                Assert.assertNotNull(file);

                Mockito.lenient().when(fileServiceFactory
                        .getInstance("csv"))
                        .thenReturn(fileService);


                MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .fileUpload("/v1/transaction")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andReturn();


                Assert.assertEquals(21, Integer.parseInt(result.getResponse().getContentAsString().trim()));

                Assert.assertEquals(200,
                        result.getResponse().getStatus());
                Assert.assertEquals("application/json",
                        result.getResponse().getContentType());

        }



}