package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;

public interface MemberFavoriteDisciplineDao {
    void addFavoriteDiscipline(int memberId, int disciplineId) throws SQLException;
    
    void removeFavoriteDiscipline(int memberId, int disciplineId) throws SQLException;
    
    List<Integer> getFavoriteDisciplineIdsByMemberId(int memberId);
    
    List<Integer> getMemberIdsByDisciplineId(int disciplineId);
    
    boolean exists(int memberId, int disciplineId);
}
