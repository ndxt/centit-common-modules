package com.centit.support.office;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import lombok.Data;

import java.awt.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class PdfSignatureUtil {

    @Data
    public static class SignatureInfo {

        private String reasonDesc; //签名的原因，显示在pdf签名属性中
        private String locationDesc;//签名的地点，显示在pdf签名属性中
        private String digestAlgorithm;//摘要算法名称，例如SHA-1
        private Image signImage;//图章路径
        private String fieldName;//表单域名称
        private Certificate[] chain;//证书链
        private PrivateKey pk;//签名私钥
        private int certificationLevel; //批准签章
        //表现形式：仅描述，仅图片，图片和描述，签章者和描述
        private PdfSignatureAppearance.RenderingMode renderingMode;
        //图章属性
        private Rectangle signRect;//图章左下角x
        private int signPage; //批准签章

        public SignatureInfo(){
            certificationLevel = 0;
            digestAlgorithm = DigestAlgorithms.SHA1;
            renderingMode = PdfSignatureAppearance.RenderingMode.GRAPHIC;
        }

        public SignatureInfo reason(String reasonString){
            this.reasonDesc = reasonString;
            return this;
        }

        public SignatureInfo location(String locationDesc){
            this.locationDesc = locationDesc;
            return this;
        }

        public SignatureInfo algorithm(String digestAlgorithm){
            this.digestAlgorithm = digestAlgorithm;
            return this;
        }

        public SignatureInfo field(String fieldName){
            this.fieldName = fieldName;
            return this;
        }

        public SignatureInfo certificate(Certificate[] chain){
            this.chain = chain;
            return this;
        }

        public SignatureInfo privateKey(PrivateKey pk){
            this.pk = pk;
            return this;
        }

        public SignatureInfo certificateLevel(int certificationLevel){
            this.certificationLevel = certificationLevel;
            return this;
        }

        public SignatureInfo renderingMode(PdfSignatureAppearance.RenderingMode renderingMode){
            this.renderingMode = renderingMode;
            return this;
        }

        public SignatureInfo image(Image image){
            this.signImage = image;
            return this;
        }

        public SignatureInfo image(String imagePath){
            try {
                this.signImage = Image.getInstance(imagePath);
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public SignatureInfo image(final java.awt.Image image){
            try {
                this.signImage = Image.getInstance(image, new Color(255,255,255));
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public SignatureInfo page(int signPage){
            this.signPage = signPage;
            return this;
        }
        public SignatureInfo rect(Rectangle rectangle){
            this.signRect = rectangle;
            return this;
        }

        public SignatureInfo rect(final float llx, final float lly, final float urx, final float ury) {
            this.signRect = new Rectangle(llx, lly, urx, ury);
            return this;
        }
    }



    public static SignatureInfo createSingInfo(){
        return new SignatureInfo();
    }
    /**
     * 单多次签章通用
     *
     * @param src
     * @param target
     * @param signatureInfo
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    @SuppressWarnings("resource")
    public static void sign(String src, String target, SignatureInfo signatureInfo) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            inputStream = new FileInputStream(src);
            ByteArrayOutputStream tempArrayOutputStream = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(inputStream);
            // 创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
            // false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
            // true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
            PdfStamper stamper = PdfStamper.createSignature(reader,
                    tempArrayOutputStream, '\0', null, true);
            // 获取数字签章属性对象
            PdfSignatureAppearance appearance = stamper
                    .getSignatureAppearance();
            appearance.setReason(signatureInfo.getReasonDesc());
            appearance.setLocation(signatureInfo.getLocationDesc());
            // 设置签名的位置，页码，签名域名称，多次追加签名的时候，签名预名称不能一样 图片大小受表单域大小影响（过小导致压缩）
            // 签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
            // 四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
            appearance.setVisibleSignature(signatureInfo.getSignRect(), signatureInfo.getSignPage(), signatureInfo
                            .getFieldName());
            // 读取图章图片
            appearance.setSignatureGraphic(signatureInfo.getSignImage());
            appearance.setCertificationLevel(signatureInfo
                    .getCertificationLevel());
            // 设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
            appearance.setRenderingMode(signatureInfo.getRenderingMode());
            // 摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            // 签名算法
            ExternalSignature signature = new PrivateKeySignature(
                    signatureInfo.getPk(), signatureInfo.getDigestAlgorithm(),
                    null);
            // 调用itext签名方法完成pdf签章 //数字签名格式，CMS,CADE
            MakeSignature.signDetached(appearance, digest, signature,
                    signatureInfo.getChain(), null, null, null, 0,
                    MakeSignature.CryptoStandard.CADES);

            inputStream = new ByteArrayInputStream(
                    tempArrayOutputStream.toByteArray());
            // 定义输入流为生成的输出流内容，以完成多次签章的过程
            result = tempArrayOutputStream;

            outputStream = new FileOutputStream(new File(target));
            outputStream.write(result.toByteArray());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != result) {
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
