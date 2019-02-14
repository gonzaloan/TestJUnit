package com.sodexo.junit.springjunittest.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sodexo.junit.springjunittest.domain.Arrival;

@Repository
public interface ArrivalRepository extends JpaRepository<Arrival, Integer>{

}
