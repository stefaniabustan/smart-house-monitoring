package ro.bstefania.ds2024.exception;
import org.springframework.web.client.RestClientException;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

