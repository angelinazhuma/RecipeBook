package com.example.demo.authorization.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {

  public String generateQRCode(String text) {
    try {
      QRCodeWriter writer =
          new QRCodeWriter();

      BitMatrix matrix = // The output — a 2D grid of black and white points
          writer.encode(
              text,
              BarcodeFormat.QR_CODE,
              300,
              300
          );


// data is written to bytes
      ByteArrayOutputStream stream =
          new ByteArrayOutputStream();

      MatrixToImageWriter.writeToStream(
          matrix,
          "PNG",
          stream
      );

      return Base64.getEncoder()
          .encodeToString(
              stream.toByteArray()
          );

    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to generate QR code",
          e
      );

    }}}