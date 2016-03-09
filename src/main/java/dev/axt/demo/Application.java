package dev.axt.demo;

import static dev.axt.demo.BackendConstants.API_BASE;
import static dev.axt.demo.BackendConstants.SECURITY_BASE;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.google.common.base.Predicates.or;

/**
 * Spring Boot Application for a REST backend with Swagger UI and Security
 * enabled
 *
 * @author alextremp
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan("dev.axt.demo.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("api")
				.apiInfo(apiInfo("Backend API", "Spring REST API Sample"))
				.select().paths(or(regex(SECURITY_BASE + ".*"), regex(API_BASE + ".*")))
				.build();
	}

	private ApiInfo apiInfo(String title, String description) {
		return new ApiInfoBuilder()
				.title(title)
				.description(description)
				.contact("Alex Castells")
				.version("1.0")
				.build();
	}

}
