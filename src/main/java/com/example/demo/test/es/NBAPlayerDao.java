package com.example.demo.test.es;


import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NBAPlayerDao {

    @Select("select * from nba_player")
    public List<NBAPlayer> selectAll();
}
