package com.plantastic.backend.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
class StringUtilTest {

    @Test
    @DisplayName("Should return null when input is null")
    void emptyStringToNull_withNull_returnsNull() {
      // When
      String result = StringUtil.emptyStringToNull(null);

      // Then
      assertNull(result);
    }

    @Test
    @DisplayName("Should return null when input is empty string")
    void emptyStringToNull_withEmptyString_returnsNull() {
      // When
      String result = StringUtil.emptyStringToNull("");

      // Then
      assertNull(result);
    }

    @Test
    @DisplayName("Should not trim string and return value if not empty")
    void emptyStringToNull_withBlankString_returnsBlank() {
      // When
      String result = StringUtil.emptyStringToNull(" ");

      // Then
      assertEquals(" ", result);
    }

    @Test
    @DisplayName("Should return same string when input is not empty")
    void emptyStringToNull_withValidString_returnsSameValue() {
      // When
      String result = StringUtil.emptyStringToNull("hello");

      // Then
      assertEquals("hello", result);
    }
}
