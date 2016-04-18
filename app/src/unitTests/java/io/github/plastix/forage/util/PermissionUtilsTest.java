package io.github.plastix.forage.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class PermissionUtilsTest {

    @Test
    public void isPermissionRequestCancelled_shouldBeCancelled() {
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{})).isTrue();
    }

    @Test
    public void isPermissionRequestCancelled_isNotCancelled() {
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{-1, -1})).isFalse();
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{0, 0})).isFalse();
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{0, -1})).isFalse();
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{-1, 0})).isFalse();
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{-1})).isFalse();
        assertThat(PermissionUtils.isPermissionRequestCancelled(new int[]{0})).isFalse();


    }

    @Test
    public void hasAllPermissionsGranted_allGranted() {
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{0})).isTrue();
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{0, 0})).isTrue();
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{0, 0, 0})).isTrue();
    }


    @Test
    public void hasAllPermissionsGranted_notAllGranted() {
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{})).isFalse();
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{-1})).isFalse();
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{0, -1})).isFalse();
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{0, -1, 0})).isFalse();
        assertThat(PermissionUtils.hasAllPermissionsGranted(new int[]{-1, -1, 0})).isFalse();
    }
}
