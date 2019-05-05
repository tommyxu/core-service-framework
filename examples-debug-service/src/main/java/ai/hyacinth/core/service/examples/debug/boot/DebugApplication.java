package ai.hyacinth.core.service.examples.debug.boot;

import ai.hyacinth.core.service.examples.debug.config.DebugApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({DebugApplicationConfig.class})
@Slf4j
public class DebugApplication {
  public static void main(String[] args) {
    SpringApplication.run(DebugApplication.class, args);
  }
}