package net.chesstango.arenaui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Mauricio Coria
 */
@SpringBootApplication
public class ArenaUiApplication {

	/*
	 * Set the working directory to module uci-arena-tv before executing main
	 */
	public static void main(String[] args) {
		SpringApplication.run(ArenaUiApplication.class, args);
	}


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						//.allowCredentials(true)
						.allowedOrigins("*")
						.allowedMethods("*");
			}
		};
	}

}
