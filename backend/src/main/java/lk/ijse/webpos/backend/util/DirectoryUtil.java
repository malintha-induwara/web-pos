package lk.ijse.webpos.backend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DirectoryUtil {
    private static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final Path IMAGE_DIRECTORY = Paths.get(HOME_DIRECTORY, "Desktop", "webPos", "imageSave");
    public static final Path LOG_DIRECTORY = Paths.get(HOME_DIRECTORY, "Desktop", "webPos", "logs");
    private static final Path POS_DIRECTORY = Paths.get(HOME_DIRECTORY, "Desktop", "webPos");

    public static void init() throws IOException {
        List<Path> pathsToCreate = Arrays.asList(POS_DIRECTORY, IMAGE_DIRECTORY, LOG_DIRECTORY);
        for (Path path : pathsToCreate) {
            createDirectory(path);
        }
    }

    private static void createDirectory(Path path) throws IOException {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
    }
}

