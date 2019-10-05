package ua.com.sipsoft.utils.security;

import lombok.Getter;

/**
 *
 * @author Pavlo Degtyaryev
 */

public enum VerificationTokenType {

    // Warning! Sequence matters for safety. Higher suits have lower numbers.
    REGNEWUSER("RNU"),
    FORGOTPASS("FGP");

    @Getter
    private final String shortName;

    /**
     * @param string
     */
    private VerificationTokenType(String shortName) {
	this.shortName = shortName;
    }

    public static VerificationTokenType fromShortName(String shortName) {
	for (VerificationTokenType type : VerificationTokenType.values()) {
	    if (type.getShortName().equals(shortName)) {
		return type;
	    }
	}
	throw new UnsupportedOperationException(
		"The VerificationTokenType short name " + shortName + " is not supported!");
    }

}
