package com.quartzbit.myzakaat.dbUtils;

import android.content.Context;

import com.quartzbit.myzakaat.dbUtils.dao.LocationDao;
import com.quartzbit.myzakaat.dbUtils.entity.BankEntity;
import com.quartzbit.myzakaat.dbUtils.entity.LocationEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


/**
 * Created by Jemsheer K D on 02 January, 2018.
 * Package in.techware.afcetcbus.database
 * Project AFS_Bus_App
 */

@Database(entities = {LocationEntity.class, BankEntity.class}, version = 1)
@TypeConverters({DataTypeConverters.class})
public abstract class MyZakaatRoomDatabase extends RoomDatabase {


    public abstract LocationDao locationDao();

    private static MyZakaatRoomDatabase INSTANCE;


    public static MyZakaatRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyZakaatRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyZakaatRoomDatabase.class, "dearest_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}
