package com.odin.orchestrator.appmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odin.orchestrator.appmgmt.entity.ResponseMessages;

@Repository
public interface ResponseMessagesRepository extends JpaRepository<ResponseMessages, Integer> {

}
