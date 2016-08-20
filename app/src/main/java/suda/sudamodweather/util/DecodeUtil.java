package suda.sudamodweather.util;

import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecodeUtil {

    public static String decodeResponse(String str) throws Exception {
        if (TextUtils.isEmpty(str))
            return null;

        String CIPHER_ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding";
        String KEY_ALGORITHM = "AES";
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NoPadding);
        SecretKeySpec secretKey = new SecretKeySpec(
                "2345android_key_".getBytes(), KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
                "2345tqIv_shiqing".getBytes()));// 使用解密模式初始化 密钥
        byte[] decrypt = cipher.doFinal(hexString2Bytes(str));
        // String response = decodeUnicode(new String(decrypt));
        String response = new String(decrypt);
        int last = response.lastIndexOf("}");
        response = response.substring(0, last + 1);
        // System.out.println(response);
        return response;
    }

    private static String decodeUnicode(String str) {
        Charset set = Charset.forName("UTF-16");
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher(str);
        int start = 0;
        int start2 = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find(start)) {
            start2 = m.start();
            if (start2 > start) {
                String seg = str.substring(start, start2);
                sb.append(seg);
            }
            String code = m.group(1);
            int i = Integer.valueOf(code, 16);
            byte[] bb = new byte[4];
            bb[0] = (byte) ((i >> 8) & 0xFF);
            bb[1] = (byte) (i & 0xFF);
            ByteBuffer b = ByteBuffer.wrap(bb);
            sb.append(String.valueOf(set.decode(b)).trim());
            start = m.end();
        }
        start2 = str.length();
        if (start2 > start) {
            String seg = str.substring(start, start2);
            sb.append(seg);
        }
        return sb.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */
    private static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }


}
