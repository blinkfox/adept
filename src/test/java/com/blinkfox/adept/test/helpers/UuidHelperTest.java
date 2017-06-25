package com.blinkfox.adept.test.helpers;

import com.blinkfox.adept.helpers.UuidHelper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UuidHelper单元测试类.
 * Created by blinkfox on 2017/6/25.
 */
public class UuidHelperTest {

    private static final Logger log = LoggerFactory.getLogger(UuidHelperTest.class);

    /**
     * 测试获取默认(32位)的UUID的方法.
     */
    @Test
    public void testGetUuid() {
        String uuid = UuidHelper.getUuid();
        Assert.assertNotNull(uuid);
        log.info("生成的默认UUID为:{}", uuid);
    }

}