package com.companyName.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.x509.RDN;
import sun.security.x509.X500Name;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PDFValidation {

    /**
     * Get pdf doucmnet
     * @param pdfUrl
     * @return PDDocument
     */
    public static PDDocument getPdfDocument(String pdfUrl){
        BufferedInputStream bf = PDFValidation.pdfReaderFromURL(pdfUrl);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    /**
     * Pdf document in text
     * @param pdfUrl
     * @return String
     */
    public static String getPdfText(String pdfUrl){
        PDDocument doc = getPdfDocument(pdfUrl);
        String pdfText = null;
        try {
            pdfText = new PDFTextStripper().getText(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfText;
    }

    /**
     * Get signature from pdf
     * @param pdfUrl
     * @return PDSignature
     */
    public static PDSignature getPdfSignature(String pdfUrl){
        PDSignature signature = null;
        try {
            signature = getPdfDocument(pdfUrl).getSignatureFields().get(0).getSignature();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signature;
    }

    /**
     * Get signature as bytes
     * @param pdfUrl
     * @return byte[]
     */
    public static byte[] getSignatureAsByte(String pdfUrl){
        BufferedInputStream bff = pdfReaderFromURL(pdfUrl);
        byte[] signatureAsBytes = null;
        try {
            signatureAsBytes = getPdfSignature(pdfUrl).getContents(bff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signatureAsBytes;
    }

    /**
     * Get signature content as bytes
     * @param pdfUrl
     * @return byte[]
     */
    public static byte[] getSignedContentAsByte(String pdfUrl){
        BufferedInputStream bff = pdfReaderFromURL(pdfUrl);
        byte[] signatureAsBytes = null;
        try {
            signatureAsBytes = getPdfSignature(pdfUrl).getSignedContent(bff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signatureAsBytes;
    }

    /**
     * Get pdf certificate
     * @param signedContentAsBytes
     * @param signatureAsBytes
     * @return X509Certificate
     */
    public static X509Certificate getCertificate(byte[] signedContentAsBytes, byte[] signatureAsBytes){
        CMSSignedData cms = null;
        X509Certificate certificate = null;
        try {
            cms = new CMSSignedData(new CMSProcessableByteArray(signedContentAsBytes), signatureAsBytes);
        } catch (CMSException e) {
            e.printStackTrace();
        }
        SignerInformation signerInfo = (SignerInformation) cms.getSignerInfos().getSigners().iterator().next();
        X509CertificateHolder cert = (X509CertificateHolder) cms.getCertificates().getMatches(signerInfo.getSID())
                .iterator().next();
        JcaX509CertificateConverter converter = new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider());
        try {
            certificate = converter.getCertificate(cert);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return certificate;
    }

    /**
     *Get certificate issue details
     * @param certificate
     * @return String
     */
    public static String getCertificateIssueDN(X509Certificate certificate){
        return certificate.getIssuerDN().getName();
    }

    /**
     * Get certificate Signature Name
     * @param certificate
     * @return String
     */
    public static String getCertificateSignatureName(X509Certificate certificate){
        X500Name x500Name = null;
        try {
            x500Name = new X500Name(certificate.getSubjectX500Principal().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDN cn = x500Name.rdns().get(0);
        return cn.avas().get(0).getValueString();
    }

    /**
     * Read pdf from URL
     * @param pdf
     * @return BufferedInputStream
     */
    public static BufferedInputStream pdfReaderFromURL(String pdf){
        URL pdfUrl = null;
        InputStream in = null;
        BufferedInputStream bff = null;
        try {
            pdfUrl = new URL(pdf);
            in = pdfUrl.openStream();
            bff = new BufferedInputStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bff;
    }

    /**
     * To get text from local stored pdf (Expected pdf)
     * @param filePath
     * @return String
     */
    public static String expectedPdfDoc(String filePath){
        File dir = new File(filePath);
        File file  = Arrays.stream(dir.listFiles()).findFirst().get();
        PDDocument excDoc = null;
        String pdfText = null;
        try {
            excDoc = PDDocument.load(file);
            pdfText = new PDFTextStripper().getText(excDoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfText;
    }


}
