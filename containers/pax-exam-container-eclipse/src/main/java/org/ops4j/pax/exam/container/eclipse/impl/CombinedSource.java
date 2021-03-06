/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.container.eclipse.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.ops4j.pax.exam.container.eclipse.ArtifactNotFoundException;
import org.ops4j.pax.exam.container.eclipse.EclipseArtifactSource;
import org.ops4j.pax.exam.container.eclipse.EclipseBundleOption;
import org.ops4j.pax.exam.container.eclipse.EclipseFeatureOption;
import org.ops4j.pax.exam.container.eclipse.EclipseInstallableUnit;
import org.ops4j.pax.exam.container.eclipse.EclipseOptions.CombinedEclipseArtifactSource;
import org.ops4j.pax.exam.container.eclipse.EclipseProject;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;

/**
 * Combines basic {@link EclipseArtifactSource}s base don the givne types
 * 
 * @author Christoph Läubrich
 *
 */
public final class CombinedSource implements CombinedEclipseArtifactSource {

    // TODO make this more useful and compact, e.g. using reflection, so we can choose what
    // interfaces are implemented, and only throw the exception with suppressed exception if we have
    // more than one source

    private final EclipseArtifactSource[] sources;

    public CombinedSource(Collection<EclipseArtifactSource> sources) {
        this.sources = sources.toArray(new EclipseArtifactSource[0]);
    }

    @Override
    public EclipseBundleOption bundle(String bundleName) throws IOException, FileNotFoundException {
        return bundle(bundleName, Version.emptyVersion);
    }

    @Override
    public EclipseBundleOption bundle(String bundleSymbolicName, Version bundleVersion)
        throws IOException, ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException(
            "bundle " + bundleSymbolicName + ":" + bundleVersion + " not found in any sources");
        EclipseBundleOption bundle = null;
        for (EclipseArtifactSource source : sources) {
            // find the highest version
            try {
                if (source instanceof EclipseBundleSource) {
                    EclipseBundleOption sourcebundle = ((EclipseBundleSource) source)
                        .bundle(bundleSymbolicName, bundleVersion);
                    if (bundle == null
                        || sourcebundle.getVersion().compareTo(bundle.getVersion()) > 0) {
                        bundle = sourcebundle;
                    }
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        if (bundle != null) {
            return bundle;
        }
        throw fnfe;
    }

    @Override
    public EclipseBundleOption bundle(String bundleSymbolicName, VersionRange bundleVersionRange)
        throws IOException, ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException("bundle "
            + bundleSymbolicName + ":" + bundleVersionRange + " not found in any sources");
        EclipseBundleOption bundle = null;
        for (EclipseArtifactSource source : sources) {
            // find the highest version
            try {
                if (source instanceof EclipseBundleSource) {
                    EclipseBundleOption sourcebundle = ((EclipseBundleSource) source)
                        .bundle(bundleSymbolicName, bundleVersionRange);
                    if (bundle == null
                        || sourcebundle.getVersion().compareTo(bundle.getVersion()) > 0) {
                        bundle = sourcebundle;
                    }
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        if (bundle != null) {
            return bundle;
        }
        throw fnfe;
    }

    @Override
    public EclipseFeatureOption feature(String featureName) throws IOException {
        return feature(featureName, Version.emptyVersion);
    }

    @Override
    public EclipseFeatureOption feature(String featureName, Version featureVersion)
        throws IOException, ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException(
            "feature " + featureName + ":" + featureVersion + " not found in any sources");
        EclipseFeatureOption feature = null;
        for (EclipseArtifactSource source : sources) {
            try {
                if (source instanceof EclipseFeatureSource) {
                    EclipseFeatureOption sourcefeature = ((EclipseFeatureSource) source)
                        .feature(featureName, featureVersion);
                    if (feature == null
                        || sourcefeature.getVersion().compareTo(feature.getVersion()) > 0) {
                        feature = sourcefeature;
                    }
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        if (feature != null) {
            return feature;
        }
        throw fnfe;
    }

    @Override
    public EclipseFeatureOption feature(String featureName, VersionRange featureVersionRange)
        throws IOException, ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException(
            "feature " + featureName + ":" + featureVersionRange + " not found in any sources");
        EclipseFeatureOption feature = null;
        for (EclipseArtifactSource source : sources) {
            try {
                if (source instanceof EclipseFeatureSource) {
                    EclipseFeatureOption sourcefeature = ((EclipseFeatureSource) source)
                        .feature(featureName, featureVersionRange);
                    if (feature == null
                        || sourcefeature.getVersion().compareTo(feature.getVersion()) > 0) {
                        feature = sourcefeature;
                    }
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        throw fnfe;
    }

    @Override
    public EclipseProject project(String projectName) throws ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException(
            "project " + projectName + " not found in any sources");
        for (EclipseArtifactSource source : sources) {
            try {
                if (source instanceof EclipseProjectSource) {
                    return ((EclipseProjectSource) source).project(projectName);
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        throw fnfe;
    }

    @Override
    public EclipseInstallableUnit unit(String id) throws IOException, ArtifactNotFoundException {
        return unit(id, Version.emptyVersion);
    }

    @Override
    public EclipseInstallableUnit unit(String id, Version version)
        throws IOException, ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException(
            "unit " + id + ":" + version + " not found in any sources");
        EclipseInstallableUnit unit = null;
        for (EclipseArtifactSource source : sources) {
            try {
                if (source instanceof EclipseUnitSource) {
                    EclipseInstallableUnit sourceunit = ((EclipseUnitSource) source).unit(id,
                        version);
                    if (unit == null || sourceunit.getVersion().compareTo(unit.getVersion()) > 0) {
                        unit = sourceunit;
                    }
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        if (unit != null) {
            return unit;
        }
        throw fnfe;
    }

    @Override
    public EclipseInstallableUnit unit(String id, VersionRange versionRange)
        throws IOException, ArtifactNotFoundException {
        ArtifactNotFoundException fnfe = new ArtifactNotFoundException(
            "unit " + id + ":" + versionRange + " not found in any sources");
        EclipseInstallableUnit unit = null;
        for (EclipseArtifactSource source : sources) {
            try {
                if (source instanceof EclipseUnitSource) {
                    EclipseInstallableUnit sourceunit = ((EclipseUnitSource) source).unit(id,
                        versionRange);
                    if (unit == null || sourceunit.getVersion().compareTo(unit.getVersion()) > 0) {
                        unit = sourceunit;
                    }
                }
            }
            catch (ArtifactNotFoundException ef) {
                fnfe.addSuppressed(ef);
            }
        }
        if (unit != null) {
            return unit;
        }
        throw fnfe;
    }

    @Override
    public Collection<EclipseInstallableUnit> getAllUnits() throws IOException {
        ArrayList<EclipseInstallableUnit> result = new ArrayList<>();
        for (EclipseArtifactSource source : sources) {
            if (source instanceof EclipseUnitSource) {
                result.addAll(((EclipseUnitSource) source).getAllUnits());
            }
        }
        return result;
    }
}
