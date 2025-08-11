package pillihuaman.com.pe.security.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.common.ResponseUtil;
import pillihuaman.com.pe.security.dto.NotificationRequestDTO;
import pillihuaman.com.pe.security.service.GenericNotificationService;


@RestController
@RequestMapping("/api/v1/notifications") // Nueva ruta dedicada
@RequiredArgsConstructor
public class NotificationController {

    private final GenericNotificationService genericNotificationService;

    @PostMapping("/send")
    public ResponseEntity<RespBase<Object>> sendNotification(@Valid @RequestBody RespBase<NotificationRequestDTO> request) {
        genericNotificationService.dispatchNotification(request.getPayload());
        return ResponseEntity.accepted().body(
                ResponseUtil.buildSuccessResponse("Notificación encolada para envío.")
        );
    }
}