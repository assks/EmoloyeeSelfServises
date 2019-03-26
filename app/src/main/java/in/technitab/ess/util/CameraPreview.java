package in.technitab.ess.util;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    final String TAG = CameraPreview.class.getSimpleName();
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Activity mContext;
    private int cameraFaceId;

    // Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
    public CameraPreview(Activity context, Camera camera, int cameraFaceId) {
        super(context);
        mContext = context;
        this.cameraFaceId = cameraFaceId;
        this.mCamera = camera;
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (mCamera != null) {
                Log.d(TAG, "surface created ");
                mCamera.setDisplayOrientation(getAngleToRotate(cameraFaceId));
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            }

        } catch (IOException e) {
            // left blank for now
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
       /* mCamera.stopPreview();
        mCamera.release();
        mCamera = null;*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(getAngleToRotate(cameraFaceId));
            mCamera.startPreview();
            Log.d(TAG, "surface change");
        } catch (Exception e) {
            Log.d(TAG, " " + e.getMessage());
        }

    }

    public void resetCamera(Camera camera) {
        if (mCamera != null) {
            mCamera.release();
        }
        mCamera = camera;

    }

    public void startPreview(int cameraFaceId) {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(getAngleToRotate(cameraFaceId));
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public int getAngleToRotate(int cameraFace) {

        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraFace, info);
        int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;

        }

        Log.d("rotate ", "" + degrees+ " camera "+cameraFace);
        return result;
    }
}
