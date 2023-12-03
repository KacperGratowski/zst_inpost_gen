package com.kacpergratowski.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText codeEditText;
    private ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        codeEditText = findViewById(R.id.codeEditText);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        Button generateQRButton = findViewById(R.id.generateQRButton);
        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });

 ;

        Button shareQRButton = findViewById(R.id.shareQRButton);
        shareQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode();
            }
        });
    }

    private void generateQRCode() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String code = codeEditText.getText().toString().trim();

        if (phoneNumber.isEmpty() || code.isEmpty()) {
            Toast.makeText(this, "Wprowadź numer telefonu i kod", Toast.LENGTH_SHORT).show();
            return;
        }

        String qrText = "P|" + phoneNumber + "|" + code;

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrText, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd generowania kodu QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQRCode() {
        qrCodeImageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = qrCodeImageView.getDrawingCache();

        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "QRCode.png");

        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            Toast.makeText(this, "Kod QR zapisany", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd zapisu kodu QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQRCode() {
        qrCodeImageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = qrCodeImageView.getDrawingCache();

        try {
            File file = new File(this.getExternalCacheDir(), "QRCode.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, file);
            startActivity(Intent.createChooser(intent, "Udostępnij kod QR"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd udostępniania kodu QR", Toast.LENGTH_SHORT).show();
        }
    }
}
