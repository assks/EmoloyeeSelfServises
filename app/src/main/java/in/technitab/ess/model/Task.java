package in.technitab.ess.model;

public class Task {
    private String project;
    private String name;
    private String duration;

    public Task(String project, String name, String duration) {
        this.project = project;
        this.name = name;
        this.duration = duration;
    }

    public String getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }
}
