package serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.LogModel;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * This class make a serializer for kafka value config (VALUE_SERIALIZER_CLASS_CONFIG) to
 * invert the logModel frame to a readable format for kafka.
 */
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
