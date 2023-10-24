package com.odin.orchestrator.appmgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odin.orchestrator.appmgmt.entity.AppVersionModel;

public interface AppVersionRepository extends JpaRepository<AppVersionModel, Long>{

}
