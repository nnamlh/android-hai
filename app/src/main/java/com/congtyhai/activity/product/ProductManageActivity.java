package com.congtyhai.activity.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.activity.search.FindReceiverActivity;
import com.congtyhai.activity.search.ShowAgencyActivity;
import com.congtyhai.adapter.GeneralAdapter;
import com.congtyhai.adapter.ProductManageAdapter;
import com.congtyhai.model.send.CheckLocationRequest;
import com.congtyhai.model.receive.GeneralInfo;
import com.congtyhai.model.HaiLocation;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.model.receive.ResultUpdate;
import com.congtyhai.model.send.StaffHelpRequest;
import com.congtyhai.model.send.UpdateProductInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManageActivity extends BaseActivity {

    @BindView(R.id.btnsend)
    Button btnSend;
    @BindView(R.id.btnscan)
    Button btnScan;
    @BindView(R.id.listproduct)
    ListView listView;
    ProductManageAdapter adapter;

    GeneralAdapter adapterResult;

    @BindView(R.id.txtcount)
    TextView txtCount;

    @BindView(R.id.edit_input)
    EditText editText;
    @BindView(R.id.edit_receive)
    EditText eReceiver;

    boolean isReset = true;

    @BindView(R.id.layout_receiver)
    View lReceiver;

    String status;

    @BindView(R.id.layout_agency)
    View lAgency;
    @BindView(R.id.edit_agency)
    EditText txtAgency;

    String title;

    String companyCode = "89352433";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        createToolbar();

        ButterKnife.bind(this);

        Intent intent = getIntent();

        status = intent.getStringExtra("STATUS");

        if (status.equals(HaiSetting.getInstance().PRODUCT_IMPORT)) {
            title = "Nhập kho";
            eReceiver.setText("");
        } else if (status.equals(HaiSetting.getInstance().PRODUCT_EXPORT)) {
            title = "Xuất kho";
            lReceiver.setVisibility(View.VISIBLE);
        } else if (status.equals(HaiSetting.getInstance().PRODUCT_HELP_SCAN)) {
            title = "Nhập giúp";
            lAgency.setVisibility(View.VISIBLE);
        } else if (status.equals(HaiSetting.getInstance().PRODUCT_TRANSPORT)){
            title = "Điều kho";
            status = HaiSetting.getInstance().PRODUCT_EXPORT;
            lReceiver.setVisibility(View.VISIBLE);
        }
        else {
            onBackPressed();
        }

        getSupportActionBar().setTitle(title);

        HaiSetting.getInstance().setAGENCY("");
        HaiSetting.getInstance().setAGENCYNAME("");

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isReset)
                    resetList();

                Intent intent = new Intent(ProductManageActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        HaiSetting.getInstance().resetListProduct();

        adapter = new ProductManageAdapter(this, HaiSetting.getInstance().getLIST_PRODUCT());


        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlert(i);
                return false;
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HaiSetting.getInstance().countListProduct() == 0) {
                    showAlert("Cảnh báo", "Nhâp danh sách sản phẩm trước khi cập nhật.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                } else
                    showAlertUpdate();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.input_text || id == EditorInfo.IME_NULL) {
                    addProduct();

                    return true;
                }
                return false;
            }
        });

        eReceiver.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                + android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        new ReadDataTask().execute();

    }

    public void addProductClick(View view) {
        addProduct();
    }

    private void addProduct() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            showAlert("Cảnh báo", "Nhập mã sản phẩm.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {

            if (editText.getText().length() < 9) {
                showAlert("Cảnh báo", "Không hợp lệ.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }  else {
                if (!isReset)
                    resetList();

                HaiSetting.getInstance().addListProduct(companyCode + editText.getText().toString());
                adapter.notifyDataSetChanged();
                txtCount.setText("Tổng số lượng: " + HaiSetting.getInstance().countListProduct());
                editText.setText("");
            }
        }
    }

    private String chooseStt() {
        return status;
    }

    public void findClick(View view) {
        Intent intent = new Intent(ProductManageActivity.this, FindReceiverActivity.class);
        startActivity(intent);
    }

    private void sendInfo() {

        showpDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        UpdateProductInfo info = new UpdateProductInfo();

        info.setStatus(chooseStt());
        info.setUser(HaiSetting.getInstance().USER);
        info.setToken(HaiSetting.getInstance().TOKEN);
        info.setProducts(HaiSetting.getInstance().toProductArrays());
        info.setReceiver(eReceiver.getText().toString());

        Call<ResultUpdate> call = apiService.updateProduct(info);

        call.enqueue(new Callback<ResultUpdate>() {
            @Override
            public void onResponse(Call<ResultUpdate> call,
                                   final Response<ResultUpdate> response) {
                hidepDialog();
                if ("1".equals(response.body().getId())) {
                    isReset = false;
                    HaiSetting.getInstance().resetListProduct();

                    int countErorr = 0;
                    int countSucces = 0;
                    for (GeneralInfo item : response.body().getProducts()) {
                        if (item.getSuccess() == 1)
                            countSucces++;
                        else
                            countErorr++;
                    }
                    String msg = "Tổng công : " + response.body().getProducts().size() + "\n" + "Thành công: " + countSucces + "\n" + "Lỗi: " + countErorr;
                    showAlert("Thông báo", msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            refeshUpdateList(response.body().getProducts());
                        }
                    });


                } else {
                    showAlert("Lỗi cập nhật", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResultUpdate> call, Throwable t) {
                hidepDialog();
                showAlert("Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

    private void sendStaffHelp(int near) {

        showpDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HaiLocation location = getCurrentLocation();
        StaffHelpRequest info = new StaffHelpRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, HaiSetting.getInstance().toProductArrays(), location.getLatitude(), location.getLongitude(), txtAgency.getText().toString(), near);

        Call<ResultUpdate> call = apiService.updateAgencyimport(info);

        call.enqueue(new Callback<ResultUpdate>() {
            @Override
            public void onResponse(Call<ResultUpdate> call,
                                   final Response<ResultUpdate> response) {
                hidepDialog();
                if ("1".equals(response.body().getId())) {
                    isReset = false;
                    HaiSetting.getInstance().resetListProduct();
                    int countErorr = 0;
                    int countSucces = 0;
                    for (GeneralInfo item : response.body().getProducts()) {
                        if (item.getSuccess() == 1)
                            countSucces++;
                        else
                            countErorr++;
                    }
                    String msg = "Tổng công : " + response.body().getProducts().size() + "\n" + "Thành công: " + countSucces + "\n" + "Lỗi: " + countErorr;
                    showAlert("Thông báo", msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            refeshUpdateList(response.body().getProducts());
                        }
                    });


                } else {
                    showAlert("Lỗi cập nhật", response.body().getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResultUpdate> call, Throwable t) {
                hidepDialog();
                showAlert("Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }


    private void refeshUpdateList(List<GeneralInfo> products) {
        adapterResult = new GeneralAdapter(ProductManageActivity.this, products);

        listView.setAdapter(adapterResult);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        txtCount.setText("TỔNG SỐ LƯỢNG : " + products.size());
    }

    private void sendCheckDistance() {

        showpDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HaiLocation location = getCurrentLocation();
        CheckLocationRequest info = new CheckLocationRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, txtAgency.getText().toString(), location.getLatitude(), location.getLongitude());

        Call<ResultInfo> call = apiService.checkLocationDistance(info);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   final Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        // thanh cong
                        sendStaffHelp(1);

                    } else if ("2".equals(response.body().getId())) {
                        // ngoai dai ly
                        showAlertCancel("Cảnh báo", "Chúng tôi đang định vị bạn ở ngoài đại lý, bạn đồng ý tiếp tục cập nhật ?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendStaffHelp(2);
                            }
                        });
                    } else {
                        showAlert("Lỗi cập nhật", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }

                } else {
                    showAlert("Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
                showAlert("Lỗi đường truyền", "Vui lòng kiểm tra lại wifi hoặc 3G trên điện thoại của bạn.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }


    private void showAlertUpdate() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProductManageActivity.this);

        dialog.setTitle("Cập nhật")
                .setMessage("Cập nhật sản phẩm.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status.equals(HaiSetting.getInstance().PRODUCT_HELP_SCAN)) {
                            if (checkLocation()) {
                                if (TextUtils.isEmpty(txtAgency.getText().toString())) {
                                    Toast.makeText(ProductManageActivity.this, "Nhập mã đại lý", Toast.LENGTH_LONG).show();
                                } else {
                                    sendCheckDistance();
                                }
                            }
                        } else {
                            sendInfo();
                        }
                    }
                }).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void findAgencyClick(View view) {
        Intent intent = new Intent(ProductManageActivity.this, ShowAgencyActivity.class);
        startActivity(intent);
    }

    private void showAlert(final int pos) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProductManageActivity.this);
        String code = HaiSetting.getInstance().getLIST_PRODUCT().get(pos);
        dialog.setTitle("Thông báo")
                .setMessage("Xóa mã : " + code)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        HaiSetting.getInstance().getLIST_PRODUCT().remove(pos);
                        adapter.notifyDataSetChanged();
                        txtCount.setText("TỔNG SỐ LƯỢNG : " + HaiSetting.getInstance().countListProduct());
                    }
                }).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        txtCount.setText("Tổng số lượng : " + HaiSetting.getInstance().countListProduct());
        eReceiver.setText(HaiSetting.getInstance().getAGENCY());
        txtAgency.setText(HaiSetting.getInstance().getAGENCY());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_manage, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_history:
                showAlert("Xóa danh sách sản phẩm", "Danh sách sản phẩm bạn mới quét sẽ xóa hết toàn bộ.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetList();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        showAlertCancel("Cảnh báo", "Bạn thoát màn hình này nếu chưa cập nhật dữ liệu sẽ không được lưu lại !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProductManageActivity.super.onBackPressed();
            }
        });


    }

    private void resetList() {
        isReset = true;
        HaiSetting.getInstance().resetListProduct();
        adapter = new ProductManageAdapter(this, HaiSetting.getInstance().getLIST_PRODUCT());

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlert(i);
                return false;
            }
        });

        listView.setAdapter(adapter);

        txtCount.setText("Tổng số lượng : " + HaiSetting.getInstance().countListProduct());
    }


    private class ReadDataTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            try{
                getListProduct();

            } catch (Exception e) {

            }
            return  "";        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(String result) {
            hidepDialog();
        }
    }
}
