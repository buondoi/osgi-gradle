package com.padma.buildtool.osgi;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


/**
 * Created on 5/9/16.
 */
public final class BundleManifest
{
	private final List<String> requires;
	private final List<String> exports;

	private BundleManifest(final Builder builder)
	{
		this.exports = builder.exports;
		this.requires = builder.requires;
	}

	public List<String> getRequires()
	{
		return requires;
	}

	public List<String> getExports()
	{
		return exports;
	}

	public static BundleManifest from(URL url) throws IOException
	{
		final Yaml yaml = new Yaml();
		try (final InputStream in = url.openStream())
		{
			final Builder builder = yaml.loadAs(in, Builder.class);
			return builder.build();
		}
	}

	public final class Builder
	{
		private List<String> exports;
		private List<String> requires;

		public void setExports(final List<String> exports)
		{
			this.exports = exports;
		}

		public void setRequires(final List<String> requires)
		{
			this.requires = requires;
		}

		public BundleManifest build()
		{
			return new BundleManifest(this);
		}
	}
}
