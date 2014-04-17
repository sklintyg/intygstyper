/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.pdf;

/**
 * @author erik
 */
public class PdfGeneratorException extends Exception {

    private static final long serialVersionUID = 1L;

    public PdfGeneratorException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message message
     */
    public PdfGeneratorException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause cause
     */
    public PdfGeneratorException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message message
     * @param cause cause
     */
    public PdfGeneratorException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message message
     * @param cause cause
     * @param enableSuppression enableSuppression
     * @param writableStackTrace writableStackTrace
     */
    public PdfGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
