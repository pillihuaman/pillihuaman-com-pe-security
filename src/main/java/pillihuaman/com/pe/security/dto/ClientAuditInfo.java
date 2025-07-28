package pillihuaman.com.pe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.lib.common.GeoLocation;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientAuditInfo {
    private String screenResolution; // Ej: "1920x1080"
    private String language;         // Ej: "es-ES"
    private String platform;         // Ej: "Win32"
    private String timeZone;         // Ej: "America/Lima"
    private String networkType;      // Ej: "4g"
    private Double connectionDownlink;
    private Double connectionRtt;
    private String deviceId;         // Un UUID generado y guardado en el localStorage del navegador
    private String sessionId;        // Un UUID generado para esta sesión de login específica
    private String lastVisitedPage;
    private List<String> clickPath;
    private Long idleTime;

    private String sourceIpAddress;
    private String userAgent;
    private String deviceType;
    private GeoLocation geoLocation;

    private String browserName;
    private String browserVersion;
    private String osName;
    private String osVersion;

    private Boolean vpnDetected;
    private String authMethod;
}
