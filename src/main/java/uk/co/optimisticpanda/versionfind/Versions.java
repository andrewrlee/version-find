package uk.co.optimisticpanda.versionfind;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Versions {

	private Map<String, Version> versions;

	public Versions() {
		versions = new VersionCollector().collect().entrySet().stream()
				.collect(toMap(Entry::getKey, e -> new Version(e.getValue())));
	}
	
	public Map<String, Version> getVersions() {
		return versions;
	}

	public static class Version {
			
		private final String buildUser;
		private final String commit;
		private final String buildTime;

		private Version(Properties properties){
			this.buildUser = properties.getProperty("git.commit.user.email", "unknown");
			this.commit = properties.getProperty("git.commit.id", "unknown");
			this.buildTime = properties.getProperty("git.build.time", "unknown");
		}

		public String getBuildUser() {
			return buildUser;
		}

		public String getCommit() {
			return commit;
		}

		public String getBuildTime() {
			return buildTime;
		}

		@Override
		public String toString() {
			return "Version [buildUser=" + buildUser + ", commit=" + commit + ", buildTime=" + buildTime + "]";
		}
	}
}