package com.mufengmucao.remind_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.mufengmucao.remind_test.constants.MyConstant;
import com.mufengmucao.remind_test.databinding.FragmentFirstBinding;
import com.mufengmucao.remind_test.helper.MedicalStateDataUtil;
import com.mufengmucao.remind_test.helper.MessageUtil;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TextView textViewContent;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewContent= binding.textviewFirst;

        //刷新按钮
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                textViewContent.setText(MedicalStateDataUtil.readAllData(getContext()));
            }
        });

        //监听可能改变文本内容的事件，更新展示内容
        BroadcastReceiver updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //重新读取文件内容并更新UI
                textViewContent.setText(MedicalStateDataUtil.readAllData(getContext()));

                // 清除通知
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.cancel(MessageUtil.getMedicalRemind());
            }
        };
        //监听磁贴更新事件
        IntentFilter filter = new IntentFilter(MyConstant.ACTION_TILE_UPDATE);
        ContextCompat.registerReceiver(getContext(), updateReceiver, filter, ContextCompat.RECEIVER_EXPORTED);

        //监听消息按钮的更新事件
        IntentFilter filter1 = new IntentFilter(MyConstant.ACTION_MESSAGE_UPDATE);
        ContextCompat.registerReceiver(getContext(), updateReceiver, filter1, ContextCompat.RECEIVER_EXPORTED);

    }


    @Override
    public void onResume() {
        super.onResume();
        // 在这里刷新数据
        textViewContent.setText(MedicalStateDataUtil.readAllData(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}