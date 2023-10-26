package com.odin.orchestrator.appmgmt.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odin.orchestrator.appmgmt.entity.APIInfo;

public interface APIInfoRepository extends JpaRepository<APIInfo, Long>{

	List<APIInfo> findByEnvironment(String parameter);

	List<APIInfo> findByEnvironmentIgnoreCase(String env);

}
