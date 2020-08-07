package com.thoughtworks.archguard.config.domain

import io.mockk.MockKAnnotations.init
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConfigureServiceTest {
    @InjectMockKs
    var service = ConfigureService()

    @MockK
    lateinit var repo: ConfigureRepository

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    fun `should get all configures`() {
        //given
        val configure = Configure("id", "nodeHidden", "clz", "21", 1)
        //when
        every { repo.getConfigures() } returns listOf(configure)
        val configures = service.getConfigures()
        //then
        assertThat(configures.size).isEqualTo(1)
        assertThat(configures[0]).isEqualToComparingFieldByField(configure)
    }

    @Test
    fun `should create configure`() {
        //given
        val configure = Configure("id", "nodeHidden", "clz", "21", 1)
        //when
        every { repo.create(configure) } just Runs
        service.create(configure)
        //then
        verify { repo.create(configure) }
    }

    @Test
    fun `should update configure`() {
        //given
        val id = "id"
        val configure = Configure(id, "nodeHidden", "clz", "21", 1)
        //when
        every { repo.update(any()) } just Runs
        service.update(id, configure)
        //then
        verify { repo.update(any()) }
    }

    @Test
    fun `should delete configure`() {
        //given
        val id = "id"
        //when
        every { repo.delete(id) } just Runs
        service.delete(id)
        //then
        verify { repo.delete(id) }
    }

    @Test
    fun `should display class`() {
        //given
        //when
        every { repo.getConfiguresByType("nodeDisplay") } returns listOf(
                Configure("", "nodeDisplay", "com", "contain", 1),
                Configure("", "nodeDisplay", "org", "contain", 1),
                Configure("", "nodeDisplay", "common", "contain", 1),
                Configure("", "nodeDisplay", "org.scalatest", "hidden", 1)
        )

        assert(service.isDisplayNode("common.domain.ConfigureService"))
        assert(service.isDisplayNode("org.scalamock.scalatest.MockFactory"))
        assert(!service.isDisplayNode("common.domain.ConfigureServiceTest"))
        assert(!service.isDisplayNode("V"))
        assert(!service.isDisplayNode("org.scalatest.FlatSpec"))
        assert(!service.isDisplayNode("scala.concurrent.Future"))
        assert(!service.isDisplayNode("java.lang.Integer"))
    }
}
