package serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.LogModel;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class LogSerializer implements Serializer<LogModel> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String s, LogModel logModel) {
        try {
            return new ObjectMapper().writer().writeValueAsBytes(logModel);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}
