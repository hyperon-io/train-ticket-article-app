package pl.decerto.hyperon.train.product.example.handler;

import lombok.RequiredArgsConstructor;

import org.smartparam.engine.core.output.ParamValue;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import pl.decerto.hyperon.runtime.core.HyperonContext;
import pl.decerto.hyperon.runtime.core.HyperonEngine;
import reactor.core.publisher.Mono;

/**
 * @author Maciej Główka on 27.07.2020
 */
@Component
@RequiredArgsConstructor
public class TrainProductHyperonHandler {
	private final HyperonEngine hyperonEngine;

	public Mono<ServerResponse> getProduct(ServerRequest request, int version) {
		return ServerResponse.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(getProductFromHyperon(request, version)));
	}

	private String getProductFromHyperon(ServerRequest request, int version) {
		ParamValue pv = hyperonEngine.get(getParameterCode(version), getContext(request, version));

		if (version == 4) {
			return String.format("%s|%d%%", pv.get("product"), pv.get("discount"));
		} else {
			return pv.get();
		}
	}

	private HyperonContext getContext(ServerRequest request, int version) {
		switch (version) {
		case 1:
			return new HyperonContext(RequestAttributeCode.CLASS, getQueryParam(request, RequestAttributeCode.CLASS));
		case 2:
		case 3:
			return new HyperonContext(RequestAttributeCode.CLASS, getQueryParam(request, RequestAttributeCode.CLASS),
				RequestAttributeCode.DISTANCE, getQueryParam(request, RequestAttributeCode.DISTANCE));
		case 4:
			return new HyperonContext(RequestAttributeCode.CLASS, getQueryParam(request, RequestAttributeCode.CLASS),
				RequestAttributeCode.DISTANCE, getQueryParam(request, RequestAttributeCode.DISTANCE),
				RequestAttributeCode.BIRTHDAY, getQueryParam(request, RequestAttributeCode.BIRTHDAY));
		default:
			throw new UnsupportedOperationException();
		}
	}

	private String getQueryParam(ServerRequest request, String name) {
		return request.queryParam(name).orElseThrow(UnsupportedOperationException::new);
	}

	private String getParameterCode(int version) {
		return "train.product.v" + version;
	}
}
