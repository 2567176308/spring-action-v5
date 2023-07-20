package tacos;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Taco {

    private Long id;
    private Date createdAt;
    @NotNull
    @Size(min=5,message="Name must be at least 5 characters long")
    private String name;
    @Size(min=1,message = "you must choose at least 1 ingredient")
    private List<Ingredient> ingredients;


}
