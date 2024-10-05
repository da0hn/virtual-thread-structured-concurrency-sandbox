package dev.da0hn.concurrency.structured;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("preview")
public class AverageWeatherTaskScope extends StructuredTaskScope<LongRunningTask.TaskResponse> {

  private final List<Subtask<? extends LongRunningTask.TaskResponse>> successfullySubtasks = Collections.synchronizedList(new ArrayList<>());


  @Override
  public StructuredTaskScope<LongRunningTask.TaskResponse> join() throws InterruptedException {
    super.join();
    return this;
  }

  @Override
  protected void handleComplete(Subtask<? extends LongRunningTask.TaskResponse> subtask) {
    if (subtask.state() == Subtask.State.SUCCESS) {
      int subtasksQuantity = 0;
      synchronized (successfullySubtasks) {
        successfullySubtasks.add(subtask);
        subtasksQuantity = successfullySubtasks.size();
      }
      if (subtasksQuantity == 2) {
        this.shutdown();
      }
    }
  }

  public LongRunningTask.TaskResponse response() {
    super.ensureOwnerAndJoined();
    if (successfullySubtasks.size() != 2) {
      throw new IllegalStateException("Pelo menos duas sub-tarefas devem finalizar com sucesso");
    }

    var r1 = successfullySubtasks.get(0).get();
    var r2 = successfullySubtasks.get(1).get();

    final var temperature1 = Integer.valueOf(r1.message());
    final var temperature2 = Integer.valueOf(r2.message());
    final var average = (temperature1 + temperature2) / 2;
    return new LongRunningTask.TaskResponse(
      "Temperatura-media",
      "A temperatura média é: " + average,
      r1.timeTaken() + r2.timeTaken()
    );

  }

}
