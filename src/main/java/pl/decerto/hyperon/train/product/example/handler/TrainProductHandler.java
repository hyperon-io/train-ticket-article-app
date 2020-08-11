package pl.decerto.hyperon.train.product.example.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * @author Maciej Główka on 30.07.2020
 */
@Component
public class TrainProductHandler {

	public Mono<ServerResponse> getProduct(ServerRequest request, int version) {
		return ServerResponse.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(resolveProduct(request, version)));
	}

	private String resolveProduct(ServerRequest request, int version) {
		if (version == 1) {
			return resolveProductInFirstVersion(request);
		} else if (version == 2) {
			return resolveProductInSecondVersion(request);
		} else {
			return "A";
		}
	}

	private String resolveProductInSecondVersion(ServerRequest request) {
		String seatClass = getQueryParam(request, RequestAttributeCode.CLASS);
		int distance = Integer.parseInt(getQueryParam(request, RequestAttributeCode.DISTANCE));

		switch (seatClass) {
		case "economy":
			if (distance < 50) {
				return "C";
			} else {
				return "B";
			}
		case "first_class":
			if (distance < 20) {
				return "B";
			} else {
				return "A";
			}
		case "second_class":
			if (distance < 20) {
				return "C";
			} else if (distance > 20 && distance < 100) {
				return "B";
			} else {
				return "A";
			}
		default:
			throw new UnsupportedOperationException();
		}
	}

	private String resolveProductInFirstVersion(ServerRequest request) {
		String seatClass = getQueryParam(request, RequestAttributeCode.CLASS);

		switch (seatClass) {
		case "economy":
			return "C";
		case "first_class":
			return "A";
		case "second_class":
			return "B";
		default:
			throw new UnsupportedOperationException();
		}
	}

	private String getQueryParam(ServerRequest request, String name) {
		return request.queryParam(name).orElseThrow(UnsupportedOperationException::new);
	}
}
