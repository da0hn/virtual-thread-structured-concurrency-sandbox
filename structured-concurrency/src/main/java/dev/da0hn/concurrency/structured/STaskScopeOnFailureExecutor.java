package dev.da0hn.concurrency.structured;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("preview")
public class STaskScopeOnFailureExecutor {

  public static Map<String, LongRunningTask.TaskResponse> execute(List<LongRunningTask> tasks) {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      final var subtasksMap = new HashMap<String, StructuredTaskScope.Subtask<LongRunningTask.TaskResponse>>();
      tasks.forEach(task -> {
        final var subtask = scope.fork(task);
        subtasksMap.put(task.name(), subtask);
      });
      scope.join();

      return subtasksMap.entrySet().stream()
        .map(entry -> entry.getValue().get())
        .collect(Collectors.toMap(LongRunningTask.TaskResponse::name, Function.identity()));
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    final var task1 = new LongRunningTask("dataTask", 3, "row1", false);
    final var task2 = new LongRunningTask("restTask", 10, "json1", false);
    final var task3 = new LongRunningTask("extTask", 5, "json1", false);

    STaskScopeOnFailureExecutor.execute(List.of(task1, task2, task3))
      .forEach((name, response) -> System.out.println(STR."\{name} -> \{response}"));

  }


}
