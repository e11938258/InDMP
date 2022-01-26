package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.DMPConstants;
import at.tuwien.indmp.util.Functions;

public class License extends ClassEntity {

    /* Properties */
    @NotNull
    private URI license_ref;

    @NotNull
    @JsonFormat(pattern = DMPConstants.DATE_FORMAT_ISO_8601)
    private Date start_date;

    public License() {
    }

    public URI getLicense_ref() {
        return this.license_ref;
    }

    public void setLicense_ref(URI license_ref) {
        this.license_ref = license_ref;
    }

    public Date getStart_date() {
        return this.start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getStart_date() != null ? DMPConstants.DATE_FORMATTER_ISO_8601.format(getStart_date()) : null,
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "start_date",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getLicense_ref().toString();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        if (hasRightsToUpdate(rdmService)) {
            final Property property = new Property(dmp.getClassIdentifier(), getClassType(), getClassIdentifier(),
                    "license_ref", getClassIdentifier(), reference);
            properties.add(property);
        }

        return properties;
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, getClassType(), classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("start_date", properties);
        try {
            setStart_date(p != null ? DMPConstants.DATE_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set identifier
        p = Functions.findPropertyInList("license_ref", properties);
        setLicense_ref(URI.create(p.getValue()));
    }
}
