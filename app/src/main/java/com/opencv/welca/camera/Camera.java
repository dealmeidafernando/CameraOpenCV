package com.opencv.welca.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    JavaCameraView cameraJava;
    Mat imgRgba, imgGray;
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    cameraJava.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraJava = (JavaCameraView) findViewById(R.id.cameraJava);
        cameraJava.setVisibility(SurfaceView.VISIBLE);
        cameraJava.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (cameraJava!=null){
            cameraJava.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (cameraJava!=null){
            cameraJava.disableView();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else{
            Toast.makeText(getApplicationContext(),"OpenCV ERRO.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        imgRgba = new Mat(height, width, CvType.CV_8UC4);
        imgGray = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        imgRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        imgRgba = inputFrame.rgba();


        //Aplicando método para tornar imagem cinza.
        Imgproc.cvtColor(imgRgba,imgGray,Imgproc.COLOR_RGB2GRAY);

        return imgGray;
    }
}
