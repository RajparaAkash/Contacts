package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.model.City;
import com.aigestudio.wheelpicker.model.Province;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class WheelAreaPicker extends LinearLayout implements IWheelAreaPicker {
    private static final float ITEM_TEXT_SIZE = 18.0f;
    private static final int PROVINCE_INITIAL_INDEX = 0;
    private static final String SELECTED_ITEM_COLOR = "#5e99a0";
    private AssetManager mAssetManager;
    private List<City> mCityList;
    private List<String> mCityName;
    private Context mContext;
    private LayoutParams mLayoutParams;
    private List<Province> mProvinceList = getJsonDataFromAssets(this.mAssetManager);
    private List<String> mProvinceName;
    private WheelPicker mWPArea;
    private WheelPicker mWPCity;
    private WheelPicker mWPProvince;

    public WheelAreaPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initLayoutParams();
        initView(context);
        obtainProvinceData();
        addListenerToWheelPicker();
    }

    private List<Province> getJsonDataFromAssets(AssetManager assetManager) {
        List<Province> list;
        Exception e;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(assetManager.open("RegionJsonData.dat"));
            list = (List) objectInputStream.readObject();
            try {
                objectInputStream.close();
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            list = null;
            e.printStackTrace();
            return list;
        }
        return list;
    }

    private void initLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        this.mLayoutParams = layoutParams;
        layoutParams.setMargins(5, 5, 5, 5);
        this.mLayoutParams.width = 0;
    }

    private void initView(Context context) {
        setOrientation(0);
        this.mContext = context;
        this.mAssetManager = context.getAssets();
        this.mProvinceName = new ArrayList();
        this.mCityName = new ArrayList();
        this.mWPProvince = new WheelPicker(context);
        this.mWPCity = new WheelPicker(context);
        this.mWPArea = new WheelPicker(context);
        initWheelPicker(this.mWPProvince, 1.0f);
        initWheelPicker(this.mWPCity, 1.5f);
        initWheelPicker(this.mWPArea, 1.5f);
    }

    private void initWheelPicker(WheelPicker wheelPicker, float f) {
        this.mLayoutParams.weight = f;
        wheelPicker.setItemTextSize(dip2px(this.mContext, ITEM_TEXT_SIZE));
        wheelPicker.setCurved(true);
        wheelPicker.setLayoutParams(this.mLayoutParams);
        addView(wheelPicker);
    }

    private void obtainProvinceData() {
        for (Province province : this.mProvinceList) {
            this.mProvinceName.add(province.getName());
        }
        this.mWPProvince.setData(this.mProvinceName);
        setCityAndAreaData(0);
    }

    private void addListenerToWheelPicker() {
        this.mWPProvince.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            /* class com.aigestudio.wheelpicker.widgets.WheelAreaPicker.AnonymousClass1 */

            @Override // com.aigestudio.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                WheelAreaPicker wheelAreaPicker = WheelAreaPicker.this;
                wheelAreaPicker.mCityList = ((Province) wheelAreaPicker.mProvinceList.get(i)).getCity();
                WheelAreaPicker.this.setCityAndAreaData(i);
            }
        });
        this.mWPCity.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            /* class com.aigestudio.wheelpicker.widgets.WheelAreaPicker.AnonymousClass2 */

            @Override // com.aigestudio.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                WheelAreaPicker.this.mWPArea.setData(((City) WheelAreaPicker.this.mCityList.get(i)).getArea());
            }
        });
    }

    
    /* access modifiers changed from: public */
    private void setCityAndAreaData(int i) {
        this.mCityList = this.mProvinceList.get(i).getCity();
        this.mCityName.clear();
        for (City city : this.mCityList) {
            this.mCityName.add(city.getName());
        }
        this.mWPCity.setData(this.mCityName);
        this.mWPCity.setSelectedItemPosition(0);
        this.mWPArea.setData(this.mCityList.get(0).getArea());
        this.mWPArea.setSelectedItemPosition(0);
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelAreaPicker
    public String getProvince() {
        return this.mProvinceList.get(this.mWPProvince.getCurrentItemPosition()).getName();
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelAreaPicker
    public String getCity() {
        return this.mCityList.get(this.mWPCity.getCurrentItemPosition()).getName();
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelAreaPicker
    public String getArea() {
        return this.mCityList.get(this.mWPCity.getCurrentItemPosition()).getArea().get(this.mWPArea.getCurrentItemPosition());
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelAreaPicker
    public void hideArea() {
        removeViewAt(2);
    }

    private int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
