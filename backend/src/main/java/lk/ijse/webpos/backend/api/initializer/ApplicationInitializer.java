package lk.ijse.webpos.backend.api.initializer;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    private static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final Path IMAGE_DIRECTORY = Paths.get(HOME_DIRECTORY, "Desktop", "webPos", "imageSave");
    public static final Path LOG_DIRECTORY = Paths.get(HOME_DIRECTORY, "Desktop", "webPos", "logs");
    private static final Path POS_DIRECTORY = Paths.get(HOME_DIRECTORY, "Desktop", "webPos");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            List<Path> pathsToCreate = Arrays.asList(POS_DIRECTORY, IMAGE_DIRECTORY, LOG_DIRECTORY);
            for (Path path : pathsToCreate) {
                createDirectory(path);
            }
            logger.info("Application directories initialized successfully");
        } catch (IOException e) {
            logger.error("Error initializing application directories", e);
            throw new RuntimeException("Failed to initialize application", e);
        }
    }

    private static void createDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}

