package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public enum CertificateState {
    /** This code is unused */
    UNHANDLED,

    /** The intyg is arhcived in Mina Intyg ('arkiverad') */
    DELETED,

    /** The intyg is no longer archived in Mina Intyg */
    RESTORED,

    /** The intyg is 'makulerat' */
    CANCELLED,

    /** The intyg is sent to a recipient */
    SENT,

    /** The intyg was stored in Intygstjänsten */
    RECEIVED,

    /** This code is unused (except for some tests) */
    IN_PROGRESS,

    /** This code is unused (except for some tests) */
    PROCESSED
}
