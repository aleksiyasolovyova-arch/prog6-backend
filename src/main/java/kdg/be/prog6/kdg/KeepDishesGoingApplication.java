package kdg.be.prog6.kdg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.Modulith;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Modulith
@EnableScheduling
@EnableAsync
public class KeepDishesGoingApplication {
    private static Logger log = LoggerFactory.getLogger(KeepDishesGoingApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KeepDishesGoingApplication.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    void onApplicationStarted() {
        ApplicationModules applicationModules = ApplicationModules.of(KeepDishesGoingApplication.class);
        applicationModules.forEach(applicationModule -> log.info("\n{}", applicationModule));
    }

}
