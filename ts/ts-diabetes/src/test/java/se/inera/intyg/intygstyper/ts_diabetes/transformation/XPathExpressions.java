/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_diabetes.transformation;

import se.inera.intyg.intygstyper.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.intyg.intygstyper.ts_parent.transformation.test.DateXPathExpression;
import se.inera.intyg.intygstyper.ts_parent.transformation.test.StringXPathExpression;

/**
 * Defines xPath expressions and templates used to create all expressions needed by Transportstyrelsen.
 */
public final class XPathExpressions {
    private XPathExpressions() {
    }

    private static final String TIDSTAMPEL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final StringXPathExpression TYP_AV_UTLATANDE_XPATH = new StringXPathExpression("utlatande/p:typAvUtlatande/@code");

    public static final StringXPathExpression TS_UTGAVA_XPATH = new StringXPathExpression("utlatande/p2:utgava");

    public static final StringXPathExpression TS_VERSION_XPATH = new StringXPathExpression("utlatande/p2:version");

    public static final StringXPathExpression INVANARE_FORNAMN_XPATH = new StringXPathExpression("utlatande/p:patient/p:fornamn/text()");

    public static final StringXPathExpression INVANARE_EFTERNAMN_XPATH = new StringXPathExpression("utlatande/p:patient/p:efternamn/text()");

    public static final StringXPathExpression INVANARE_POSTADRESS_XPATH = new StringXPathExpression("utlatande/p:patient/p:postadress/text()");

    public static final StringXPathExpression INVANARE_POSTNUMMER_XPATH = new StringXPathExpression("utlatande/p:patient/p:postnummer/text()");

    public static final StringXPathExpression INVANARE_POSTORT_XPATH = new StringXPathExpression("utlatande/p:patient/p:postort/text()");

    public static final StringXPathExpression INVANARE_PERSONNUMMER_XPATH = new StringXPathExpression("utlatande/p:patient/p:person-id/@extension");
    
    public static final DateXPathExpression SIGNERINGSDATUM_XPATH = new DateXPathExpression(
            "utlatande/p:signeringsdatum", TIDSTAMPEL_FORMAT);

    public static final StringXPathExpression ENHET_VARDINRATTNINGENS_NAMN_XPATH = new StringXPathExpression(
            "/utlatande/p:skapadAv/p:enhet/p:enhetsnamn");

    public static final StringXPathExpression ENHET_ID_XPATH = new StringXPathExpression("/utlatande/p:skapadAv/p:enhet/p:enhets-id/@extension");

    public static final StringXPathExpression ENHET_POSTADRESS_XPATH = new StringXPathExpression("/utlatande/p:skapadAv/p:enhet/p:postadress");

    public static final StringXPathExpression ENHET_POSTORT_XPATH = new StringXPathExpression("/utlatande/p:skapadAv/p:enhet/p:postort");

    public static final StringXPathExpression ENHET_POSTNUMMER_XPATH = new StringXPathExpression("/utlatande/p:skapadAv/p:enhet/p:postnummer");

    public static final StringXPathExpression ENHET_TELEFONNUMMER_XPATH = new StringXPathExpression(
            "/utlatande/p:skapadAv/p:enhet/p:telefonnummer");

    public static final StringXPathExpression VARDGIVARE_ID_XPATH = new StringXPathExpression("/utlatande/p:skapadAv/p:enhet/p:vardgivare/p:vardgivare-id/@extension");

    public static final StringXPathExpression VARDGIVARE_NAMN_XPATH = new StringXPathExpression("/utlatande/p:skapadAv/p:enhet/p:vardgivare/p:vardgivarnamn/text()");

    public static final StringXPathExpression SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH = new StringXPathExpression(
            "/utlatande/p:skapadAv/p:fullstandigtNamn");
    
    public static final StringXPathExpression SKAPAD_AV_HSAID_XPATH = new StringXPathExpression(
            "/utlatande/p:skapadAv/p:personal-id/@extension");
    
    public static final BooleanXPathExpression SKAPAD_AV_SPECIALISTKOMPETENS_CHECK_XPATH = new BooleanXPathExpression(
            "/utlatande/p:skapadAv/p2:specialitet/@code");
    
    public static final StringXPathExpression SKAPAD_AV_SPECIALISTKOMPETENS_BESKRVNING_XPATH = new StringXPathExpression(
            "/utlatande/p:skapadAv/p2:specialitet/@code");
    
    public static final StringXPathExpression SKAPAD_AV_BEFATTNING_XPATH = new StringXPathExpression(
            "utlatande/p:skapadAv/p:befattning/text()");
    
    public static final StringXPathExpression VARD_PA_SJUKHUS_TID_XPATH = new StringXPathExpression(
            "utlatande/p:aktivitet/p2:ostruktureradtid[(parent::p:aktivitet/p:aktivitetskod/@code='AKT19')]/text()");

    public static final StringXPathExpression VARD_PA_SJUKHUS_VARDINRATTNING_XPATH = new StringXPathExpression(
            "utlatande/p:aktivitet/p2:plats[(parent::p:aktivitet/p:aktivitetskod/@code='AKT19')]");

    public static final StringXPathExpression OVRIG_BESKRIVNING_XPATH = new StringXPathExpression(
            "utlatande/p:kommentar");

    public static final String INTYG_AVSER_TEMPLATE = "utlatande/p2:intygAvser/@code = '%s'";

    public static final String ID_KONTROLL_TEMPLATE = "utlatande/p:vardkontakt/p:idKontroll/@code = '%s'";

    public static final String AKTIVITET_FOREKOMST_TEMPLATE = "utlatande/p:aktivitet/p:forekomst[parent::p:aktivitet/p:aktivitetskod/@code='%s'] = '%s'";

    public static final String AKTIVITET_BESKRIVNING_TEMPLATE = "utlatande/p:aktivitet/p:beskrivning[(parent::p:aktivitet/p:aktivitetskod/@code='%s')]";

    public static final String OBSERVATION_FOREKOMST_TEMPLATE = "utlatande/p:observation/p:forekomst[parent::p:observation/p:observationskod/@code='%s'] = '%s'";

    public static final String OBSERVATION_FOREKOMST_VARDE_TEMPLATE = "utlatande/p:observation/p:forekomst[parent::p:observation/p:observationskod/@code='%s']";

    public static final String OBSERVATION_VARDE_CODE_LATERALITET = "utlatande/p:observation/p:varde[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p2:lateralitet/@code='%s')]/@value";

    public static final String OBSERVATION_FOREKOMST_CODE_LATERALITET = "utlatande/p:observation/p:forekomst[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p2:lateralitet/@code='%s')]='true'";

    public static final String OBSERVATION_BESKRIVNING_TEMPLATE = "utlatande/p:observation/p:beskrivning[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:forekomst = 'true')] ";

    public static final String OBSERVATION_TID_TEMPLATE = "utlatande/p:observation/p:observationstid[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:forekomst = 'true')] ";

    public static final String REKOMMENDATION_VARDE_CODE_TEMPLATE = "utlatande/p:rekommendation/p2:varde/@code = '%s'";

    public static final String REKOMMENDATION_VARDE_BOOL_TEMPLATE = "utlatande/p:rekommendation/p2:varde[parent::p:rekommendation/p:rekommendationskod/@code='%s'] = '%s'";

    public static final String REKOMMENDATION_BESKRIVNING_TEMPLATE = "utlatande/p:rekommendation/p:beskrivning[parent::p:rekommendation/p:rekommendationskod/@code='%s']/text()";

    /**
     * Creates a {@link BooleanXPathExpression} from a string template and arguments.
     *
     * @param template
     *            The string template to use.
     * @param args
     *            The additional arguments to inject in the template.
     *
     * @return A boolean xPath expression.
     */
    public static BooleanXPathExpression booleanXPath(String template, Object... args) {
        return new BooleanXPathExpression(String.format(template, args));
    }

    /**
     * Creates a {@link StringXPathExpression} from a string template and arguments.
     *
     * @param template
     *            The string template to use.
     * @param args
     *            The additional arguments to inject in the template.
     *
     * @return A string xPath expression.
     */
    public static StringXPathExpression stringXPath(String template, Object... args) {
        return new StringXPathExpression(String.format(template, args));
    }
    
    public static DateXPathExpression dateXPath(String template, String dateFormat, Object... args) {
        return new DateXPathExpression(String.format(template, args), dateFormat);
    }
}
