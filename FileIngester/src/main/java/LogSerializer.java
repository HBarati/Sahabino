
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import java.util.Map;

public class LogSerializer implements Serializer<Log> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Log log) {
        try {
            return new ObjectMapper().writer().writeValueAsBytes(log);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}
