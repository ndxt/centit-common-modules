package com.centit.test;

import com.centit.support.office.PdfSignatureUtil;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.DigestAlgorithms;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class TestPdfSign {
    public static final char[] PASSWORD = "123456".toCharArray();// keystory密码

    public static void main(String[] args) {
        try {
            PdfSignatureUtil app = new PdfSignatureUtil();
            String base="/home/codefan/projects/framework/centit-common-modules/centit-office-utils/src/main/resources/template/";
            // 将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
            String pkPath = "client1.p12";
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(base+pkPath), PASSWORD);
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
            // 得到证书链
            Certificate[] chain = ks.getCertificateChain(alias);
            //需要进行签章的pdf
            String path = "fff.pdf";
            // 封装签章信息
            PdfSignatureUtil.SignatureInfo signInfo = new PdfSignatureUtil.SignatureInfo();
            signInfo.setReason("理由");
            signInfo.setLocation("位置");
            signInfo.setPk(pk);
            signInfo.setChain(chain);
            signInfo.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            signInfo.setDigestAlgorithm(DigestAlgorithms.SHA1);
            signInfo.setFieldName("demo");
            // 签章图片
            signInfo.setImagePath(base+"yinzhang.jpg");
            signInfo.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
            signInfo.setRectllx(100);  // 值越大，代表向x轴坐标平移 缩小 （反之，值越小，印章会放大）
            signInfo.setRectlly(200);  // 值越大，代表向y轴坐标向上平移（大小不变）
            signInfo.setRecturx(800);  // 值越大   代表向x轴坐标向右平移  （大小不变）
            signInfo.setRectury(400);  // 值越大，代表向y轴坐标向上平移（大小不变）
            //签章后的pdf路径
            app.sign(base+path, base+"output.pdf", signInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
