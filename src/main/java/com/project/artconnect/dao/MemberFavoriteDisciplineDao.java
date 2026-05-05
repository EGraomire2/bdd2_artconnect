package com.project.artconnect.dao;

import java.util.List;

public interface MemberFavoriteDisciplineDao {
    void addFavoriteDiscipline(int memberId, int disciplineId);
    
    void removeFavoriteDiscipline(int memberId, int disciplineId);
    
    List<Integer> getFavoriteDisciplineIdsByMemberId(int memberId);
    
    List<Integer> getMemberIdsByDisciplineId(int disciplineId);
    
    boolean exists(int memberId, int disciplineId);
}
