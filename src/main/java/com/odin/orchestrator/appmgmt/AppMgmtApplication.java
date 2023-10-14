package com.odin.orchestrator.appmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppMgmtApplication.class, args);
	}

}
