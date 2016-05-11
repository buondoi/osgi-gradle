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
	private final List<String> modules;

	private Instructions(final Builder builder)
	{
		this.exports = builder.exports == null ? emptyList() : builder.exports;
		this.requires = builder.requires == null ? emptyList() : builder.requires;
		this.embeddedLibs = builder.embeddedLibs == null ? emptyList() : builder.embeddedLibs;
		this.modules = builder.modules == null ? emptyList() : builder.modules;
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

	public List<String> getModules()
	{
		return modules;
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
		private List<String> modules;

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

		public void setModules(final List<String> modules)
		{
			this.modules = modules;
		}

		public Instructions build()
		{
			return new Instructions(this);
		}
	}
}
