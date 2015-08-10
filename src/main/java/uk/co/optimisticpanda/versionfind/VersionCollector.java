package uk.co.optimisticpanda.versionfind;

import static java.util.Collections.emptyMap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.util.ClasspathHelper;
import org.reflections.util.FilterBuilder;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.Vfs.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;

public class VersionCollector {

	private static final Logger L = LoggerFactory.getLogger(VersionCollector.class);
	private static final String FILENAME_REGEXP = "git-(.*)\\.properties";
	private static final Pattern FILENAME_REGEXP_PATTERN = Pattern.compile(FILENAME_REGEXP);
	private static final String VERSION_LOCATION = "META-INF/version";

	public VersionCollector() {
	}
	
	public Map<String, Properties> collect() {
		Predicate<String> filterBuilder = new FilterBuilder().include(FILENAME_REGEXP);

		Collection<URL> urls = ClasspathHelper.forPackage(VERSION_LOCATION);
		if (urls.isEmpty()) {
			return emptyMap();
		}

		Iterable<File> files = Vfs.findFiles(urls, VERSION_LOCATION, filterBuilder);
		Map<String, Properties> result = new HashMap<>();
		for (final Vfs.File file : files) {
			loadProperties(file).ifPresent(properties -> 
				result.put(moduleName(file), properties));
		}
		return result;
	}

	private Optional<Properties> loadProperties(File file) {
		try (InputStreamReader reader = new InputStreamReader(file.openInputStream(), Charsets.UTF_8)) {
			Properties properties = new Properties();
			properties.load(file.openInputStream());
			return Optional.of(properties);
		} catch (IOException e) {
			L.error("Problem loading file: {}", e.getMessage(), e);
			return Optional.empty();
		}
	}

	private String moduleName(File file) {
		Matcher matcher = FILENAME_REGEXP_PATTERN.matcher(file.getName());
		if (!matcher.find()) {
			throw new IllegalStateException("Should find module in " + file.getName());
		}
		return matcher.group(1);
	}
}
