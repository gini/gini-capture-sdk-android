package net.gini.android.capture.requirements;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.hardware.Camera;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

@RunWith(JUnit4.class)
public class CameraFlashRequirementTest {

    @Test
    public void should_reportUnfulfilled_ifFlash_isNotSupported() {
        OldCameraApiHolder cameraHolder = getCameraHolder(false);

        CameraFlashRequirement requirement = new CameraFlashRequirement(cameraHolder);

        assertThat(requirement.check().isFulfilled()).isFalse();
    }

    @Test
    public void should_reportFulfilled_ifFlash_isSupported() {
        OldCameraApiHolder cameraHolder = getCameraHolder(true);

        CameraFlashRequirement requirement = new CameraFlashRequirement(cameraHolder);

        assertThat(requirement.check().isFulfilled()).isTrue();
    }

    @Test
    public void should_reportUnfulfilled_ifCamera_isNotOpen() {
        OldCameraApiHolder cameraHolder = mock(OldCameraApiHolder.class);

        CameraFlashRequirement requirement = new CameraFlashRequirement(cameraHolder);

        assertThat(requirement.check().isFulfilled()).isFalse();
    }

    private OldCameraApiHolder getCameraHolder(boolean isFlashSupported) {
        OldCameraApiHolder cameraHolder = mock(OldCameraApiHolder.class);
        Camera.Parameters parameters = mock(Camera.Parameters.class);
        when(cameraHolder.getCameraParameters()).thenReturn(parameters);
        when(parameters.getSupportedFlashModes()).thenReturn(
                isFlashSupported ?
                        Collections.singletonList(Camera.Parameters.FLASH_MODE_ON)
                        : Collections.singletonList(Camera.Parameters.FLASH_MODE_OFF));

        return cameraHolder;
    }
}
