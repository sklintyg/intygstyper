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
package se.inera.certificate.modules.rli.json;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.integration.json.TrimmingStringSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;

public class RLICustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -3291211792669869270L;

    public RLICustomObjectMapper() {
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        registerModule(new RLIModule());
    }

    private static final class RLIModule extends SimpleModule {

        private static final long serialVersionUID = -5095620428675230583L;

        public RLIModule() {
            addSerializer(String.class, new TrimmingStringSerializer());

            addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

            addSerializer(LocalDate.class, new LocalDateSerializer());
            addDeserializer(LocalDate.class, new LocalDateDeserializer());
        }

    }
}
