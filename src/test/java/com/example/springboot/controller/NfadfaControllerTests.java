package com.example.springboot.controller;

import com.example.springboot.model.Nfadfa;
import com.example.springboot.repository.NfadfaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NfadfaController.class)
public class NfadfaControllerTests {
    @MockBean
    private NfadfaRepository nfadfaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllNfadfasTest()throws Exception{
        Nfadfa nfadfa=new Nfadfa(5,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        List<Nfadfa> nfadfas=new ArrayList<>();
        nfadfas.add(nfadfa);
        when(nfadfaRepository.findAll()).thenReturn(nfadfas);
        mockMvc.perform(get("/nfadfas"))
                .andExpect(jsonPath("$[0].states").value("A,B,D"))
                .andExpect(jsonPath("$[0].symbols").value("a,b,d"))
                .andExpect(status().isOk());
    }

    @Test
    void createNfadfaTest() throws Exception{
        Nfadfa nfadfa=new Nfadfa(5,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        when(nfadfaRepository.save(nfadfa)).thenReturn(nfadfa);
        mockMvc.perform(post("/nfadfas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"states\": \"A,B,D\" ," +
                        " \"symbols\": \"a,b,d\"," +
                        " \"initialState\": \"A\" ," +
                        "\"finalState\": \"D\" , " +
                        "\"transition\": \"A:b:B,B:a:D,A:d:D\" }"))

                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void getNfadfasbyIdTest() throws Exception{
        Nfadfa nfadfa=new Nfadfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");

        Long id=1L;
        when(nfadfaRepository.findById(id)).thenReturn(Optional.of(nfadfa));

        mockMvc.perform(get("/nfadfas/{id}",id))
                .andExpect(jsonPath("$[0].states").value("A,B,D"))
                .andExpect(jsonPath("$[0].symbols").value("a,b,d"))
                .andExpect(jsonPath("$[0].transition").value("A:b:B,B:a:D,A:d:D"))
                .andExpect(jsonPath("$[1].states").value("A,B,D"))
                .andExpect(jsonPath("$[1].symbols").value("a,b,d"))
                .andExpect(jsonPath("$[1].transition").value("A:b:B,B:a:D,A:d:D"))
                .andExpect(status().isOk());
    }

    @Test
    void updateNfadfaTest() throws Exception{
        Nfadfa nfadfa=new Nfadfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        Nfadfa nfadfa1=new Nfadfa(1,"A,B,C","a,b,d","A","C","A:b:B,B:a:C,A:d:C");

        Long id=1L;
        when(nfadfaRepository.findById(id)).thenReturn(Optional.of(nfadfa));
        when(nfadfaRepository.save(nfadfa1)).thenReturn(nfadfa1);

        mockMvc.perform(put("/nfadfas/{id}",id)
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
    }

    @Test
    void deleteNfadfaTest() throws Exception{
        Nfadfa nfadfa=new Nfadfa(1,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
        Long id=1L;
        when(nfadfaRepository.findById(id)).thenReturn(Optional.of(nfadfa));
        mockMvc.perform(delete("/nfadfas/{id}",id))
                .andExpect(jsonPath("$.deleted").value(true))
                .andExpect(status().isOk()).andDo(print());
    }


}
