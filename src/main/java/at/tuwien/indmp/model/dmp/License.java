package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class License extends AbstractClassObject {

    /* Properties */
    @NotNull
    private URI license_ref;

    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private LocalDate start_date;

    public License() {
    }

    public URI getLicense_ref() {
        return this.license_ref;
    }

    public void setLicense_ref(URI license_ref) {
        this.license_ref = license_ref;
    }

    public LocalDate getStart_date() {
        return this.start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getStart_date() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getStart_date()) : null,
                getLicense_ref().toString()
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "start_date",
                "license_ref"
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getLicense_ref().toString();
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "start_date", properties);
        setStart_date(p != null ? LocalDate.parse(p.getValue()) : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        p = Functions.findPropertyInList(getObjectType(), "license_ref", properties);
        setLicense_ref(URI.create(p.getValue()));
    }
}
