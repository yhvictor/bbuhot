package com.bbuhot.server.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AuthorityUtilTest {

  @Test
  public void testAuthKeyWithSaltAfterMd5() {
    String result =
        AuthorityUtil.authKeyWithSaltAfterMd5(
            "76fce85ae9cf5ceeee99c014615ee215qfDf49F0YXdxHuEkru", "R4nzBENB");
    assertEquals("1ce197e45f6c28c1d9f6b9d8086e71fc", result);
  }

  @Test
  public void testDecode() {
    String result =
        AuthorityUtil.decode(
            "168fo5rEPmPh2+Cf/zxw9wIcEhSRNuwWSMhKvX1TZVsM+Ng8xPZ03LFQb+rHmS8H/PGG6ZUjXdFKPKrPuvlG",
            "1ce197e45f6c28c1d9f6b9d8086e71fc");
    assertEquals("b2b29e4862ffe7a5d1388d1722a9aae5\t1", result);
  }
}
