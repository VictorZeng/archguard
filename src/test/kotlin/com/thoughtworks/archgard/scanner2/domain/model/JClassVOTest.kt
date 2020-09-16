package com.thoughtworks.archgard.scanner2.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class JClassVOTest {
    @Test
    internal fun shouldGetPackageNameGivenNameStartWithPackageName() {
        val jClassVO = JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service")

        val packageName = jClassVO.getPackageName()

        assertEquals("net.aimeizi.dubbo.service.service", packageName)
    }

    @Test
    internal fun shouldGetEmptyPackageNameGivenNameWithoutPackageName() {
        val jClassVO = JClassVO("UserService", "dubbo-service")

        val packageName = jClassVO.getPackageName()

        assertEquals("", packageName)
    }

    @Test
    internal fun shouldGetTypeNameGivenNameStartWithPackageName() {
        val jClassVO = JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service")

        val typeName = jClassVO.getTypeName()

        assertEquals("UserService", typeName)
    }

    @Test
    internal fun shouldGetTypeNameGivenNameNotStartWithPackageName() {
        val jClassVO = JClassVO("UserService", "dubbo-service")

        val typeName = jClassVO.getTypeName()

        assertEquals("UserService", typeName)
    }

}