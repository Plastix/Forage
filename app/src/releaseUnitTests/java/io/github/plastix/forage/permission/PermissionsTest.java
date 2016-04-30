package io.github.plastix.forage.permission;

import android.Manifest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import java.util.HashSet;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public final class PermissionsTest {

    /**
     * This list must be kept updated as new permissions/libraries are added!
     */
    private static final String[] EXPECTED_PERMISSIONS = {
            // App permissions
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            // Google Maps Permission
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    /**
     * Merged manifest location.
     * Only check the release manifest because the debug version might have debug libraries that
     * require special permissions that won't show up in the release.
     */
    private static final String MERGED_MANIFEST =
            "build/intermediates/manifests/full/release/AndroidManifest.xml";

    /**
     * Test to check if libraries are adding extra permissions to the app manifest.
     */
    @Test
    public void shouldMatchPermissions() {
        AndroidManifest manifest = new AndroidManifest(
                Fs.fileFromPath(MERGED_MANIFEST),
                null,
                null
        );

        assertThat(new HashSet<>(manifest.getUsedPermissions())).
                containsExactly((Object[]) EXPECTED_PERMISSIONS);
    }
}