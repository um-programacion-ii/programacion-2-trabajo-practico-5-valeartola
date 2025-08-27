import com.example.sistema_gestion_empleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistema_gestion_empleados.exceptions.EmailDuplicadoException;
import com.example.sistema_gestion_empleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistema_gestion_empleados.exceptions.ProyectoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejador para entidades no encontradas
    @ExceptionHandler({EmpleadoNoEncontradoException.class,
            DepartamentoNoEncontradoException.class,
            ProyectoNoEncontradoException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Recurso no encontrado");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Manejador para conflictos
    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Object> handleEmailDuplicadoException(EmailDuplicadoException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflicto de datos");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // Manejador para cualquier otra excepción
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error Interno del Servidor");
        body.put("message", "Ocurrió un error inesperado. Contacte al administrador.");
        body.put("path", request.getDescription(false).replace("uri=", ""));


        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
