package com.zzrv5.http.slice;

import com.zzrv5.http.ResourceTable;
import com.zzrv5.http.util.ZZRCallBack;
import com.zzrv5.http.util.ZZRHttpUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;

public class MainAbilitySlice extends AbilitySlice {



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout);
        Text text = (Text) findComponentById(ResourceTable.Id_text);
        text.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                text.setText("正在get访问,请稍后...");
                String url="https://zzrv5.github.io/test/zzr.html";
                ZZRHttpUtil.get(url, new ZZRCallBack.ZZRCallBackString() {
                    @Override
                    public void onFailure(int code, String errorMessage) {
                        //http访问出错了，注意此部分内容在主线程中工作，可以更新UI等操作，但请不要执行阻塞操作。
                        text.setText("get访问出错："+errorMessage);
                    }
                    @Override
                    public void onResponse(String response) {
                        //http访问成功，注意此部分内容在主线程中工作，可以更新UI等操作，但请不要执行阻塞操作。
                        text.setText("服务器页面内容为（get）："+response);
                    }
                });
            }
        });

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
