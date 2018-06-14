package com.quartzbit.myzakaat.dbUtils.dao;

import com.quartzbit.myzakaat.dbUtils.entity.LocationEntity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/**
 * Created by Jemsheer K D on 13 February, 2018.
 * Package com.quartzbit.myzakaat.dbUtils.dao
 * Project Dearest
 */

@Dao
public interface LocationDao {

    @Query("SELECT * FROM location")
    List<LocationEntity> loadAllLocations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocationEntity> locationEntityList);


    @Query("select * from location where id = :id ORDER BY id DESC LIMIT 1")
    List<LocationEntity> getLocation(String id);

    @Query("DELETE FROM location")
    void deleteAll();

    @Insert
    void insert(LocationEntity locationEntity);

    @Update
    void update(LocationEntity locationEntity);

    @Delete
    void delete(LocationEntity locationEntity);

}
