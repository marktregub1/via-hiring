package org.via.hiring.pathsearch;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.via.hiring.pathsearch.beans.GraphData;
import org.via.hiring.pathsearch.dao.GraphDao;
import org.via.hiring.pathsearch.exceptions.GraphNotInitiatedException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class PathSearchApiController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@PostMapping("/set-graph")
	public void setGraph(@NonNull @RequestBody JsonNode graphDataJson) {
		GraphData graphData = GraphData.fromJson(graphDataJson);
		graphData.validate();
		GraphDao.graphData = graphData;
	}

	@GetMapping("/shortest-path")
	public List<String> shortestPath(@NonNull @RequestParam(value = "from") String from,
									 @NonNull @RequestParam(value = "to") String to) {
		validateGraphInitiated();
		return GraphDao.graphData.searchPath(from, to);
	}

	@PostMapping("/set-weight")
	public void setWeight(@NonNull @RequestParam(value = "from") String from,
						  @NonNull @RequestParam(value = "to") String to,
						  @NonNull @RequestParam(value = "weight") Double weight) {
		validateGraphInitiated();
		GraphDao.graphData.setWeight(from, to, weight);
	}

	private void validateGraphInitiated() {
		if(GraphDao.graphData == null) throw new GraphNotInitiatedException();
	}

}
