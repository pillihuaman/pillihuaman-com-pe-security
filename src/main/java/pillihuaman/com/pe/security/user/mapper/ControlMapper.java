package pillihuaman.com.pe.security.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.security.dto.RespControl;
import pillihuaman.com.pe.security.entity.control.Control;


import java.util.List;
@Mapper
public interface ControlMapper {
    ControlMapper INSTANCE = Mappers.getMapper(ControlMapper.class);
    RespControl toRespControl(Control control);
    List<RespControl> controlsToRespControls(List<Control> controls);

}
