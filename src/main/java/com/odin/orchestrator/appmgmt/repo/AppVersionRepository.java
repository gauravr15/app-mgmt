package com.odin.orchestrator.appmgmt.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odin.orchestrator.appmgmt.entity.AppVersionModel;

public interface AppVersionRepository extends JpaRepository<AppVersionModel, Long>{

	List<AppVersionModel> findFirstByIsMandatoryAndEnvironmentOrderByIdDesc(boolean b, String environment);

}
