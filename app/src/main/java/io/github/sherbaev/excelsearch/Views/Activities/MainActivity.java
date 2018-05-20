package io.github.sherbaev.excelsearch.Views.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.github.sherbaev.excelsearch.App;
import io.github.sherbaev.excelsearch.db.Pojo;
import io.github.sherbaev.excelsearch.R;
import io.github.sherbaev.excelsearch.db.PojoDao;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    File file;
    Button btnUpDirectory, btnSdCard, openRv;
    ListView lInternalStorage;
    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;
    ArrayList<Pojo> uploadData;
    private String[] FilePathStorage;
    private String[] FileNameStorage;
    private File[] listFile;
    private PojoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App app=(App)  getApplication();
        dao=app.getMyDataBase().loadPojoDao();
        init();
        checkFilePermission();
        lInternalStorage.setOnItemClickListener((parent, view, position, id) -> {
            lastDirectory = pathHistory.get(count);
            if (lastDirectory.equals(parent.getItemAtPosition(position))) {
                Log.d(TAG, "onCreate: lvExternalStorage : Selected a file for upload :" + lastDirectory);
                readExcelData(lastDirectory);
            } else {
                count++;
                pathHistory.add(count, (String) parent.getItemAtPosition(position));
                checkInternalStorage();
                Log.d(TAG, "lvInternalStorage : " + pathHistory.get(position));
            }

        });
        btnUpDirectory.setOnClickListener(v -> {
            if (count == 0) {
                Log.d(TAG, "BtnUpDirectory: You have reached the highest level directory");
            } else {
                pathHistory.remove(count);
                count--;
                checkInternalStorage();
                Log.d(TAG, "onCreate: btnUpDirectory : " + pathHistory.get(count));
            }
        });
        btnSdCard.setOnClickListener(v -> {
            count = 0;
            pathHistory = new ArrayList<>();
            pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
            Log.d(TAG, "btnSdCard : " + pathHistory.get(count));
            checkInternalStorage();
        });
        openRv.setOnClickListener(v -> {
            startActivity(new Intent(this, ListActivity.class));
        });
    }

    private void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: ");
        //input file
        File inputFile = new File(filePath);
        try {
            InputStream in = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outer loop,loop through rows
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop,loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are many columns on the excel sheet
                    if (c > 2) {
                        Log.e(TAG, "readExcelData: Error excel format is incorrect");
                        toastMessage(" Error excel format is incorrect");
                        break;
                    } else {
                        String value = getCellsAsString(row, c, formulaEvaluator);
                        String cellsInfo = "r:" + r + " ; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row : " + cellsInfo);
                        sb.append(value + ",");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExcelData: StringBuilder : " + sb.toString());
            parseStringBuilder(sb);
        } catch (IOException a) {
            Log.e(TAG, "readExcelData: " + a.getMessage());
        }
    }

    private void parseStringBuilder(StringBuilder mStringBuilder) {
        String[] rows = mStringBuilder.toString().split(":");
        //Add to the ArrayList<Pojo>row by row
        for (int i = 0; i < rows.length; i++) {
            //Split the columns of the String
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles
            try {
                String name = columns[0];
                String surName = columns[1];
                String cellInfo = "name : " + name + " | surName : " + surName;
                Log.d(TAG, "parseStringBuilder: " + cellInfo);
                Pojo pojo=new Pojo();
                pojo.name=name;
                pojo.surName=surName;
                dao.addPojo(pojo);
                Log.d(TAG, "Pojo Added : "+pojo.name+" "+pojo.surName);
            } catch (NumberFormatException e) {
                Log.e(TAG, "parseStringBuilder: " + e.getMessage());
            }
        }
        printDataLog();
    }

    private void printDataLog() {
        for (int i = 0; i < uploadData.size(); i++) {
            String name = uploadData.get(i).name;
            String surName = uploadData.get(i).surName;
            Log.d(TAG, "printDataLog: " + name + "  " + surName);
        }
    }

    private String getCellsAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                        value = dateFormat.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "getCellsAsString: " + e.getMessage());
        }
        return value;

    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: ");
        try {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found");
            } else {
                file = new File(pathHistory.get(count));
            }
            listFile = file.listFiles();
            //String array for FileNameStrings
            FileNameStorage = new String[listFile.length];
            //String array for FilePathStrings
            FilePathStorage = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++) {
                FilePathStorage[i] = listFile[i].getAbsolutePath();
                FileNameStorage[i] = listFile[i].getName();
            }
            for (int i = 0; i < listFile.length; i++) {
                Log.d(TAG, "checkInternalStorage: FileName" + listFile[i].getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, FilePathStorage);
            lInternalStorage.setAdapter(adapter);
        } catch (NullPointerException e) {
            Log.e(TAG, "checkInternalStorage: " + e.getMessage());
        }
    }

    private void checkFilePermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck;
            permissionCheck = this.checkSelfPermission(Manifest
                    .permission
                    .WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }
        } else {
            Log.d(TAG, "No need to check permission ");
        }
    }

    private void init() {
        lInternalStorage = findViewById(R.id.lInternalStorage);
        btnUpDirectory = findViewById(R.id.btnUpDirectory);
        btnSdCard = findViewById(R.id.btnSdCard);
        openRv = findViewById(R.id.btnRv);
        uploadData = new ArrayList<>();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
