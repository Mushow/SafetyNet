package uk.mushow.safetynet.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Getter
@Log4j2
@Component
public class JsonDataLoader {

    private final ObjectMapper mapper;
    private final DataWrapper dataWrapper;

    @Value("${json-data}")
    private String jsonFile;

    @Autowired
    public JsonDataLoader(ObjectMapper mapper, DataWrapper dataWrapper) {
        this.mapper = mapper;
        this.dataWrapper = dataWrapper;
    }

    @PostConstruct
    private void init() {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource(jsonFile);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("File not found: " + jsonFile);
            }
            // Populate the autowired DataWrapper instance
            mapper.readerForUpdating(dataWrapper).readValue(resourceUrl);
            log.info("Data successfully retrieved from JSON file and populated to DataWrapper.");
        } catch (IOException e) {
            log.error("Could not read JSON file: {}. Error: {}", jsonFile, e.getMessage());
            throw new RuntimeException("Could not initialize data structure from JSON file: " + jsonFile, e);
        }
    }
}
