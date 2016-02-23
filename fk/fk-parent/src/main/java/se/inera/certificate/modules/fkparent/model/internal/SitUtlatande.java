package se.inera.certificate.modules.fkparent.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by BESA on 2016-02-23.
 */
public interface SitUtlatande {

    // Kategori 3 - Diagnos
    // Fråga 6
    public abstract ImmutableList<Diagnos> getDiagnoser();
}
