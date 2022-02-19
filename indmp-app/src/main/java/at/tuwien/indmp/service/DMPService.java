package at.tuwien.indmp.service;

import java.util.Date;
import java.util.List;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.idmp.IdentifierUnit;


public interface  DMPService {

    public void create(DMP dmp, RDMService rdmService);

    public DMP identifyDMP(DMP dmp, RDMService system);

    public void update(DMP currentDMP, DMP dmp, RDMService rdmService);

    public void updateModified(DMP currentDMP, Date modified, RDMService system);

    public void changeIdentifiers(DMP currentDMP, List<IdentifierUnit> identifiers, Date modified,
            RDMService rdmService);

    public void deleteInstances(DMP currentDMP, List<IdentifierUnit> identifiers);

    public DMPScheme loadWholeDMP(DMP dmp);

    public List<Property> loadDMPIdentifiers(DMP dmp);

}
