package com.devmasterteam.convidados.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devmasterteam.convidados.constants.DataBaseConstants;
import com.devmasterteam.convidados.constants.GuestConstants;
import com.devmasterteam.convidados.entity.GuestCountEntity;
import com.devmasterteam.convidados.entity.GuestEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistência de dados de convidados
 */
public class GuestRepository {

    private static GuestRepository INSTANCE;
    private GuestDataBaseHelper mGuestDataBaseHelper;

    // Previne que a classe seja instanciada - singleton
    private GuestRepository(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }
        this.mGuestDataBaseHelper = new GuestDataBaseHelper(context);
    }

    /**
     * Singleton
     */
    public static GuestRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GuestRepository(context);
        }
        return INSTANCE;
    }

    /**
     * Faz a listagem de todos os convidados
     */
    public List<GuestEntity> getInvited() {
        List<GuestEntity> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.mGuestDataBaseHelper.getReadableDatabase();

            // Colunas que serão retornadas
            String[] projection = {
                    DataBaseConstants.GUEST.COLUMNS.ID,
                    DataBaseConstants.GUEST.COLUMNS.NAME,
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE
            };

            // Linha única
            // Cursor cursor = db.rawQuery("select * from Guest", null);

            // Faz a seleção
            Cursor cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME, projection, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    GuestEntity guestEntity = new GuestEntity();
                    guestEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.ID)));
                    guestEntity.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.NAME)));
                    guestEntity.setConfirmed(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.PRESENCE)));

                    // Adiciona item a lista
                    list.add(guestEntity);
                }

                // Como verificar se um valor é nulo
                // cursor.isNull(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.CONFIRMED))
            }

            // Fecha cursor
            if (cursor != null) {
                cursor.close();
            }

            // Objeto convidado
            return list;

        } catch (Exception e) {
            return list;
        }
    }

    /**
     * Faz a listagem de todos os convidados confirmados
     */
    public List<GuestEntity> getConfirmed() {
        List<GuestEntity> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.mGuestDataBaseHelper.getReadableDatabase();

            // Colunas que serão retornadas
            String[] projection = {
                    DataBaseConstants.GUEST.COLUMNS.ID,
                    DataBaseConstants.GUEST.COLUMNS.NAME,
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE
            };

            // Filtro
            String selection = DataBaseConstants.GUEST.COLUMNS.PRESENCE + " = ?";
            String[] selectionArgs = {String.valueOf(GuestConstants.CONFIRMATION.PRESENT)};

            // Linha única
            // Cursor cursor = db.rawQuery("select * from Guest", null);

            // Faz a seleção
            Cursor cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    GuestEntity guestEntity = new GuestEntity();
                    guestEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.ID)));
                    guestEntity.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.NAME)));
                    guestEntity.setConfirmed(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.PRESENCE)));

                    // Adiciona item a lista
                    list.add(guestEntity);
                }

                // Como verificar se um valor é nulo
                // cursor.isNull(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.CONFIRMED))
            }

            // Fecha cursor
            if (cursor != null) {
                cursor.close();
            }

            // Objeto convidado
            return list;

        } catch (Exception e) {
            return list;
        }
    }

    /**
     * Faz a listagem de todos os convidados ausentes
     */
    public List<GuestEntity> getAbsent() {
        List<GuestEntity> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.mGuestDataBaseHelper.getReadableDatabase();

            // Colunas que serão retornadas
            String[] projection = {
                    DataBaseConstants.GUEST.COLUMNS.ID,
                    DataBaseConstants.GUEST.COLUMNS.NAME,
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE
            };

            // Filtro
            String selection = DataBaseConstants.GUEST.COLUMNS.PRESENCE + " = ?";
            String[] selectionArgs = {String.valueOf(GuestConstants.CONFIRMATION.ABSENT)};

            // Linha única
            // Cursor cursor = db.rawQuery("select * from Guest", null);

            // Faz a seleção
            Cursor cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    GuestEntity guestEntity = new GuestEntity();
                    guestEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.ID)));
                    guestEntity.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.NAME)));
                    guestEntity.setConfirmed(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.GUEST.COLUMNS.PRESENCE)));

                    // Adiciona item a lista
                    list.add(guestEntity);
                }

                // Como verificar se um valor é nulo
                // cursor.isNull(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.CONFIRMED))
            }

            // Fecha cursor
            if (cursor != null) {
                cursor.close();
            }

            // Objeto convidado
            return list;

        } catch (Exception e) {
            return list;
        }
    }

    /**
     * Carrega convidado
     */
    public GuestEntity load(int id) {
        GuestEntity guestEntity = new GuestEntity();
        try {
            SQLiteDatabase db = this.mGuestDataBaseHelper.getReadableDatabase();

            // Colunas que serão retornadas
            String[] projection = {
                    DataBaseConstants.GUEST.COLUMNS.ID,
                    DataBaseConstants.GUEST.COLUMNS.NAME,
                    DataBaseConstants.GUEST.COLUMNS.PRESENCE
            };

            // Filtro
            String selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            // Linha única
            // Cursor cursor = db.rawQuery("select * from Guest where _id = " + String.valueOf(id), null);

            // Faz a seleção
            Cursor cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestEntity.setId(id);
                guestEntity.setName(cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME)));
                guestEntity.setConfirmed(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)));
            }

            // Fecha cursor
            if (cursor != null) {
                cursor.close();
            }

            // Objeto convidado
            return guestEntity;

        } catch (Exception e) {
            return guestEntity;
        }
    }

    /**
     * Carrega quantidade de convidados no total, ausentes e presentes
     */
    public GuestCountEntity loadGuestsCount() {
        GuestCountEntity guestCount = new GuestCountEntity(0, 0, 0);

        try {

            // Para fazer leitura de dados
            SQLiteDatabase db = this.mGuestDataBaseHelper.getReadableDatabase();
            Cursor cursor;

            // Convidados presentes
            String queryPresence = "select count(*) from " + DataBaseConstants.GUEST.TABLE_NAME + " where "
                    + DataBaseConstants.GUEST.COLUMNS.PRESENCE + " = " + GuestConstants.CONFIRMATION.PRESENT;
            cursor = db.rawQuery(queryPresence, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestCount.setTotalPresent(cursor.getInt(0));
            }

            // Convidados ausentes
            String queryAbsent = "select count(*) from " + DataBaseConstants.GUEST.TABLE_NAME + " where "
                    + DataBaseConstants.GUEST.COLUMNS.PRESENCE + " = " + GuestConstants.CONFIRMATION.ABSENT;
            cursor = db.rawQuery(queryAbsent, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestCount.setTotalAbsent(cursor.getInt(0));
            }

            // Total de Convidados
            String queryAllInvited = "select count(*) from " + DataBaseConstants.GUEST.TABLE_NAME;
            cursor = db.rawQuery(queryAllInvited, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                guestCount.setTotalGuests(cursor.getInt(0));
            }

            // Fecha cursor
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            return guestCount;
        }
        return guestCount;
    }

    /**
     * Remove convidado
     */
    public Boolean remove(int id) {
        try {
            SQLiteDatabase db = this.mGuestDataBaseHelper.getWritableDatabase();

            String whereClause = "id=?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            db.delete(DataBaseConstants.GUEST.TABLE_NAME, whereClause, whereArgs);

            // Linha única
            // db.delete(DataBaseConstants.GUEST.TABLE_NAME, DataBaseConstants.GUEST.COLUMNS.ID + " = " + String.valueOf(id), null);

            // Registro removido
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Insere convidado
     */
    public Boolean insert(GuestEntity guestEntity) {
        try {

            // Para fazer escrita de dados
            SQLiteDatabase db = this.mGuestDataBaseHelper.getWritableDatabase();

            ContentValues insertValues = new ContentValues();
            insertValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guestEntity.getName());
            insertValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guestEntity.getConfirmed());
            db.insert(DataBaseConstants.GUEST.TABLE_NAME, null, insertValues);

            // Registro inserido
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Atualiza convidado
     */
    public Boolean update(GuestEntity guestEntity) {
        try {
            SQLiteDatabase db = this.mGuestDataBaseHelper.getWritableDatabase();

            // Valores que serão atualizados
            ContentValues updateValues = new ContentValues();
            updateValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guestEntity.getName());
            updateValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guestEntity.getConfirmed());

            // Critério de seleção
            String selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?";
            String[] selectionArgs = {String.valueOf(guestEntity.getId())};

            // Executa
            db.update(DataBaseConstants.GUEST.TABLE_NAME, updateValues, selection, selectionArgs);

            // Registro inserido
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
