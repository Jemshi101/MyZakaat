package com.quartzbit.myzakaat.dbUtils.dao;

import com.quartzbit.myzakaat.dbUtils.entity.BankEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by Jemsheer K D on 13 February, 2018.
 * Package com.quartzbit.myzakaat.dbUtils.dao
 * Project Dearest
 */

@Dao
public interface BankDao {

    @Query("SELECT * FROM bank")
    List<BankEntity> loadAllBanks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BankEntity> bankEntityList);


    @Query("select * from bank where id = :id ORDER BY id ASC LIMIT 1")
    List<BankEntity> getBank(String id);

    @Query("DELETE FROM bank")
    void deleteAll();

    @Insert
    void insert(BankEntity bankEntity);

    @Update
    void update(BankEntity bankEntity);

    @Delete
    void delete(BankEntity bankEntity);

}
