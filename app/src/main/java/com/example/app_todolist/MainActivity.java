package com.example.app_todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataBase dataBase;
    ListView lvCongViec;
    ArrayList<CongViec> array_CongViec;
    CongViec_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.listView_cv);
        array_CongViec = new ArrayList<>();

        adapter = new CongViec_Adapter(this, R.layout.dong_cong_viec, array_CongViec); // (context,layout,mảng)
        lvCongViec.setAdapter(adapter);  // phải  gán cái này nó mưới hiện

// -------------------------------------------------------------------------------------

        // tạo Database ghichu
        dataBase = new DataBase(this, "GhiChu.sqlite", null, 1);

//  -1-    tạo bảng công việc
        dataBase.QueryData_("CREATE TABLE IF NOT EXISTS Bang_CongViec_Moi(Id INTEGER PRIMARY KEY AUTOINCREMENT , TenCV VARCHAR(200))");        // tạo 1 cái bảng nếu không tồn tại (nếu nó k trùng ),tên bảng =>  Bang_CongViec_Moi(ID LÀ SỐ NGUYÊN LÀM KHÓA CHÍNH TĂNG DẦN),TÊN CÔNG VIỆC  là chuỗi có chiều dài 200

//  -2-   Insert data  - thêm dữ liệu vao database  //=> ghi vào công việc  => lưu ý khi mà thêm xong thì đóng nó lại
        //     dataBase.QueryData_("INSERT INTO Bang_CongViec_Moi VALUES(null,'Android')");

//  -3-   > xuất ra Màn hinh - Select data
        GetDataCongViec();
    }
// -------------------------------------------------------------------------------------
public void Dialog_xoa_CV(String tenCV,int id___CV){
    AlertDialog.Builder dialog_xoa = new AlertDialog.Builder(this);   // tạo nhanh dialog
    dialog_xoa.setMessage("Bạn có ,muốn xóa công việc => " + tenCV + " <= không ?");

    // => them tạo 2 cái nút có và không
    dialog_xoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dataBase.QueryData_("DELETE FROM Bang_CongViec_Moi WHERE Id = '"+id___CV+"'");
            Toast.makeText(MainActivity.this, "Đã Xóa "+tenCV, Toast.LENGTH_SHORT).show();
            GetDataCongViec(); // xóa xong thì load lại công việc
        }
    });


    dialog_xoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });

    dialog_xoa.show();
}

    // -------------------------------------------------------------------------------------
    public void Dialog_Sua_CongViec(String ten_Cv, final int id) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sua);

        EditText edt_TenCV = (EditText) dialog.findViewById(R.id.editText_chinhsua);
        Button btn_XacNhan = (Button) dialog.findViewById(R.id.button_capNhat__);
        Button btn_huy = (Button) dialog.findViewById(R.id.button_huy___);

        edt_TenCV.setText(ten_Cv);     //  hiện cái tên mk muốn sửa lên

        btn_XacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMoi = edt_TenCV.getText().toString().trim();
                dataBase.QueryData_("UPDATE Bang_CongViec_Moi SET TenCV = '" + tenMoi + "' WHERE Id = '" + id + "'"); // chú ý điều kiện where
                Toast.makeText(MainActivity.this, "Đã Cập Nhật", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();  // load lại công việc
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // -------------------------------------------------------------------------------------
    private void GetDataCongViec() {
        //- > xuất ra Màn hinh - Select data
        Cursor dataCongViec = dataBase.GetData_("SELECT * FROM Bang_CongViec_Moi");  // con trỏ _ lấy dữ liệ từ bảng
        array_CongViec.clear(); // xóa mảng trước khi adđ - để tránh lập lại cái cũ
        while (dataCongViec.moveToNext()) {  // khi nó còn duyệt dữ liệu
            String ten = dataCongViec.getString(1);  //=> vị trí đâu tiên là thứ 0 (tên) , tên và id là thứ 1
            int id = dataCongViec.getInt(0);   // id là thứ 0  - tên là thứ 1
            array_CongViec.add(new CongViec(id, ten));
        }

        adapter.notifyDataSetChanged();// cập nhật dữ liệu
    }
// -------------------------------------------------------------------------------------

    @Override     // hiển thị cái menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // -------------------------------------------------------------------------------------
    // băt sự kiện menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == (R.id.menuAdd)) { // biến item id = id của cái item tạo trong menu
            Dialog_Them();
        }
        return super.onOptionsItemSelected(item);
    }
// -------------------------------------------------------------------------------------

    private void Dialog_Them() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        // bắt sk => khai báo ax
        EditText edtTen = (EditText) dialog.findViewById(R.id.editText_them_cong_viec);
        Button btn_Them = (Button) dialog.findViewById(R.id.button_Them);
        Button btn_Huy = (Button) dialog.findViewById(R.id.button_Huy);

        btn_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenCv = edtTen.getText().toString();
                if (tenCv.equals("")) {    // = rỗng
                    Toast.makeText(MainActivity.this, "Vui Lòng Nhập tên công Việc", Toast.LENGTH_SHORT).show();
                } else {
                    dataBase.QueryData_("INSERT INTO Bang_CongViec_Moi VALUES(null,'" + tenCv + "')"); // tách chuỗi nối tiến
                    Toast.makeText(MainActivity.this, "Đã Thêm,", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();   // thêm xong tắt nó đi  và --- show cái công việc thêm ra
                    GetDataCongViec();
                }
            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();  // hủy tắt cái show ra
            }
        });

        dialog.show();
    }
}