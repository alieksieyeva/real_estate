package com.generation.REA.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.REA.model.entities.House;

public interface HouseRepository extends JpaRepository <House, Integer>
{
	List<House> findByCity(String city);
}
