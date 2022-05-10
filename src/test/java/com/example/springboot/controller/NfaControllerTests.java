package com.example.springboot.controller;

import com.example.springboot.model.Nfa;
import com.example.springboot.repository.NfaRepository;
import com.example.springboot.source.FiniteAutomaton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NfaController.class)
public class NfaControllerTests {
    @MockBean
    private NfaRepository nfaRepository;

//    @MockBean
//    private FiniteAutomaton nfa1;

    @Autowired
    private MockMvc mockMvc;

    private static final Logger logger = LogManager.getLogger(NfaController.class);

    @Test
    void getAllNfasTest()throws Exception{
        logger.info("[Get All NFA Test]");

        Nfa nfa =new Nfa(5,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        List<Nfa> nfas =new ArrayList<>();
        nfas.add(nfa);
        when(nfaRepository.findAll()).thenReturn(nfas);
        mockMvc.perform(get("/nfa"))
                .andExpect(jsonPath("$[0].states").value("A,B,D"))
                .andExpect(jsonPath("$[0].symbols").value("a,b,d"))
                .andExpect(status().isOk());

        logger.info("[Get All NFA Test Passed]");
    }

    @Test
    void createNfaTest() throws Exception{
        logger.info("[Create NFA Test]");

        Nfa nfa =new Nfa(5,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        FiniteAutomaton nfa1 =new FiniteAutomaton(nfa);

        when(nfaRepository.save(nfa)).thenReturn(nfa);
//        when(new FiniteAutomaton(nfa)).thenReturn(nfa1);
//        when(nfa1.getRE()).thenReturn(nfa1.getRE());


        mockMvc.perform(post("/nfa")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"states\": \"A,B,D\" ," +
                        " \"symbols\": \"a,b,d\"," +
                        " \"initialState\": \"A\" ," +
                        "\"finalState\": \"D\" , " +
                        "\"transition\": \"A:b:B,B:a:D,A:d:D\" }"))
                .andExpect(status().isOk()).andDo(print());

        logger.info("[Create NFA Test Passed]");
    }

    @Test
    void createNfaFalseTest() throws Exception{
        logger.info("[Create NFA False Test]");
        Nfa nfa =new Nfa(5,"A,B","a,b,d","A","D","A:b:B,B:a:D,A:d:D");

        when(nfaRepository.save(nfa)).thenReturn(nfa);
        String error= mockMvc.perform(post("/nfa")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"states\": \"A,B\" ," +
                        " \"symbols\": \"a,b,d\"," +
                        " \"initialState\": \"A\" ," +
                        "\"finalState\": \"D\" , " +
                        "\"transition\": \"A:b:B,B:a:D,A:d:D\" }"))
                .andExpect(status().is(400)).andDo(print())
                .andReturn().getResolvedException().getMessage();
        System.out.println(error);
        assertTrue(error.contains("Input for NFA invalid"),"Input for NFA invalid");

        logger.info("[Create NFA False Test Passes] " + error);
    }

    @Test
    void getNfabyIdTest()throws Exception{
        logger.info("[Get NFA by ID Test]");
        Nfa nfa =new Nfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");

        Long id=1L;
        when(nfaRepository.findById(id)).thenReturn(Optional.of(nfa));

        mockMvc.perform(get("/nfa/{id}",id))
                .andExpect(jsonPath("$.states").value("A,B,D"))
                .andExpect(jsonPath("$.symbols").value("a,b,d"))
                .andExpect(jsonPath("$.transition").value("A:b:B,B:a:D,A:d:D"))
                .andExpect(status().isOk())
                .andDo(print());
        logger.info("[Get NFA by ID Test Passed]");
    }

    @Test
    void getNfaDfabyIdTest() throws Exception{
        logger.info("[Get NFA DFA by ID Test]");

        Nfa nfa =new Nfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");

        Long id=1L;
        when(nfaRepository.findById(id)).thenReturn(Optional.of(nfa));

        mockMvc.perform(get("/nfa/dfa/{id}",id))
                .andExpect(jsonPath("$[0].states").value("A,B,D"))
                .andExpect(jsonPath("$[0].symbols").value("a,b,d"))
                .andExpect(jsonPath("$[0].transition").value("A:b:B,B:a:D,A:d:D"))
                .andExpect(jsonPath("$[1].states").value("A,B,D"))
                .andExpect(jsonPath("$[1].symbols").value("a,b,d"))
                .andExpect(jsonPath("$[1].transition").value("A:b:B,B:a:D,A:d:D"))
                .andExpect(status().isOk());

        logger.info("[Get NFA DFA by ID Test Passed]");
    }

    @Test
    void updateNfaTest() throws Exception{
        logger.info("[Update NFA by ID Test]");

        Nfa nfa =new Nfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        Nfa nfa1 =new Nfa(1,"A,B,C","a,b,d","A","C","A:b:B,B:a:C,A:d:C");

        Long id=1L;
        when(nfaRepository.findById(id)).thenReturn(Optional.of(nfa));
        when(nfaRepository.save(nfa1)).thenReturn(nfa1);

        mockMvc.perform(put("/nfa/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"states\": \"A,B,C\" ," +
                                " \"symbols\": \"a,b,d\"," +
                                " \"initialState\": \"A\" ," +
                                "\"finalState\": \"C\" , " +
                                "\"transition\": \"A:b:B,B:a:C,A:d:C\" }"))
                .andExpect(jsonPath("$.states").value("A,B,C"))
                .andExpect(jsonPath("$.symbols").value("a,b,d"))
                .andExpect(jsonPath("$.transition").value("A:b:B,B:a:C,A:d:C"))
                .andExpect(status().isOk()).andDo(print());
        logger.info("[Update NFA by ID Test Passed]");
    }

    @Test
    void updateNfaFalseTest() throws Exception{
        logger.info("[Update NFA by ID False Test]");

        Nfa nfa =new Nfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        Nfa nfa1 =new Nfa(1,"A,B,C","a,b,d","A","C","A:b:B,B:a:C,A:d:C");

        Long id=1L;
        when(nfaRepository.findById(id)).thenReturn(Optional.of(nfa));
        when(nfaRepository.save(nfa1)).thenReturn(nfa1);

        String error=mockMvc.perform(put("/nfa/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"states\": \"A,B\" ," +
                        " \"symbols\": \"a,b,d\"," +
                        " \"initialState\": \"A\" ," +
                        "\"finalState\": \"C\" , " +
                        "\"transition\": \"A:b:B,B:a:C,A:d:C\" }"))
                .andExpect(status().is(400)).andDo(print())
                .andReturn().getResolvedException().getMessage();
        System.out.println(error);
        assertTrue(error.contains("Input for NFA invalid"),"Input for NFA invalid");

        logger.info("[Update NFA by ID False Test Passed] "+error);
    }

    @Test
    void deleteNfaTest() throws Exception{
        logger.info("[Delete NFA by ID Test]");
        Nfa nfa =new Nfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        Long id=1L;
        when(nfaRepository.findById(id)).thenReturn(Optional.of(nfa));
        mockMvc.perform(delete("/nfa/{id}",id))
                .andExpect(jsonPath("$.deleted").value(true))
                .andExpect(status().isOk()).andDo(print());
        logger.info("[Delete NFA by ID Test Passed]");

    }


}
