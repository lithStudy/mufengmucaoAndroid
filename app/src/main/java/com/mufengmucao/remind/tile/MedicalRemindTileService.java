package com.mufengmucao.remind.tile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.core.content.ContextCompat;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.mufengmucao.remind.constants.MyConstant;
import com.mufengmucao.remind.helper.MedicalStateDataUtil;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MedicalRemindTileService extends TileService {

    private BroadcastReceiver updateReceiver;
    @Override
    public void onStartListening() {
        super.onStartListening();
        // 处理磁贴变得可见时的逻辑
        updateTileState(MedicalStateDataUtil.readDoneStateForToday(this));

        //监听更新事件，更新磁贴状态
        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTileState(MedicalStateDataUtil.readDoneStateForToday(getApplicationContext()));
            }
        };
        IntentFilter filter = new IntentFilter(MyConstant.ACTION_MESSAGE_UPDATE);
        ContextCompat.registerReceiver(this.getApplicationContext(), updateReceiver, filter, ContextCompat.RECEIVER_EXPORTED);

        IntentFilter filter1 = new IntentFilter(MyConstant.ACTION_INIT);
        ContextCompat.registerReceiver(this.getApplicationContext(), updateReceiver, filter1, ContextCompat.RECEIVER_EXPORTED);
    }




    @Override
    public void onClick() {
        super.onClick();
        boolean currentState = MedicalStateDataUtil.readDoneStateForToday(this);
        MedicalStateDataUtil.writeDoneStateForToday(this,!currentState); // Toggle the state
        updateTileState(!currentState);
        // 发送广播通知Activity更新UI
        Intent intent = new Intent(MyConstant.ACTION_TILE_UPDATE);
        sendBroadcast(intent);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        // 处理磁贴被添加到快捷设置时的逻辑
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        // 处理磁贴被移除时的逻辑

    }
    @Override
    public void onStopListening() {
        super.onStopListening();
    }


    /**
     * 更新磁贴状态
     * @param active
     */
    private void updateTileState(boolean active) {
        Tile tile = getQsTile();
        tile.setState(active ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            this.getApplicationContext().unregisterReceiver(updateReceiver);
        } catch (IllegalArgumentException e) {
            // 如果已经注销过，可能会抛出IllegalArgumentException，这里捕获并忽略
            Log.e(TAG, "BroadcastReceiver already unregistered.");
        }
    }
}