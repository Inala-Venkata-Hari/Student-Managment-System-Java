/*************  ✨ Windsurf Command 🌟  *************/
package studentdb.model;
/*******  ac819822-b55e-4a33-96a3-834e3f5deb2f  *******/

public class Faculty {
    private int id;
    private String name;
    private String department;
    private String email;

    public Faculty(int id, String name, String department, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    // Overloaded constructor for new inserts (id = 0)
    public Faculty(String name, String department, String email) {
        this(0, name, department, email);
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Dept: %s | Email: %s",
                             id, name, department, email);
    }
}
