package com.bbuhot.server.domain;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class AuthorityUtil {

  private AuthorityUtil() {
  }

  private static final char[] hexArray = "0123456789abcdef".toCharArray();

  static String authKeyWithSaltAfterMd5(String authKey, String saltKey) {
    return md5(authKey + saltKey);
  }

  static AuthResult getAuthResult(
          String authKey, @Nullable String saltKey, @Nullable String mixedInput) {
    if (saltKey == null || mixedInput == null) {
      throw new IllegalStateException("Missing input values.");
    }

    authKey = authKeyWithSaltAfterMd5(authKey, saltKey);

    @SuppressWarnings("StringSplitter")
    String[] pair = decode(mixedInput, authKey).split("\t");
    if (pair.length != 2) {
      throw new IllegalStateException("Auth decode error: wrong pair length");
    }

    return new AuthResult(/* uid= */ Integer.valueOf(pair[1]), /* password= */ pair[0]);
  }

  static String decode(String mixedInput, String authKey) {
    int keyCLength = 4;

    if (mixedInput.length() <= keyCLength) {
      throw new IllegalStateException("Auth key too short: " + mixedInput.length());
    }

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
    try {
      MessageDigest digester = MessageDigest.getInstance("MD5");
      digester.update(text.getBytes(StandardCharsets.ISO_8859_1));
      byte[] md5Bytes = digester.digest();
      return bytesToHex(md5Bytes);

    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }

  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  private static byte[] hexStringToByteArray(String string) {
    byte[] ret = new byte[string.length()];
    for (int i = 0; i < string.length(); i++) {
      ret[i] = (byte) string.charAt(i);
    }
    return ret;
  }

  private static String byteArrayToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte aByte : bytes) {
      sb.append((char) aByte);
    }

    return sb.toString();
  }

  static class AuthResult {

    private final int uid;
    private final String password;

    private AuthResult(int uid, String password) {
      this.uid = uid;
      this.password = password;
    }

    int getUid() {
      return uid;
    }

    String getPassword() {
      return password;
    }
  }
}
