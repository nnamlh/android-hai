package com.congtyhai.activity.product;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.event.EventSendActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.ProductChooseFuncAdapter;
import com.congtyhai.model.send.AuthInfo;
import com.congtyhai.model.ProductChooseFuncInfo;
import com.congtyhai.model.receive.ProductFunction;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RecyclerTouchListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseProductFuncActivity extends BaseActivity {

    @BindView(R.id.productchooserecycler) RecyclerView recyclerView;
    List<ProductChooseFuncInfo> infos;
    ProductChooseFuncAdapter adapter;
    String KEY_FUNCTION = "functionproduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product_func);
        createToolbar();
        //
        ButterKnife.bind(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        infos = new ArrayList<>();

        adapter = new ProductChooseFuncAdapter(infos, this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProductChooseFuncInfo info = infos.get(position);
                Intent intent = null;
                switch (info.getCode()) {

                    case "importproduct":
                        intent= new Intent(ChooseProductFuncActivity.this, ProductManageActivity.class);
                        intent.putExtra("STATUS", HaiSetting.PRODUCT_IMPORT);
                        break;
                    case "exportproduct":
                        intent= new Intent(ChooseProductFuncActivity.this, ProductManageActivity.class);
                        intent.putExtra("STATUS", HaiSetting.PRODUCT_EXPORT);
                        break;
                    case "staffimportproduct":
                        intent= new Intent(ChooseProductFuncActivity.this, ProductManageActivity.class);
                        intent.putExtra("STATUS", HaiSetting.PRODUCT_HELP_SCAN);
                        break;
                    case "savepoint":
                        intent = new Intent(ChooseProductFuncActivity.this, EventSendActivity.class);
                        break;
                    case "tracking":
                        intent = new Intent(ChooseProductFuncActivity.this, TrackingActivity.class);
                        break;

                }

                if(intent != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getFunction();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void initList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String function = sharedPref.getString(KEY_FUNCTION, "");

       try{
           JSONArray jsonArray = new JSONArray(function);

           for (int i = 0; i < jsonArray.length(); i++) {
               String code = jsonArray.getString(i);
               switch (code) {

                   case "importproduct":
                       infos.add(new ProductChooseFuncInfo(code, "NHẬP KHO", R.mipmap.ic_import));
                       break;
                   case "savepoint":
                       infos.add(new ProductChooseFuncInfo(code, "TÍCH ĐIỂM", R.mipmap.ic_savepoint));
                       break;
                   case "tracking":
                       infos.add(new ProductChooseFuncInfo(code, "TRA CỨU", R.mipmap.ic_checkproduct));
                       break;
                   case "staffimportproduct":
                       infos.add(new ProductChooseFuncInfo(code, "QUÉT NHẬP GIÙM", R.mipmap.ic_staffscan));
                       break;
                   case "exportproduct":
                       infos.add(new ProductChooseFuncInfo(code, "XUẤT KHO", R.mipmap.ic_export));
                       break;
               }
           }
           adapter.notifyDataSetChanged();
       } catch (Exception e){

       }


    }

    private void getFunction() {
        showpDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        final AuthInfo info = new AuthInfo(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN);

        Call<ProductFunction> call = apiService.functionProduct(info);

        call.enqueue(new Callback<ProductFunction>() {
            @Override
            public void onResponse(Call<ProductFunction> call,
                                   Response<ProductFunction> response) {
                if(response.body() != null) {
                    if ("1".equals(response.body().getId())) {

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(KEY_FUNCTION, response.body().getFunction());
                        editor.commit();
                        initList();


                    } else {
                        Toast.makeText(ChooseProductFuncActivity.this,response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    initList();
                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<ProductFunction> call, Throwable t) {
                Log.e("error:", t.getMessage());
                initList();
                hidepDialog();
            }
        });
    }

}
