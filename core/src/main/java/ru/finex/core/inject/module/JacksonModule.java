package ru.finex.core.inject.module;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import lombok.EqualsAndHashCode;
import ru.finex.core.inject.LoaderModule;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode
public class JacksonModule extends AbstractModule {

    @Override
    protected void configure() {
        JsonFactory jsonFactory = MappingJsonFactory.builder()
            .configure(JsonReadFeature.ALLOW_MISSING_VALUES, true)
            .configure(StreamReadFeature.STRICT_DUPLICATE_DETECTION, true)
            .configure(StreamReadFeature.IGNORE_UNDEFINED, true)
            .build();
        bind(JsonFactory.class).toInstance(jsonFactory);

        ObjectMapper mapper = new ObjectMapper(jsonFactory)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        bind(ObjectMapper.class).toInstance(mapper);
    }

}
