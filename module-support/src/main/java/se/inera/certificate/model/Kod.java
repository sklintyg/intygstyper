package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public final class Kod {

    public static final int MAGIC_HASH_BASE = 31;
    private String codeSystem;
    private String codeSystemName;
    private String codeSystemVersion;
    private String code;
    private String displayName;

    public Kod() {
    }

    public Kod(String codeSystem, String codeSystemName, String codeSystemVersion, String code, String displayName) {
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
        this.code = code;
        this.displayName = displayName;
    }

    public Kod(String codeSystem, String codeSystemName, String codeSystemVersion, String code) {
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
        this.code = code;
    }

    public Kod(String code) {
        this.code = code;
    }

    public Kod(String codeSystem, String code) {
        this.codeSystem = codeSystem;
        this.code = code;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public void setCodeSystemName(String codeSystemName) {
        this.codeSystemName = codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public void setCodeSystemVersion(String codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Kod kod = (Kod) o;

        boolean result = true;
        if (code != null ? !code.equals(kod.code) : kod.code != null) {
            result = false;
        } else if (codeSystem != null ? !codeSystem.equals(kod.codeSystem) : kod.codeSystem != null) {
            result = false;
        } else if (codeSystemName != null ? !codeSystemName.equals(kod.codeSystemName) : kod.codeSystemName != null) {
            result = false;
        } else if (codeSystemVersion != null ? !codeSystemVersion.equals(kod.codeSystemVersion) : kod.codeSystemVersion != null) {
            result = false;
        }

        return result;
    }

    @Override
    public int hashCode() {
        int result = codeSystem != null ? codeSystem.hashCode() : 0;
        result = MAGIC_HASH_BASE * result + (codeSystemName != null ? codeSystemName.hashCode() : 0);
        result = MAGIC_HASH_BASE * result + (codeSystemVersion != null ? codeSystemVersion.hashCode() : 0);
        result = MAGIC_HASH_BASE * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
