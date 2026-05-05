package com.easysport.assistantappnewmb.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BetDao_Impl implements BetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Bet> __insertionAdapterOfBet;

  private final EntityDeletionOrUpdateAdapter<Bet> __deletionAdapterOfBet;

  public BetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBet = new EntityInsertionAdapter<Bet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bets` (`id`,`date`,`name`,`odds`,`stake`,`result`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Bet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getOdds());
        statement.bindDouble(5, entity.getStake());
        statement.bindString(6, entity.getResult());
      }
    };
    this.__deletionAdapterOfBet = new EntityDeletionOrUpdateAdapter<Bet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Bet entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insertBet(final Bet bet, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBet.insert(bet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBet(final Bet bet, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBet.handle(bet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Bet>> getAllBets() {
    final String _sql = "SELECT * FROM bets ORDER BY date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bets"}, new Callable<List<Bet>>() {
      @Override
      @NonNull
      public List<Bet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOdds = CursorUtil.getColumnIndexOrThrow(_cursor, "odds");
          final int _cursorIndexOfStake = CursorUtil.getColumnIndexOrThrow(_cursor, "stake");
          final int _cursorIndexOfResult = CursorUtil.getColumnIndexOrThrow(_cursor, "result");
          final List<Bet> _result = new ArrayList<Bet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Bet _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpOdds;
            _tmpOdds = _cursor.getString(_cursorIndexOfOdds);
            final double _tmpStake;
            _tmpStake = _cursor.getDouble(_cursorIndexOfStake);
            final String _tmpResult;
            _tmpResult = _cursor.getString(_cursorIndexOfResult);
            _item = new Bet(_tmpId,_tmpDate,_tmpName,_tmpOdds,_tmpStake,_tmpResult);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
