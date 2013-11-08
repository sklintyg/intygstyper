package se.inera.certificate.modules.rli.model.codes;

import se.inera.certificate.model.Kod;

class EmptyCodeSystem implements ICodeSystem {

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getCodeSystem() {
        return null;
    }

    @Override
    public String getCodeSystemName() {
        return null;
    }

    @Override
    public String getCodeSystemVersion() {
        return null;
    }

    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}