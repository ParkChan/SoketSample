package com.socket;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.orhanobut.logger.Logger;
import com.socket.databinding.ActivityMainBinding;
import com.socket.socket.SocketMannager;
import com.socket.socket.constant.NetworkConstant;
import com.squareup.otto.Subscribe;

/**
 * https://socket.io/blog/native-socket-io-and-android
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        initLayout();
        initListener();
        SocketMannager.getInstance().registerEventBus(this);

    }

    private void initLayout() {
        binding.tvMessage.setText("서버 메시지...");
    }

    private void initListener() {
        binding.btnSend.setOnClickListener(view -> {
            sendMessage(binding.etMessage.getText().toString());
        });
    }

    /**
     * 소켓 통신 전송
     * @param message 메시지
     */
    private void sendMessage(String message){
        if(message != null && !message.isEmpty()){
            SocketMannager.getInstance().sendMessage(NetworkConstant.SOCKET_NEW_MESSAGE, message);
            binding.etMessage.setText("");
            Toast.makeText(this, "메시지 전송", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * EventBus Received
     * @param message : EventBus Received Data
     */
    @Subscribe
    public void onReceived(String message) {
        binding.tvMessage.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Logger.d("@Subscribe onReceived >>> " + message);
    }

    @Override
    protected void onDestroy() {
        SocketMannager.getInstance().unregisterEventBus(this);
        super.onDestroy();
    }
}
