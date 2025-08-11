package pillihuaman.com.pe.security.util;


import lombok.*;
import org.bson.types.ObjectId;
import org.jboss.resteasy.jwt.JsonWebToken;
import pillihuaman.com.pe.security.dto.ResponseUser;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyJsonWebToken extends JsonWebToken implements Serializable {

    private static long serialVersionUID = 1L;

    private String tenantId;
    private ResponseUser user;
    private Aplication aplication;

    public static class Aplication {

        public ObjectId getAplicationID() {
            return aplicationID;
        }

        public void setAplicationID(ObjectId aplicationID) {
            this.aplicationID = aplicationID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMultiSession() {
            return multiSession;
        }

        public void setMultiSession(String multiSession) {
            this.multiSession = multiSession;
        }

        private ObjectId aplicationID;
        private String name;
        private String multiSession;

    }


    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    /**
     * @param serialversionuid the serialversionuid to set
     */
    public static void setSerialversionuid(long serialversionuid) {
        serialVersionUID = serialversionuid;
    }


    /**
     * @param idUser the idUser to set
     */


    /**
     * @param aplicationID the aplicationID to set
     */
    public void setAplicationID(ObjectId aplicationID) {
        this.aplication.aplicationID = aplicationID;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.aplication.name = name;
    }

    /**
     * @param multiSession the multiSession to set
     */
    public void setMultiSession(String multiSession) {
        this.aplication.multiSession = multiSession;
    }

    public void setUser(ResponseUser user) {
        this.user = user;
    }

    public void setAplication(Aplication aplication) {
        this.aplication = aplication;
    }

}
