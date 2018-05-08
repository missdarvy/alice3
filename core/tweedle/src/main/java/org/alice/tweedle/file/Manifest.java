package org.alice.tweedle.file;


import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class Manifest {
	private static final String DEFAULT_ICON = "thumbnail.png";
	private static final String DEFAULT_VERSION = "1.0";

	public Description description = new Description();
	public Provenance provenance = new Provenance();
	public MetaData metadata = new MetaData();
	public List<ProjectIdentifier> prerequisites = new ArrayList<>();
	public List<ResourceReference> resources = new ArrayList<>();

	public String getId() {
		return metadata.identifier.id;
	}

	public String getName() {
		return description.name;
	}

	public static class Description {
		public String name;
		public String icon = DEFAULT_ICON;
		public List<String> tags = new ArrayList<>();
		public List<String> groupTags = new ArrayList<>();
		public List<String> themeTags = new ArrayList<>();
	}

	public static class Provenance {
		public String aliceVersion;
		public String creationYear = Year.now().toString();
		public String creator = "Anonymous";
	}

	public static class MetaData {
		public String formatVersion;
		public ProjectIdentifier identifier = new ProjectIdentifier();
	}

	public static class ProjectIdentifier {
		public String id;
		public String version = DEFAULT_VERSION;
		public ProjectType type;
	}

	public enum ProjectType
	{
		Library, World, Model
	}
}
