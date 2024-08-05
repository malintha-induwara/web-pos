package lk.ijse.webpos.backend.api.initializer;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lk.ijse.webpos.backend.util.DirectoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebListener
public class ApplicationInitializer implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DirectoryUtil.init();
            logger.info("Application directories initialized successfully");
        } catch (IOException e) {
            logger.error("Error initializing application directories", e);
            throw new RuntimeException("Failed to initialize application", e);
        }
    }
}

