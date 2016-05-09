![](https://travis-ci.org/buondoi/osgi-gradle.svg?branch=master)
# Why
* Support OSGI instructions in Yaml format.
* Define OSGI instructions in a separated file so it can be used by build tool (Gradle) or integration test framework.

# How to use
OSGI instructions are declared in **META-INF/manifest.yml** file.

Supported instructions
* exports: export package instruction
* requires: import package instruction
* embeddedLibs: bundle classpath instruction

