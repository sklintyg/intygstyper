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
package se.inera.intyg.intygstyper.fkparent.pdf.eventhandlers;

/**
 * Extension of the generic FkAbstractPersonnummerEventHandler, overriding the positioning an on which pages to show the
 * personnummer on overflows pages.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkOverflowPagePersonnummerEventHandlerImpl extends FkAbstractPersonnummerEventHandler {
    public FkOverflowPagePersonnummerEventHandlerImpl(String personnummer) {
        super(personnummer);
    }

    @Override
    protected int getActiveFromPage() {
        return 5;
    }

    @Override
    protected int getActiveToPage() {
        return 999;
    }

    @Override
    protected float getXOffset() {
        return 170f;
    }

    @Override
    protected float getYOffset() {
        return 18f;
    }
}
