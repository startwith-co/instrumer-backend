package instrumers.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient.Builder webClientBuilder() {
		HttpClient httpClient = HttpClient.create()
			.responseTimeout(Duration.ofSeconds(10))
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.doOnConnected(conn ->
				conn.addHandlerLast(new ReadTimeoutHandler(10))
					.addHandlerLast(new WriteTimeoutHandler(10)));

		ExchangeStrategies strategies = ExchangeStrategies.builder()
			.codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(1024 * 1024))
			.build();

		return WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.exchangeStrategies(strategies);
	}
}
