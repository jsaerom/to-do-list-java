import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("categories/:id/tasks/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("category", category);
      model.put("template", "templates/category-tasks-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories/:categoryId/tasks/:taskId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":categoryId")));
      Task task = Task.find(Integer.parseInt(request.params(":taskId")));
      model.put("category", category);
      model.put("task", task);
      model.put("template", "templates/task-mark-complete.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/taskcomplete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.queryParams("taskId")));
      if(request.queryParams("taskCompleted") != null) {
        task.setCompleted(true);
      } else {
        task.setCompleted(false);
      }
      task.setDescription(request.queryParams("taskDescription"));
      task.update();
      model.put("template", "templates/task-complete-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.queryParams("categoryId")));

      String description = request.queryParams("description");
      Task newTask = new Task(description, category.getId());
      newTask.save();

      model.put("category", category);
      model.put("template", "templates/category-tasks-success.vtl");
      return new ModelAndView(model, layout);
     }, new VelocityTemplateEngine());

     get("/tasks/:id", (request, response) -> {
       HashMap<String, Object> model = new HashMap<String, Object>();
       Task task = Task.find(Integer.parseInt(request.params(":id")));
       model.put("task", task);
       model.put("template", "templates/task.vtl");
       return new ModelAndView (model, layout);
     }, new VelocityTemplateEngine());

     get("/categories/new", (request, response) -> {
       Map<String, Object> model = new HashMap <String, Object>();
       model.put("template", "templates/category-form.vtl");
       return new ModelAndView(model, layout);
     }, new VelocityTemplateEngine());

     post("/categories", (request, response) -> {
       Map<String, Object> model = new HashMap<String, Object>();
       String name = request.queryParams("name");
       Category newCategory = new Category(name);
       newCategory.save();
       model.put("template" , "templates/category-success.vtl");
       return new ModelAndView(model, layout);
     }, new VelocityTemplateEngine());

     get("/categories", (request, response) -> {
       Map<String, Object> model = new HashMap<String, Object>();
       model.put("categories", Category.all());
       model.put("template", "templates/categories.vtl");
       return new ModelAndView(model, layout);
     } , new VelocityTemplateEngine());

     get("/categories/:id", (request, response) -> {
       Map<String, Object> model = new HashMap<String, Object>();
       Category category = Category.find(Integer.parseInt(request.params(":id")));
       model.put("category", category);
       model.put("template" , "templates/category.vtl");
       return new ModelAndView(model, layout);
     }, new VelocityTemplateEngine());
  }
}
