package com.devmasterteam.tasks.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.devmasterteam.tasks.constants.DataBaseConstants;
import com.devmasterteam.tasks.entity.PriorityEntity;

import java.util.ArrayList;
import java.util.List;

public class PriorityRepository {

    private static PriorityRepository INSTANCE;
    private TaskDataBaseHelper mUltraTaskDataBaseHelper;

    // Previne que a classe seja instanciada - singleton
    private PriorityRepository(Context context) {
        this.mUltraTaskDataBaseHelper = new TaskDataBaseHelper(context);
    }

    /**
     * Singleton
     */
    public static PriorityRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PriorityRepository(context);
        }
        return INSTANCE;
    }

    /**
     * Adiciona lista de prioridades no banco de dados
     */
    public void insert(List<PriorityEntity> list) {
        String sql = "insert into Priority (_id, description) values (?, ?);";
        SQLiteDatabase db = this.mUltraTaskDataBaseHelper.getWritableDatabase();
        db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);
        for (PriorityEntity priorityEntity : list) {
            stmt.bindLong(1, priorityEntity.getId());
            stmt.bindString(2, priorityEntity.getDescription());

            stmt.executeInsert();
            stmt.clearBindings();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * Carrega todos os prioridades
     */
    public List<PriorityEntity> getList() {
        List<PriorityEntity> list = new ArrayList<>();

        try {
            Cursor cursor;
            SQLiteDatabase db = this.mUltraTaskDataBaseHelper.getReadableDatabase();

            // Lista de prioridades
            cursor = db.rawQuery("select * from Priority", null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    PriorityEntity guestEntity = new PriorityEntity();
                    guestEntity.setId((cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.PRIORITY.COLUMNS.ID))));
                    guestEntity.setDescription((cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION))));

                    // Adiciona item a lista
                    list.add(guestEntity);
                }
            }

            // Fecha cursor
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            return list;
        }

        // Retorno objeto com dados
        return list;
    }

    /**
     * Remove todos as prioridades do banco de dados
     */
    public void clear() {
        SQLiteDatabase db = this.mUltraTaskDataBaseHelper.getReadableDatabase();
        db.delete(DataBaseConstants.PRIORITY.TABLE_NAME, null, null);
        db.close();
    }

}
