package eto.riswan.rumahsewa.helper;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import eto.riswan.rumahsewa.model.Rating;
import eto.riswan.rumahsewa.model.RumahSewa;
import eto.riswan.rumahsewa.model.User;

public class Database extends OrmLiteSqliteOpenHelper {

	// name of the database file
	private static final String DATABASE_NAME = "rumahsewa.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 2;

	// the DAO object we use to cache access from tables
	private Dao<RumahSewa, Long> rumahSewaDao = null;
	private RuntimeExceptionDao<RumahSewa, Long> rumahSewaRuntimeDao = null;
	private Dao<Rating, Long> ratingDao = null;
	private RuntimeExceptionDao<Rating, Long> ratingRuntimeDao = null;
	private Dao<User, Long> userDao = null;
	private RuntimeExceptionDao<User, Long> userRuntimeDao = null;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void close() {
		super.close();
	}

	public Dao<Rating, Long> getRating() throws SQLException {
		if (this.ratingDao == null) this.ratingDao = this.getDao(Rating.class);
		return this.ratingDao;
	}

	public RuntimeExceptionDao<Rating, Long> getRatingRuntime() {
		if (this.ratingRuntimeDao == null) this.ratingRuntimeDao = this.getRuntimeExceptionDao(Rating.class);
		return this.ratingRuntimeDao;
	}

	public Dao<RumahSewa, Long> getRumahSewa() throws SQLException {
		if (this.rumahSewaDao == null) this.rumahSewaDao = this.getDao(RumahSewa.class);
		return this.rumahSewaDao;
	}

	public RuntimeExceptionDao<RumahSewa, Long> getRumahSewaRuntime() {
		if (this.rumahSewaRuntimeDao == null)
			this.rumahSewaRuntimeDao = this.getRuntimeExceptionDao(RumahSewa.class);
		return this.rumahSewaRuntimeDao;
	}

	public Dao<User, Long> getUser() throws SQLException {
		if (this.userDao == null) this.userDao = this.getDao(User.class);
		return this.userDao;
	}

	public RuntimeExceptionDao<User, Long> getUserRuntime() {
		if (this.userRuntimeDao == null) this.userRuntimeDao = this.getRuntimeExceptionDao(User.class);
		return this.userRuntimeDao;
	}

	private void initializeValuesUserAccount() {
		// RuntimeExceptionDao<UserAccount, Long> dao = this.getUserAccountRuntime();
		//
		// UserAccount admin = new UserAccount(Default.AdminUid, Default.AdminPassword);
		// admin.Role = UserRole.ADMIN;
		// dao.createIfNotExists(admin);
		//
		// UserAccount dispatcher = new UserAccount(Default.DispatcherUid, Default.DispatcherPassword);
		// dispatcher.Role = UserRole.DISPATCHER;
		// dao.createIfNotExists(dispatcher);
		//
		// Log.e(Database.class.getName(), "Initialized default values for user accounts.");
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(Database.class.getName(), "onCreate");

			TableUtils.createTable(connectionSource, RumahSewa.class);
			TableUtils.createTable(connectionSource, Rating.class);
			TableUtils.createTable(connectionSource, User.class);

			this.initializeValuesUserAccount();
		} catch (SQLException e) {
			Log.e(Database.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(Database.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, RumahSewa.class, true);
			TableUtils.dropTable(connectionSource, Rating.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			// after we drop the old databases, we create the new ones
			this.onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(Database.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
}