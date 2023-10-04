package pillihuaman.com.security.user.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.basebd.control.Control;
import pillihuaman.com.lib.response.RespControl;

import java.util.List;
@Mapper
public interface ControlMapper {
    ControlMapper INSTANCE = Mappers.getMapper(ControlMapper.class);
    @Mapping(source = "idCode", target = "idCode")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "icono", target = "icono")
    @Mapping(source = "iconClass", target = "iconClass")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "styleClass", target = "styleClass")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "text", target = "text")
    RespControl toRespControl(Control control);
    List<RespControl> controlsToRespControls(List<Control> controls);

}
