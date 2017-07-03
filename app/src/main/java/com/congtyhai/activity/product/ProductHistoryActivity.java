package com.congtyhai.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.ProductHistoryAdapter;
import com.congtyhai.model.data.DHistoryProductScan;
import com.congtyhai.util.RealmController;

import java.util.List;

public class ProductHistoryActivity extends BaseActivity {

    private ListView listView;
    private List<DHistoryProductScan> historyProductScen;
    private ProductHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        createToolbar();

        listView = (ListView) findViewById(R.id.list);
        historyProductScen = RealmController.getInstance().getHistoryProductScan();
        adapter = new ProductHistoryAdapter(this, historyProductScen);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DHistoryProductScan history = historyProductScen.get(position);
                Intent intent = new Intent(ProductHistoryActivity.this, ProductHistoryDetailActivity.class);
                intent.putExtra("HistoryId", history.getId());
                startActivity(intent);
            }
        });
    }
}
