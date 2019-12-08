package com.tl.commerce.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerifyCodeUtilTest {
    @Test
    void generateVerifyCode() {
        System.out.println(VerifyCodeUtil.generateVerifyCode(6));
    }
}