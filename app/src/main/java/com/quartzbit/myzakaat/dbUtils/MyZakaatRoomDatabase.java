package com.quartzbit.myzakaat.dbUtils;

import android.content.Context;

import com.quartzbit.myzakaat.dbUtils.dao.BankDao;
import com.quartzbit.myzakaat.dbUtils.entity.BankEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


/**
 * Created by Jemsheer  D on 02 January, 2018.
 * Package in.techware.afcetcbus.database
 * Project AFS_Bus_App
 */

@Database(entities = {BankEntity.class}, version = 1)
@TypeConverters({DataTypeConverters.class})
public abstract class MyZakaatRoomDatabase extends RoomDatabase {


    public abstract BankDao bankDao();

    private static MyZakaatRoomDatabase INSTANCE;


    public static MyZakaatRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyZakaatRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyZakaatRoomDatabase.class, "my_zakaat_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}
