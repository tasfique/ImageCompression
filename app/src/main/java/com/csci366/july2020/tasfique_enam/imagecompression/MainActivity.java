package com.csci366.july2020.tasfique_enam.imagecompression;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView originalImage, modifiedImage;
    ImageView openFile, compressFile, decompressFile;
    TextView originalText, compressedText;
    final int PICK_IMAGE_REQUEST = 135;
    final int PICK_COMPRESSED_IMAGE_REQUEST = 357;
    Uri uri;
    String fileName;
    Bitmap inputBM, outputBM;
    boolean imageLoaded = false;
    int processStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        originalImage = findViewById(R.id.originalImage);
        modifiedImage = findViewById(R.id.modifiedImage);
        openFile = findViewById(R.id.openFile);
        compressFile = findViewById(R.id.compressFile);
        decompressFile = findViewById(R.id.decompressFile);
        originalText = findViewById(R.id.originalTextView);
        compressedText = findViewById(R.id.compressedTextView);

        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent();
                    // Show only images, no videos or anything else
                    i.setType("*/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    // Always show the chooser (if there are multiple options available)
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

//        openFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent();
//                // Show only images, no videos or anything else
//                i.setType("*/*");
//                i.setAction(Intent.ACTION_GET_CONTENT);
//                // Always show the chooser (if there are multiple options available)
//                startActivityForResult(Intent.createChooser(i, "Select File"), PICK_COMPRESSED_IMAGE_REQUEST);
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // WHEN A USER CHOOSES AN ORIGINAL IMAGE
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            InputStream inputFile;
            float fileSize = 0;
            try {
                inputFile = getContentResolver().openInputStream(uri);
                //get file size and convert to kB
                fileSize = (float)inputFile.available() / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //get last part of path (file name)
            int index = uri.getLastPathSegment().lastIndexOf('/');
            fileName = uri.getLastPathSegment().substring(index+1);
            originalText.setText(uri.getLastPathSegment().substring(index+1) + "\nImage Original Size: " + String.format("%.02f",fileSize) + " kB");
            originalText.setVisibility(View.VISIBLE);

            originalImage.setVisibility(View.VISIBLE);
            modifiedImage.setVisibility(View.INVISIBLE);
            //compressFile.setImageResource(R.drawable.compress_icon);
            originalText.setText(""); //compressedTextView
            compressedText.setText("Compress"); //processtextview
            compressedText.setVisibility(View.VISIBLE); //processtextview
            compressFile.setVisibility(View.VISIBLE); //processbutton

            try {
                inputBM = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                originalImage.setImageBitmap(inputBM);
                imageLoaded = true;
                processStatus = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // WHEN A USER CHOOSES A CSZ COMPRESSED FILE
        else if (requestCode == PICK_COMPRESSED_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            InputStream inputFile;
            float fileSize = 0;
            try {
                inputFile = getContentResolver().openInputStream(uri);
                //get file size and convert to kB
                fileSize = (float)inputFile.available() / 1024;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //get last part of path (file name)
            int index = uri.getLastPathSegment().lastIndexOf('/');
            fileName = uri.getLastPathSegment().substring(index+1);
            originalText.setText(uri.getLastPathSegment().substring(index+1) + "\nImage Compressed Size: " + String.format("%.02f",fileSize) + " kB");
            originalText.setVisibility(View.VISIBLE);

            modifiedImage.setVisibility(View.INVISIBLE);
            //processButton.setImageResource(R.drawable.decompress_icon);
            //processedTextView.setText("");
            //processTextView.setText("Decompress");
            //processTextView.setVisibility(View.VISIBLE);
           //processButton.setVisibility(View.VISIBLE);

            imageLoaded = true;
            processStatus = 2;
        }
    }

    public void compressImage() {
        // CONVERT RGB TO YCrCb
        int originalWidth = inputBM.getWidth();
        int originalHeight = inputBM.getHeight();
        int halfWidth = (int)(Math.ceil(inputBM.getWidth() / 2.0));
        int halfHeight = (int)(Math.ceil(inputBM.getHeight() / 2.0));
        YCrCbImage originalYCrCbImage = new YCrCbImage(originalWidth, originalHeight);
        YCrCbImage subsampledImage = new YCrCbImage(originalWidth, originalHeight, halfWidth, halfHeight, halfWidth, halfHeight);
        originalYCrCbImage = convertToYCrCb(originalYCrCbImage);

        // CHROMA SUBSAMPLING 4:2:0
        subsampledImage = chromaSubsampling(originalYCrCbImage, subsampledImage);

        // DISPLAY IMAGE
        displayImage(subsampledImage);

        // QUANTIZE
//        QuantizationImage quantizeImage = quantizeImage(subsampledImage);

        // SAVE AS CSZ FILE INTO YOUR ANDROID EMULATOR (DCIM)
//        saveAsCszFile(quantizeImage);
    }

    public void decompressImage() {
        // READ CSZ FILE FROM DCIM


        // DEQUANTIZE


        // UPSAMPLE FROM 4:2:0 TO 4:4:4


        // CONVERT YCrCb to RGB


        // DISPLAY IMAGE


        // CALCULATE MSE (Mean Square Error):
        // You should calculate the differences between Original RGB values and decompressed RGB values.
        // R=100 G=110 B=120 (ORIGINAL) / R=110 G=120= B=130 (DECOMPRESSED)
        // (110 - 100) ^ 2 + (120-110) ^ 2 + (130-120) ^ 2 = 300 (MSE)
    }

    public YCrCbImage convertToYCrCb(YCrCbImage image) {
        for (int i=0; i<inputBM.getWidth();i++)
        {
            for (int j=0;j<inputBM.getHeight();j++)
            {
                int p = inputBM.getPixel(i,j);
                float r = (float) Color.red(p);
                float g = (float) Color.green(p);
                float b = (float)Color.blue(p);

                image.Y[j][i] = (byte) ( 0.299 * r + 0.587* g + 0.114* b);
                image.Cb[j][i] = (byte)((-0.169 * r) + (-0.331)* g + 0.500 * b) ;
                image.Cr[j][i] = (byte)(( 0.500 * r) + (-0.419) * g +(-0.081) * b);

            }
        }
        return image;
    }

    public YCrCbImage chromaSubsampling(YCrCbImage original_image, YCrCbImage subsampled_image) {
        for (int i=0; i<inputBM.getWidth(); i++) {
            for (int j=0; j<inputBM.getHeight(); j++) {
                subsampled_image.Y[i][j] = original_image.Y[i][j];
            }
        }
        int heightCount = -1;
        int widthCount = 0;
        for(int i = 0; i < inputBM.getHeight() / 2; i += 2) {
            heightCount++;
            widthCount = 0;
            for (int j = 0; j < inputBM.getWidth() / 2; j += 2) {
                subsampled_image.Cb[heightCount][widthCount] =
                        (byte) Math.round((original_image.Cb[i][j] +
                                original_image.Cb[i+1][j] +
                                original_image.Cb[i][j+1] +
                                original_image.Cb[i+1][j+1]) / 4);
                subsampled_image.Cr[heightCount][widthCount] =
                        (byte) Math.round((original_image.Cr[i][j] +
                                original_image.Cr[i+1][j] +
                                original_image.Cr[i][j+1] +
                                original_image.Cr[i+1][j+1]) / 4);
            }
        }
        return subsampled_image;
    }

    public void displayImage(YCrCbImage image) {
        outputBM = Bitmap.createBitmap(inputBM.getWidth(), inputBM.getHeight(), inputBM.getConfig());
        for (int i=0; i<inputBM.getWidth(); i++)
        {
            int r=0,g=0,b=0;

            for (int j=0;j<inputBM.getHeight();j++)
            {
                float Y = image.Y[j][i];
                float Cr = image.Cr[j/2][i/2];
                float Cb = image.Cb[j/2][i/2];
                if(Y < 0){
                    Y += 256;
                }

                r = (int)(Y + 1.402 * (Cr));
                g = (int)(Y - 0.34414 * (Cb) - 0.71414 * (Cr));
                b = (int)(Y + 1.772  * (Cb));

                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));

                outputBM.setPixel(i,j, Color.argb(255, r, g, b));
            }
        }
        modifiedImage.setImageBitmap(outputBM);
        modifiedImage.setVisibility(View.VISIBLE);
    }


}