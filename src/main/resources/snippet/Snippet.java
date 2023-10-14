package snippet;

public class Snippet {
	spring.application.name=core-data
	spring.profiles.active=development,global
	spring.cloud.config.label=1
	spring.config.import=optional:configserver:http://localhost:8008/config-server
}

