package pillihuaman.com.pe.security.service; // O tu paquete correcto

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.GeoLocation;
import pillihuaman.com.pe.security.dto.ClientAuditInfo;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Date;

@Service
@Slf4j
public class AuditDataService {

    private final Parser uaParser;
    private final DatabaseReader geoIpReader;

    public AuditDataService() {
        this.uaParser = new Parser();
        DatabaseReader tempReader = null;
        try {
            InputStream dbStream = getClass().getClassLoader().getResourceAsStream("GeoLite2-City.mmdb");
            if (dbStream != null) {
                tempReader = new DatabaseReader.Builder(dbStream).build();
                log.info("Base de datos GeoLite2-City cargada exitosamente.");
            } else {
                log.warn("Archivo GeoLite2-City.mmdb no encontrado. La geolocalización estará deshabilitada.");
            }
        } catch (Exception e) {
            log.error("Error al cargar GeoLite2-City.mmdb. La aplicación continuará sin geolocalización. Error: {}", e.getMessage());
        }
        this.geoIpReader = tempReader;
    }

    public AuditEntity buildAuditEntity(HttpServletRequest request, ClientAuditInfo clientInfo, String userEmail, String authMethod) {
        AuditEntity.AuditEntityBuilder auditBuilder = AuditEntity.builder();

        String ipAddress = getClientIpAddress(request);
        String userAgentString = request.getHeader("User-Agent");
        Client uaClient = (userAgentString != null) ? uaParser.parse(userAgentString) : null;

        auditBuilder
                .dateRegister(new Date())
                .mail(userEmail)
                .authMethod(authMethod)
                .sourceIpAddress(ipAddress) // <-- Aquí se usa la IP corregida
                .userAgent(userAgentString)
                .browserName(uaClient != null ? uaClient.userAgent.family : null)
                .browserVersion(uaClient != null ? uaClient.userAgent.major : null)
                .osName(uaClient != null ? uaClient.os.family : null)
                .osVersion(uaClient != null ? uaClient.os.major : null)
                .deviceType(uaClient != null ? uaClient.device.family : null)
                .geoLocation(getGeoLocation(ipAddress));

        if (clientInfo != null) {
            auditBuilder
                    .screenResolution(clientInfo.getScreenResolution())
                    .language(clientInfo.getLanguage())
                    .platform(clientInfo.getPlatform())
                    .timeZone(clientInfo.getTimeZone())
                    .networkType(clientInfo.getNetworkType())
                    .connectionDownlink(clientInfo.getConnectionDownlink())
                    .connectionRtt(clientInfo.getConnectionRtt())
                    .deviceId(clientInfo.getDeviceId())
                    .sessionId(clientInfo.getSessionId())
                    .lastVisitedPage(clientInfo.getLastVisitedPage())
                    .clickPath(clientInfo.getClickPath())
                    .idleTime(clientInfo.getIdleTime());
        }
        return auditBuilder.build();
    }

    private GeoLocation getGeoLocation(String ipAddress) {
        if (geoIpReader == null || ipAddress == null) return null;
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            CityResponse response = geoIpReader.city(ip);
            return GeoLocation.builder()
                    .country(response.getCountry().getName())
                    .city(response.getCity().getName())
                    .coordinates(response.getLocation().getLatitude() + "," + response.getLocation().getLongitude())
                    .build();
        } catch (Exception e) {
            log.warn("Geolocalización no encontrada para la IP {}: {}", ipAddress, e.getMessage());
            return null;
        }
    }

    /**
     * MÉTODO CORREGIDO Y MEJORADO PARA OBTENER LA IP
     * Primero busca en los headers de proxy comunes (como X-Forwarded-For).
     * Si no encuentra ninguno, usa el método estándar request.getRemoteAddr().
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headersToCheck = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA"
        };

        for (String header : headersToCheck) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                String clientIp = ip.split(",")[0].trim();
                log.debug("IP de cliente encontrada en el header '{}': {}", header, clientIp);
                return clientIp;
            }
        }

        String remoteAddr = request.getRemoteAddr();
        log.debug("No se encontraron headers de proxy, usando request.getRemoteAddr(): {}", remoteAddr);
        return remoteAddr;
    }
}