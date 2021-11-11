package zaslontelecom.esk.backend.api.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import zaslontelecom.esk.backend.api.Controller.Response.CustomErrorResponse;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorResponse> customHandleConflict(RuntimeException ex, WebRequest request) {

        CustomErrorResponse e = new CustomErrorResponse();
        e.setTimestamp(LocalDateTime.now());
        e.setMessage(ex.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof HandledException) {
            status = HttpStatus.CONFLICT;
        }
        if (ex instanceof BadCredentialsException || ex instanceof AuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
        }

        if (status == HttpStatus.INTERNAL_SERVER_ERROR){
            e.setMessage("Internal server error");
        }
        e.setStatus(status.value());

        return new ResponseEntity<>(e, status);
    }
}
