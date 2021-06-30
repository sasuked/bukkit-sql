package io.github.sasuked.bukkitsql.loader;

import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PluginSqlLoader {

	private final Plugin plugin;

	private final Map<String, String> queryCache;

	public PluginSqlLoader(Plugin plugin) {
		this.plugin = plugin;
		this.queryCache = Collections.synchronizedMap(new HashMap<>());
	}

	public String getQuery(String path) {
		return queryCache.getOrDefault(path, load(path));
	}


	public Map<String, String> getQueryCache() {
		return queryCache;
	}

	private String load(String resource) {
		InputStream inputStream = plugin.getResource(resource);
		String result = new BufferedReader(new InputStreamReader(inputStream))
			.lines()
			.collect(Collectors.joining(" "));

		queryCache.put(resource, result);

		return StringUtils.normalizeSpace(result);
	}
}
