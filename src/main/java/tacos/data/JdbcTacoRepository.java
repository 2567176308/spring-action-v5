package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository{
    private JdbcTemplate jdbc;
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(ingredient,tacoId);
        }
        return taco;
    }

    /**
     * 因为只有插入进表里面才能得到真实的id值，才能插入Taco_Ingredient表中
     * @param taco
     * @return
     */
    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory preparedStatementCreatorFactory =
                new PreparedStatementCreatorFactory(
                        "insert into Taco (name, createdAt) values (?,?)",
                        Types.VARCHAR,Types.TIMESTAMP
                );
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc =
                preparedStatementCreatorFactory.newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                new Timestamp(taco.getCreatedAt().getTime())
                        )
                );
//        PreparedStatementCreator psc =
//                new PreparedStatementCreatorFactory(
//                        "insert into Taco (name,createdAt) values(?,?)",
//                        Types.VARCHAR,Types.TIMESTAMP).newPreparedStatementCreator(
//                        Arrays.asList(
//                                taco.getName(),
//                                new Timestamp(taco.getCreatedAt().getTime())
//                        )
//                );
        KeyHolder keyHolder =new GeneratedKeyHolder();
        jdbc.update(psc,keyHolder);
        long id =  keyHolder.getKey().longValue();
        return id;
    }
    private void saveIngredientToTaco(Ingredient ingredient, long tacoId ){
                jdbc.update(
                        "insert into Taco_Ingredients (taco , ingredient)" +
                                "values (?,?)",
                        tacoId ,ingredient.getId());
    }
}
