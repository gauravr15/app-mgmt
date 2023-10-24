package com.odin.orchestrator.appmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odin.orchestrator.appmgmt.entity.Messages;

public interface MessagesRepository extends JpaRepository<Messages, Integer> {

}
