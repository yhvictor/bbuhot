package com.bbuhot.server.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class AuthorityUtil {

  static String authKeyWithSaltAfterMd5(String authKey, String saltKey) {
    return md5(authKey + saltKey);
  }

  static boolean isValid(
      String authKey, String saltKey, String mixedInput, int uid, String password) {
    authKey = authKeyWithSaltAfterMd5(authKey, saltKey);
    return decode(mixedInput, authKey).equals(password + "\t" + uid);
  }

  static String decode(String mixedInput, String authKey) {
    int keyCLength = 4;

    authKey = md5(authKey);
    String keyA = md5(authKey.substring(0, 16));
    String keyB = md5(authKey.substring(16, 32));
    String keyC = mixedInput.substring(0, keyCLength);

    String cryptKey = keyA + md5(keyA + keyC);

    byte[] input2 = new byte[(mixedInput.length() - keyC.length() + 3) / 4 * 4];
    for (int i = 0; i < input2.length; i++) {
      input2[i] =
          (i + keyC.length()) < mixedInput.length()
              ? (byte) mixedInput.charAt(i + keyC.length())
              : (byte) '=';
    }

    input2 = Base64.getDecoder().decode(input2);

    char[] box = new char[256];
    for (int i = 0, j = 0; i < 256; i++, j++) {
      box[i] = (char) i;
    }

    char[] rndKey = new char[256];
    for (int i = 0; i < 256; i++) {
      rndKey[i] = (char) (cryptKey.charAt(i % cryptKey.length()) & 255);
    }

    for (int j = 0, i = 0; i < 256; i++) {
      j = (j + box[i] + rndKey[i]) & 255;
      char tmp = box[i];
      box[i] = box[j];
      box[j] = tmp;
    }

    for (int k = 0, j = 0, i = 0; i < input2.length; i++) {
      k = (k + 1) & 255;
      j = (j + box[k]) & 255;
      char tmp = box[k];
      box[k] = box[j];
      box[j] = tmp;
      input2[i] = (byte) (input2[i] ^ box[(box[k] + box[j]) & 255]);
    }

    String ret = byteArrayToHexString(input2);

    return ret.substring(26);
  }

  private static String md5(String text) {
    String hashtext="";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(new String(text).getBytes("UTF-8"));
      BigInteger no = new BigInteger(1, md.digest());
      hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
    return hashtext;
  }

  private static String byteArrayToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte aByte : bytes) {
      sb.append((char) aByte);
    }

    return sb.toString();
  }
}
