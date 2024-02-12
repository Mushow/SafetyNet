package uk.mushow.safetynet.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonDataSaver {

    private static final Logger log = LogManager.getLogger(JsonDataSaver.class);

    private final DataWrapper dataWrapper;
    private final ObjectMapper objectMapper;
    private final String jsonFile;

    @Autowired
    public JsonDataSaver(DataWrapper dataWrapper, ObjectMapper objectMapper, @Value("${json-data}") String jsonFile) {
        this.dataWrapper = dataWrapper;
        this.objectMapper = objectMapper;
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.jsonFile = jsonFile;
    }

    @EventListener
    public void onSaveData(ContextClosedEvent event) {
        try {
            File file = new File("src/main/resources/" + jsonFile);
            objectMapper.writeValue(file, dataWrapper);
            log.debug("Data successfully saved to JSON file from the populated DataWrapper.");
        } catch (IOException e) {
            log.error("Could not save JSON file: {}. Error: {}", jsonFile, e.getMessage());
        }
    }
}
