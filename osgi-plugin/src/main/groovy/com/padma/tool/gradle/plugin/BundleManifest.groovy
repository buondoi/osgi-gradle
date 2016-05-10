package com.padma.tool.gradle.plugin

import com.padma.buildtool.osgi.manifest.ManifestBuilder
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.plugins.osgi.DefaultOsgiManifest
import org.gradle.api.java.archives.internal.DefaultManifest
import org.gradle.util.WrapUtil

import java.util.jar.Attributes
import java.util.jar.Manifest

/**
 * Created on 5/10/16.
 */
class BundleManifest extends DefaultOsgiManifest
{
    final ManifestBuilder manifestBuilder

    BundleManifest(final FileResolver fileResolver, final ManifestBuilder manifestBuilder)
    {
        super(fileResolver)
        this.manifestBuilder = manifestBuilder;
    }

    @Override
    DefaultManifest getEffectiveManifest()
    {
        final Manifest manifest = manifestBuilder.build()

        final DefaultManifest effectiveManifest = new DefaultManifest(null);
        final Attributes attributes = manifest.getMainAttributes();
        for (Map.Entry<Object, Object> entry : attributes.entrySet())
        {
            effectiveManifest.attributes(WrapUtil.toMap(entry.getKey().toString(), (String) entry.getValue()));
        }

        return getEffectiveManifestInternal(effectiveManifest);
    }
}
