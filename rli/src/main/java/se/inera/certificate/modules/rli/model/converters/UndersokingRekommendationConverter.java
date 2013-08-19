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
package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utlatande;

public class UndersokingRekommendationConverter {
	
	private static Logger LOG = LoggerFactory.getLogger(UndersokingRekommendationConverter.class);

	public void populateUndersokning(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande,
			Utlatande intUtlatande) {
	
	}
	
	public void populateUndersokningRekommendation(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande,
			Utlatande intUtlatande) {
		
		List<Observation> observations = extUtlatande.getObservationer();
		
		Observation obs = null;
		
		obs = (Observation) CollectionUtils.find(observations, new ObservationsKodPredicate(ObservationsKod.SJUKDOM));
		
		if (obs != null) {
			intUtlatande.setOrsakForAvbokning(OrsakAvbokning.RESENAR_SJUK);
			handleSjukdom(obs, extUtlatande, intUtlatande);
			return;
		}
		
		obs = (Observation) CollectionUtils.find(observations, new ObservationsKodPredicate(ObservationsKod.GRAVIDITET));
		
		if (obs != null) {
			intUtlatande.setOrsakForAvbokning(OrsakAvbokning.RESENAR_GRAVID);
			handleGraviditet(obs, extUtlatande, intUtlatande);
			return;
		}
		
		LOG.error("No observations found");
	}

	public void populateFromAktiviteter(List<Aktivitet> aktiviteter, Undersokning intUndersokning) {
		
		LOG.debug("Handling aktiviteter");
		
		List<Aktivitet> filteredAktiviteter = filterAktiviteter(aktiviteter, AktivitetsKod.KLINISK_UNDERSOKNING);
		
		if (filteredAktiviteter == null || filteredAktiviteter.isEmpty()) {
			LOG.debug("No aktiviteter found");
		}
		
		if (aktiviteter.size() == 1) {
//			Aktivitet aktivitet = filteredAktiviteter.get(0);
			
			
		}
		
		// get oldest aktivitet
		Comparator<Aktivitet> comp = new Comparator<Aktivitet>() {
			@Override
			public int compare(Aktivitet a1, Aktivitet a2) {
				// TODO: get date to compare with
				return 0;
			}
		};
		
		// get oldest aktiviet and populate f
		Aktivitet oldestAktivitet = Collections.max(filteredAktiviteter, comp);
		
		//intUndersokning.setForstaUndersokningDatum(oldestAktiviet.getDatum);
		intUndersokning.setForstaUndersokningPlats(oldestAktivitet.getBeskrivning());
		
		
	}
	

	private List<Aktivitet> filterAktiviteter(List<Aktivitet> aktiviteter, AktivitetsKod aktivitetsKod) {
		
		LOG.debug("Got list with {} aktiviteter", aktiviteter.size());
		
		List<Aktivitet> filteredAktivieter = new ArrayList<Aktivitet>();
		
		CollectionUtils.select(aktiviteter, new AktivitetPredicate(aktivitetsKod), filteredAktivieter);
		
		LOG.debug("Filtered on {} and got {} aktiviteter", aktivitetsKod.name(), filteredAktivieter.size());
		
		return filteredAktivieter;
	}

	private void handleGraviditet(
			Observation obs,
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande,
			Utlatande intUtlatande) {
		
		LOG.debug("Handling pregnancy observations");
		
				
	}


	private void handleSjukdom(
			Observation obs,
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande,
			Utlatande intUtlatande) {
		
		LOG.debug("Handling sickness observations");
		
	}
	
	
	private class AktivitetPredicate implements Predicate {

		private AktivitetsKod aktivitetsKodEnum;
		
		public AktivitetPredicate(AktivitetsKod aktivitetsKod) {
			this.aktivitetsKodEnum = aktivitetsKod;
		}

		@Override
		public boolean evaluate(Object obj) {
			
			if (!(obj instanceof Aktivitet)) {
				return false;
			}
			
			Aktivitet aktivitet = (Aktivitet) obj;
			Kod aktivitetskod = aktivitet.getAktivitetskod();
						
			return (aktivitetskod.getCode().equals(aktivitetsKodEnum.getCode()));
		}
		
	}

	private class ObservationsKodPredicate implements Predicate {
		
		private ObservationsKod obsKodEnum;
		
		public ObservationsKodPredicate(ObservationsKod obsKodEnum) {
			this.obsKodEnum = obsKodEnum;
		} 
				
		@Override
		public boolean evaluate(Object obj) {
			
			if (!(obj instanceof Observation)) {
				return false;
			}
			
			Observation obs = (Observation) obj;
			Kod obsKod = obs.getObservationskod();
						
			return (obsKod.getCode().equals(obsKodEnum.getCode()));
		}
		
	}	
}
