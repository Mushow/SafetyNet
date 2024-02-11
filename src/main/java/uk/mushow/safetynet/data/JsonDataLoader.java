package uk.mushow.safetynet.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Getter
@Component
public class JsonDataLoader {

    private final DataWrapper jsonData;

    public JsonDataLoader(ObjectMapper mapper, @Value("${json-data}") String jsonFile) {
        Logger log = LoggerFactory.getLogger(JsonDataLoader.class);
        try {
            URL resourceUrl = getClass().getClassLoader().getResource(jsonFile);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("File not found: " + jsonFile);
            }
            jsonData = mapper.readValue(resourceUrl, DataWrapper.class);
            log.debug("Data successfully retrieved from JSON file.");
        } catch (IOException e) {
            log.error("Could not read JSON file: {}. Error: {}", jsonFile, e.getMessage());
            throw new RuntimeException("Could not initialize data structure from JSON file: " + jsonFile, e);
        }
    }

}