package pillihuaman.com.pe.security.repository;

import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.Help.Constants;
import pillihuaman.com.pe.security.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.security.dto.ReqControl;
import pillihuaman.com.pe.security.entity.control.Control;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.util.MyJsonWebToken;

import java.util.List;

@Component
public class ControlDaoImplement extends AzureAbstractMongoRepositoryImpl<Control> implements ControlRepository {

    ControlDaoImplement() {
        DS_WRITE = Constants.DW;
        // DS_READ = Constants.DR;
        COLLECTION = Constants.COLLECTION_CONTROL;
    }

    private UserRepository userRepository;

    @Override
    public Class<Control> provideEntityClass() {
        return Control.class;
    }

    @Override
    public List<Control> listControl(ReqControl reqControl) {
        return List.of();
    }

    @Override
    public Control saveControl(Control reqControl, MyJsonWebToken to) {
        return null;
    }

    @Override
    public List<Control> findByUser(User user) {
        return List.of();
    }
/*
    @Override
    public List<Control> listControl(ReqControl reqControl) {
        MongoCollection<Control> collection = getCollection(this.COLLECTION, Control.class);
        Document query = null;
        if (reqControl != null) {
            if (reqControl.getId_user() != null && !reqControl.getId_user().toString().isEmpty()) {
                query = new Document().append("id", reqControl.getId());
            }
        } else {
            query = new Document().append("status", ConstantsUseful.CONTROL_ESTATE_PUBLIC);

        }
        List<Control> lisControl = collection.find(query, Control.class)
                .into(new ArrayList<Control>());
        return lisControl;
    }

    @Override
    public Control saveControl(Control request, MyJsonWebToken to) {
        RespControl re = new RespControl();
        List<RespControl> lstre = new ArrayList<>();
        Document doc = new Document();
        Document docAud = new Document();
        //	List<User> lst=	userRepository.findUserById(request.getId_user());
        AuditEntity aud = new AuditEntity();
        aud.setMail(to.getUser().getMail());
        aud.setDateRegister(new Date());
        request.setAuditEntity(aud);
        return save(request);
    }

    @Override
    public List<Control> findByUser(User user) {
        MongoCollection<Control> collection = getCollection(this.collectionName, Control.class);

        // Assuming you have a field like "user.id" that stores the user's ObjectId in Control class.
        Document query = new Document("user.id", user.getId());

        List<Control> lisControl = collection.find(query, Control.class)
                .into(new ArrayList<Control>());
        return lisControl;
    }


    public String createCodeControl(ReqControl re) {
        //1  idSystem 1 support
        //2 idMenu 1 Support control
        //3 idPage 1 Save and Update control
        //4 text SAVE =SAVE  SAVE CONTROL= SC
        //5 purpose save =SA  Update=UP delete=DE export=EX select=SE count=CO  Search=SE  ETC TO MIX LIKE SAVE AND DELETE SADE , save and update SAUP
        String code = re.get + "" + re.getIdMenu() + "" + re.getIdPage() + re.getText() + re.getDescription().toUpperCase();

        return code;
    }

    public List<Control> listControlbyCode(ReqControl reqControl) {
        MongoCollection<Control> collection = getCollection(this.collectionName, Control.class);
        Document query = null;
        if (reqControl != null) {
            if (reqControl.getId_user() != null) {
                query = new Document()
                        .append("idMenu", reqControl.getIdMenu())
                        .append("idSystem", reqControl.getIdSystem())
                        .append("idPage", reqControl.getIdPage())
                        .append("text", reqControl.getText())
                        .append("description", reqControl.getDescription());
            }
        }
        List<Control> lisControl = collection.find(query, Control.class)
                .into(new ArrayList<Control>());
        return lisControl;
    }

    public List<Control> listControlbyIdUsr(ReqControl reqControl) {
        MongoCollection<Control> collection = getCollection(this.collectionName, Control.class);
        Document query = null;
        if (reqControl != null) {
            if (reqControl.getId_user() != null) {
                query = new Document()
                        .append("idMenu", reqControl.getIdMenu())
                        .append("idSystem", reqControl.getIdSystem())
                        .append("idPage", reqControl.getIdPage())
                        .append("text", reqControl.getText())
                        .append("description", reqControl.getDescription());
            }
        }
        List<Control> lisControl = collection.find(query, Control.class)
                .into(new ArrayList<Control>());
        return lisControl;
    }*/
}
