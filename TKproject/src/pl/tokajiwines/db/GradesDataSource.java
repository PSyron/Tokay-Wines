
package pl.tokajiwines.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pl.tokajiwines.models.Grade;
import pl.tokajiwines.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class GradesDataSource {
    // LogCat tag
    private static final String LOG = "GradesDataSource";
    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            "IdGrade", "Name"
    };

    public GradesDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.i(LOG, database + " ");
    }

    public void close() {
        if (database != null && database.isOpen()) dbHelper.close();
    }

    public long insertGrade(Grade grade) {
        Log.i(LOG, "insertGrade()");
        ContentValues values = new ContentValues();
        values.put("IdGrade", grade.mIdGrade);
        values.put("Name", grade.mName);
        long insertId = database.insert(DatabaseHelper.TABLE_GRADES, null, values);
        Log.i(LOG, "Grade with id: " + grade.mIdGrade + " inserted with id: " + insertId);
        return insertId;
    }

    public void deleteGrade(Grade grade) {
        long id = grade.mIdGrade;
        database.delete(DatabaseHelper.TABLE_GRADES, "IdGrade" + " = " + id, null);
        Log.i(LOG, "Deleted grade with id: " + id);
    }

    public void updateGrade(Grade gradeOld, Grade gradeNew) {
        ContentValues values = new ContentValues();
        values.put("IdGrade", gradeOld.mIdGrade);
        values.put("Name", gradeNew.mName);
        int rows = database.update(DatabaseHelper.TABLE_GRADES, values, "'" + gradeOld.mIdGrade
                + "' = '" + gradeNew.mIdGrade + "'", null);
        Log.i(LOG, "Updated grade with id: " + gradeOld.mIdGrade + " on: " + rows + " row(s)");
    }

    public List<Grade> getAllGrades() {
        Log.i(LOG, "getAllGrades()");
        List<Grade> grades = new ArrayList<Grade>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_GRADES, allColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Grade grade = cursorToGrade(cursor);
            grades.add(grade);
            cursor.moveToNext();
        }
        cursor.close();
        if (grades.isEmpty()) Log.w(LOG, "Grades are empty()");
        return grades;
    }

    private Grade cursorToGrade(Cursor cursor) {
        Grade grade = new Grade();
        grade.mIdGrade = cursor.getInt(0);
        grade.mName = cursor.getString(1);
        return grade;
    }
}
