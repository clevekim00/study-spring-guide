package me.clevekim.springguide.gsintegration

import com.rometools.rome.feed.synd.SyndEntryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.integration.endpoint.SourcePollingChannelAdapter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@SpringBootTest(
    "auto.startup=false",  // we don't want to start the real feed
    "feed.file.name=Test"
) // use a different file
class FlowTests(
    @Autowired val newsAdapter: SourcePollingChannelAdapter,
    @Autowired val news: MessageChannel
) {

    @Test
    @Throws(Exception::class)
    fun test() {
        assertThat(newsAdapter.isRunning()).isFalse()
        val syndEntry = SyndEntryImpl()
        syndEntry.setTitle("Test Title")
        syndEntry.setLink("http://characters/frodo")
        val out = File("/tmp/si/Test")
        out.delete()
        assertThat(out.exists()).isFalse()
        news.send(MessageBuilder.withPayload(syndEntry).build())
        assertThat(out.exists()).isTrue()
        val br = BufferedReader(FileReader(out))
        val line: String = br.readLine()
        assertThat(line).isEqualTo("Test Title @ http://characters/frodo")
        br.close()
        out.delete()
    }
}