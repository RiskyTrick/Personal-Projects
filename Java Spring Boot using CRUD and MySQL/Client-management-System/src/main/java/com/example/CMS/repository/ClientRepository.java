package com.example.CMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.CMS.entity.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

	
}
