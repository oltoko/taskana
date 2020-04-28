package pro.taskana.rest.resource;

import static pro.taskana.rest.resource.TaskanaPagedModelKeys.TASKS;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.SystemException;
import pro.taskana.rest.Mapping;
import pro.taskana.rest.resource.links.PageLinks;
import pro.taskana.task.api.models.TaskSummary;

/** EntityModel assembler for {@link TaskSummaryRepresentationModel}. */
@Component
public class TaskSummaryRepresentationModelAssembler
    implements RepresentationModelAssembler<TaskSummary, TaskSummaryRepresentationModel> {

  @NonNull
  @Override
  public TaskSummaryRepresentationModel toModel(@NonNull TaskSummary taskSummary) {
    TaskSummaryRepresentationModel resource;
    try {
      resource = new TaskSummaryRepresentationModel(taskSummary);
      return resource;
    } catch (InvalidArgumentException e) {
      throw new SystemException("caught unexpected Exception.", e.getCause());
    }
  }

  @PageLinks(Mapping.URL_TASKS)
  public TaskanaPagedModel<TaskSummaryRepresentationModel> toPageModel(
      List<TaskSummary> taskSummaries, PageMetadata pageMetadata) {
    return taskSummaries.stream()
        .map(this::toModel)
        .collect(
            Collectors.collectingAndThen(
                Collectors.toList(), list -> new TaskanaPagedModel<>(TASKS, list, pageMetadata)));
  }
}
