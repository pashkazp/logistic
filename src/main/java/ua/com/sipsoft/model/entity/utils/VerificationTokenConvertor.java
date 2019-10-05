package ua.com.sipsoft.model.entity.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ua.com.sipsoft.utils.security.VerificationTokenType;

@Converter
public class VerificationTokenConvertor implements AttributeConverter<VerificationTokenType, String> {

    @Override
    public String convertToDatabaseColumn(VerificationTokenType token) {
	if (token == null) {
	    return null;
	}
	return token.getShortName();
    }

    @Override
    public VerificationTokenType convertToEntityAttribute(String tokenShortName) {
	if (tokenShortName == null) {
	    return null;
	}
	return VerificationTokenType.fromShortName(tokenShortName);
    }

}
