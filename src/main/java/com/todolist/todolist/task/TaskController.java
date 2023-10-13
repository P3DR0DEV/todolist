package com.todolist.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/create")
  public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = (UUID) request.getAttribute("idUser");
    taskModel.setIdUser(idUser);
    var currentDate = LocalDateTime.now();
    var startTime = taskModel.getStartAt();
    
    if(currentDate.isAfter(startTime)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time must be before current time");
    }

    if(startTime.isBefore(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End time must be after start time");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }

  @GetMapping("")
  public List<TaskModel> fetchTasks() {
    var tasks = this.taskRepository.findAll();
    return tasks;
  }

  @GetMapping("user/")
  public List<TaskModel> fetchUsersTasks(HttpServletRequest resquest) {
    var idUser = (UUID) resquest.getAttribute("idUser");

    var tasks = this.taskRepository.findByIdUser(idUser);
    return tasks;
  }

  @GetMapping("{id}")
  public Optional<TaskModel> fetchTask(@PathVariable UUID id) {
    var task = this.taskRepository.findById(id);
    return task;
  }

  @PutMapping("{id}")
  public ResponseEntity<Object> updateTask(@PathVariable UUID id, @RequestBody TaskModel taskModel,HttpServletRequest resquest) {
    var task = this.taskRepository.findById(id).orElse(null);
    var idUser = (UUID) resquest.getAttribute("idUser");

    if(task == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }

    if(!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This task does not belong to you");
    }
    Utils.copyNonNullProperties(taskModel, task);

   var updatedTask= this.taskRepository.save(taskModel);
   return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Object> delete(@PathVariable UUID id, HttpServletRequest resquest) {
    var task = this.taskRepository.findById(id).get();
    var idUser = (UUID) resquest.getAttribute("idUser");

    if(task == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }
      
    if(!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This task does not belong to you");
    }
    this.taskRepository.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("Task deleted");
  }
}
