package at.tuwien.indmp.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // maDMP REST API
    public static final String MODIFY_MADMP_INFORMATION = "/madmp"; // PUT
    public static final String UPDATE_MADMP_OBJECT_IDENTIFIER = "/madmp/object/identifier"; // PUT
    public static final String REMOVE_MADMP_OBJECT = "/madmp/object/remove"; // PUT
    public static final String GET_MADMP = "/madmp"; // GET
    public static final String GET_PROVENANCE_INFORMATION = "/madmp/provenance"; // GET

    // RDM service REST API
    public static final String CREATE_RDM_SERVICE = "/service"; // POST
    public static final String READ_RDM_SERVICES = "/services"; // GET
    public static final String SET_PROPERTY_RIGHTS_TO_RDM_SERVICE = "/service/rights/property/{accessRights}"; // PUT
}
