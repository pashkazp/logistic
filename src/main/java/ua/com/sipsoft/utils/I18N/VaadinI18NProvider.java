package ua.com.sipsoft.utils.I18N;

import static java.util.ResourceBundle.getBundle;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.success;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link I18NProvider} for Application
 *
 * @author Pavlo Degtyaryev
 */
@SpringComponent
@Slf4j
public class VaadinI18NProvider implements I18NProvider {

	private static final String CANNOT_FIND_TRANSLATIONS_FILE = " cannot find translations file.. ";

	private static final long serialVersionUID = -2902521256418298973L;

	private static final String BUNDLE_PREFIX = "translate";

	private static Locale localeEN;
	private static Locale localeUK;
	private static Locale localeRU;
	private static Locale localeForced;
	private List<Locale> locales;

	private static ResourceBundle resourceBundleEN;
	private static ResourceBundle resourceBundleUK;
	private static ResourceBundle resourceBundleRU;
	private static ResourceBundle resourceBundleForced;

	/**
	 * Construct I18N provider. If parameter localeForcedProp is not null try to
	 * force translations to specified language
	 * 
	 * @param localeForcedProp
	 */
	public VaadinI18NProvider(@Value("${ua.com.sipsoft.default-locale:}") String localeForcedProp) {
		log.info(VaadinI18NProvider.class.getSimpleName() + " was found..");
		localeEN = Locale.ENGLISH;
		localeUK = Locale.forLanguageTag("uk-UA");
		localeRU = Locale.forLanguageTag("ru-RU");
		locales = new ArrayList<>();

		if (localeForcedProp != null && !localeForcedProp.isBlank()) {
			localeForced = Locale.forLanguageTag(localeForcedProp);
			try {
				resourceBundleForced = getBundle(BUNDLE_PREFIX, localeForced, new ResourceBundle.Control() {
					@Override
					public List<Locale> getCandidateLocales(String s, Locale locale) {
						return Arrays.asList(localeForced, Locale.ROOT);
					}
				});
				locales.add(localeForced);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(VaadinI18NProvider.class.getSimpleName() + CANNOT_FIND_TRANSLATIONS_FILE + e);
			}
		}

		try {
			resourceBundleEN = getBundle(BUNDLE_PREFIX, localeEN, new ResourceBundle.Control() {
				@Override
				public List<Locale> getCandidateLocales(String s, Locale locale) {
					return Arrays.asList(Locale.ENGLISH, Locale.ROOT);
				}
			});
			locales.add(localeEN);
		} catch (Exception e) {
			log.error(VaadinI18NProvider.class.getSimpleName() + CANNOT_FIND_TRANSLATIONS_FILE + e);
		}
		try {
			resourceBundleUK = getBundle(BUNDLE_PREFIX, localeUK);
			locales.add(localeUK);
		} catch (Exception e) {
			log.error(VaadinI18NProvider.class.getSimpleName() + CANNOT_FIND_TRANSLATIONS_FILE + e);
		}
		try {
			resourceBundleRU = getBundle(BUNDLE_PREFIX, localeRU);
			locales.add(localeRU);
		} catch (Exception e) {
			log.error(VaadinI18NProvider.class.getSimpleName() + CANNOT_FIND_TRANSLATIONS_FILE + e);
		}
		locales = Collections.unmodifiableList(locales);
	}

	@Override
	public List<Locale> getProvidedLocales() {
		log.info("VaadinI18NProvider getProvidedLocales..");
		return locales;
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
//		log.info("VaadinI18NProvider getTranslation.. key : " + key + " - " + locale);
		return match(
				matchCase(() -> success(resourceBundleEN)),
				matchCase(() -> resourceBundleForced != null, () -> success(resourceBundleForced)),
				matchCase(() -> localeUK.equals(locale), () -> success(resourceBundleUK)),
				matchCase(() -> localeRU.equals(locale), () -> success(resourceBundleRU)))
						.map(resourceBundle -> {
							if (!resourceBundle.containsKey(key))
								log.info("missing ressource key (i18n) " + key);
//							log.info(resourceBundle.getBaseBundleName() + " locale " + locale + ": '" + key + "' - "
//									+ ((resourceBundle.containsKey(key)) ? resourceBundle.getString(key) : key));
							return (resourceBundle.containsKey(key)) ? resourceBundle.getString(key) : key;

						})
						.getOrElse(() -> key + " - " + locale);
	}

}
