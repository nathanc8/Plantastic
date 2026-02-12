package com.plantastic.backend.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserUtilTest {

  @Test
  @DisplayName("Valid emails should return true")
  void isValidEmail_validEmails() {
    assertTrue(UserUtil.isValidEmail("user@example.com"));
    assertTrue(UserUtil.isValidEmail("user.name@example.co.uk"));
    assertTrue(UserUtil.isValidEmail("user_name-123@example-domain.com"));
  }

  @Test
  @DisplayName("Invalid emails should return false")
  void isValidEmail_invalidEmails() {
    assertFalse(UserUtil.isValidEmail("userexample.com"));  
    assertFalse(UserUtil.isValidEmail("user@.com"));       
    assertFalse(UserUtil.isValidEmail("@example.com"));     
    assertFalse(UserUtil.isValidEmail("user@com"));         
    assertFalse(UserUtil.isValidEmail("user@domain.toolongtld")); 
    assertFalse(UserUtil.isValidEmail(""));               
    assertFalse(UserUtil.isValidEmail(null));                
  }
}
