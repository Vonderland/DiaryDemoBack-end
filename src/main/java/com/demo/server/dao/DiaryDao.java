package com.demo.server.dao;

import com.demo.server.bean.Diary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Vonderland on 2017/1/29.
 */
@Repository
public interface DiaryDao {
    int insertDiary(Diary diary);
    int updateDiary(@Param("diary") Diary diary, @Param("id") long id);
    int updateDiaryNotChangePic(@Param("diary") Diary diary, @Param("id") long id);
    int deleteDiary(@Param("id") long id);
    Diary selectDiaryByID(@Param("id") long id);
    List<Diary> selectDiaries(@Param("size") int size, @Param("timeCursor") long timeCursor);
    List<Diary> selectAllDiaries();
}
