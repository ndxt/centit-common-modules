package com.centit.support.office.commons;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.File;

public abstract class CommonUtils {
    private static final char intToAlpha[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    public static String mapWidowsPathIfNecessary(String filePath) {
        return File.separator.equals("\\") ?
            filePath.replace('/', '\\') :
            filePath;
    }

    public static String byteArrayToBase64(byte[] a) {
        int aLen = a.length;
        int numFullGroups = aLen / 3;
        int numBytesInPartialGroup = aLen - 3 * numFullGroups;
        int resultLen = 4 * ((aLen + 2) / 3);
        final StringBuilder result = new StringBuilder(resultLen);

        // Translate all full groups from byte array elements to Base64
        int inCursor = 0;
        for (int i = 0; i < numFullGroups; i++) {
            int byte0 = a[inCursor++] & 0xff;
            int byte1 = a[inCursor++] & 0xff;
            int byte2 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
            result.append(intToAlpha[(byte1 << 2) & 0x3f | (byte2 >> 6)]);
            result.append(intToAlpha[byte2 & 0x3f]);
        }

        // Translate partial group if present
        if (numBytesInPartialGroup != 0) {
            int byte0 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToAlpha[(byte0 << 4) & 0x3f]);
                result.append("==");
            } else {
                // assert numBytesInPartialGroup == 2;
                int byte1 = a[inCursor++] & 0xff;
                result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
                result.append(intToAlpha[(byte1 << 2) & 0x3f]);
                result.append('=');
            }
        }
        // assert inCursor == a.length;
        // assert result.length() == resultLen;
        return result.toString();
    }

    public static Transformer createTransformer() throws TransformerConfigurationException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "no");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        return serializer;
    }
}

