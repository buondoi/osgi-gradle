package com.padma.buildtool.osgi.manifest;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static java.util.Collections.emptyList;


/**
 * Created on 5/9/16.
 */
public final class Instructions
{
	private final List<String> requires;
	private final List<String> exports;
	private final List<String> embeddedLibs;

	private Instructions(final Builder builder)
	{
		this.exports = builder.exports == null ? emptyList() : builder.exports;
		this.requires = builder.requires == null ? emptyList() : builder.requires;
		this.embeddedLibs = builder.embeddedLibs == null ? emptyList() : builder.embeddedLibs;
	}

	public List<String> getRequires()
	{
		return requires;
	}

	public List<String> getExports()
	{
		return exports;
	}

	public List<String> getEmbeddedLibs()
	{
		return embeddedLibs;
	}

	public static Instructions fromYml(URL url) throws IOException
	{
		final Yaml yaml = new Yaml();
		try (final InputStream in = url.openStream())
		{
			final Builder builder = yaml.loadAs(in, Builder.class);
			return builder.build();
		}
	}

	public static final class Builder
	{
		private List<String> exports;
		private List<String> requires;
		private List<String> embeddedLibs;

		public void setExports(final List<String> exports)
		{
			this.exports = exports;
		}

		public void setRequires(final List<String> requires)
		{
			this.requires = requires;
		}

		public void setEmbeddedLibs(List<String> embeddedLibs)
		{
			this.embeddedLibs = embeddedLibs;
		}

		public Instructions build()
		{
			return new Instructions(this);
		}
	}
}
