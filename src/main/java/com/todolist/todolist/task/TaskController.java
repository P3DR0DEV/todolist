package com.todolist.todolist.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/create")
  public TaskModel create(@RequestBody TaskModel taskModel) {
    var task = this.taskRepository.save(taskModel);
    return task;
  }

  @GetMapping("")
  public List<TaskModel> fetchTasks() {
    var tasks = this.taskRepository.findAll();
    return tasks;
  }

  @GetMapping("user/{idUser}")
  public List<TaskModel> fetchUsersTasks(@PathVariable UUID idUser) {
    var tasks = this.taskRepository.findByIdUser(idUser);
    return tasks;
  }

  @GetMapping("{id}")
  public Optional<TaskModel> fetchTask(@PathVariable UUID id) {
    var task = this.taskRepository.findById(id);
    return task;
  }

  @PutMapping("{id}")
  public TaskModel updateTask(@PathVariable UUID id, @RequestBody TaskModel taskModel) {
    var task = this.taskRepository.findById(id).get();

    task.setTitle(taskModel.getTitle());
    task.setDescription(taskModel.getDescription());
    task.setPriority(taskModel.getPriority());
    task.setStartAt(taskModel.getStartAt());
    task.setEndAt(taskModel.getEndAt());

    return this.taskRepository.save(task);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable UUID id) {
    this.taskRepository.deleteById(id);
  }
}
