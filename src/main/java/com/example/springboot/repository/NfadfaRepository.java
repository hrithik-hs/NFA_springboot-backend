package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.model.Nfadfa;

@Repository
public interface NfadfaRepository  extends JpaRepository<Nfadfa,Long> {

}
