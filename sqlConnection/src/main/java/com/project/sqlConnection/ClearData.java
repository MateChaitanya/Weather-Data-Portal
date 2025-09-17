package com.project.sqlConnection;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class ClearData {
    @Autowired
    EntityManager entityManager;

    public void truncateTable(){

        try {
            String sql = " TRUNCATE TABLE Historical_Data";
            Query sqlQuery = entityManager.createNativeQuery(sql);
            sqlQuery.executeUpdate();
        }
        catch (Exception e){
            throw new RuntimeException();
        }
        System.out.println("Table Truncated - Historical_Data");
    }
}
