package com.padma.buildtool.osgi

import spock.lang.Specification

/**
 * Created on 5/9/16.
 */
class BundleManifestSpec extends Specification
{
    def 'create bundle manifest from manifest.yml file'()
    {
        setup:
        def url = BundleManifestSpec.class.getResource('/META-INF/manifest.yml')

        when:
        def manifest = BundleManifest.from(url)

        then:
        noExceptionThrown()
        manifest != null
    }
}
