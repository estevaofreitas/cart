package br.com.mateus.cart.util;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonbResolver implements ContextResolver<Jsonb> {
	private final Jsonb jsonB;

	public JsonbResolver() {
		JsonbConfig config = new JsonbConfig();
		config.withNullValues(true);
		jsonB = JsonbBuilder.create(config);
	}

	@Override
	public Jsonb getContext(Class<?> type) {
		return jsonB;
	}
}