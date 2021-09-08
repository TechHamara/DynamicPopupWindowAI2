package com.vedang;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.content.Context;
import android.widget.FrameLayout;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.YailList;

import java.util.HashMap;

@DesignerComponent(version = 1,
        description = " Popup Window Extension. Made by Vedang",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "")
@SimpleObject(external = true)

public class PopWindow extends AndroidNonvisibleComponent  {

    public Context context;
    public FrameLayout popView;
    public PopupWindow popupWindow;
    int density;
    HashMap<Integer, PopupWindow> windows = new HashMap<>();
    HashMap<PopupWindow, Integer> ids = new HashMap<>();

    public PopWindow(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
        density = (int) context.getResources().getDisplayMetrics().density;
    }

    @SimpleFunction
    public void ShowAt(int x, int y, int gravity, int id){
        windows.get(id).showAtLocation(popView, gravity, x,y);
    }

    @SimpleFunction
    public boolean IsShowing(int id){
        return windows.get(id).isShowing();
    }

    @SimpleFunction
    public void Dismiss(int id) {
        windows.get(id).dismiss();
    }

    @SimpleFunction
    public void CreatePopupWindow(AndroidViewComponent arrangement, int height, int width, boolean cancelable, int id){
        int pixelHeight = density * height;
        int pixelWidth = density * width;
        if (arrangement.getView().getParent() != null){
            ((ViewGroup) arrangement.getView().getParent()).removeView(arrangement.getView());
        }
        popView = (FrameLayout) arrangement.getView();
        popupWindow = new PopupWindow(popView, pixelWidth, pixelHeight, cancelable);
        windows.put(id, popupWindow);
        ids.put(popupWindow, id);
        windows.get(ids.get(popupWindow)).setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                OnDismiss(ids.get(popupWindow));
            }
        });
    }

    @SimpleFunction
    public void Unregister(int id){
        PopupWindow windowToUnregister = windows.get(id);
        windows.remove(id);
        ids.remove(windowToUnregister);
    }

    @SimpleFunction
    public boolean IsRegistered(int id){
        return windows.containsKey(id);
    }

    @SimpleFunction
    public YailList RegisteredIds(){
        return YailList.makeList(ids.values());
    }

    @SimpleFunction
    public void ShowAsDropdown(AndroidViewComponent component, boolean overlapAnchor, int id) {
        windows.get(id).setOverlapAnchor(overlapAnchor);
        windows.get(id).showAsDropDown(component.getView());
    }

    @SimpleFunction
    public void ShowAsDropdownWithCustomization(AndroidViewComponent component, int x, int y, int gravity, boolean overlapAnchor, int id){
        windows.get(id).setOverlapAnchor(overlapAnchor);
        windows.get(id).showAsDropDown(component.getView(), x, y, gravity);
    }

    @SimpleEvent
    public void OnDismiss(int id){
        EventDispatcher.dispatchEvent(this, "OnDismiss", id);
    }

    @SimpleProperty
    public int NoGravity(){
        return Gravity.NO_GRAVITY;
    }

    @SimpleProperty
    public int GravityBottom(){
        return Gravity.BOTTOM;
    }

    @SimpleProperty
    public int GravityTop(){
        return Gravity.TOP;
    }

    @SimpleProperty
    public int GravityCenter(){
        return Gravity.CENTER;
    }

    @SimpleProperty
    public int GravityCenterHorizontal(){
        return Gravity.CENTER_HORIZONTAL;
    }

    @SimpleProperty
    public int GravityCenterVertical(){
        return Gravity.CENTER_VERTICAL;
    }

    @SimpleProperty
    public int GravityLeft(){
        return Gravity.LEFT;
    }

    @SimpleProperty
    public int GravityRight(){
        return Gravity.RIGHT;
    }

}
