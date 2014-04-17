package se.inera.certificate.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class LogMarkers {

    private LogMarkers() {
    }

    public static final Marker VALIDATION = MarkerFactory.getMarker("Validation");
    public static final Marker MONITORING = MarkerFactory.getMarker("Monitoring");
}
