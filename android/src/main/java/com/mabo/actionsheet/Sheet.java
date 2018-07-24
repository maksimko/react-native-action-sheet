package com.mabo.actionsheet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.views.view.ColorUtil;

import java.util.ArrayList;

/**
 * Created by maksim on 22/03/2018.
 */

class Sheet extends BottomSheetDialog {
    private String title;
    private String message;
    private Integer tintColor;
    private Integer headerColor;
    private Integer headerTextColor;
    private Integer cancelIndex;
    private Object cancelOption;
    private Integer destructiveIndex;
    private Callback callback;
    private ArrayList<Object> options;

    private RecyclerView.Adapter adapter;

    Sheet(@NonNull Context context, @NonNull ReadableMap parameters, @Nullable final Callback callback) {
        super(context, R.style.ActionSheet);

        this.callback = callback;

        title = MapUtils.get(parameters, "title");
        message = MapUtils.get(parameters, "message");
        options = MapUtils.get(parameters, "options");
        cancelIndex = MapUtils.get(parameters, "cancelButtonIndex");
        destructiveIndex = MapUtils.get(parameters, "destructiveButtonIndex");

        String headerHexColor = MapUtils.get(parameters, "android.headerColor");
        String headerTextHexColor = MapUtils.get(parameters, "android.headerTextColor");
        String tintHexColor = MapUtils.get(parameters, "tintColor");

        if (headerHexColor != null) {
            headerColor = Color.parseColor(headerHexColor);
        }

        if (headerTextHexColor != null) {
            headerTextColor = Color.parseColor(headerTextHexColor);
        }

        if (tintHexColor != null) {
            tintColor = Color.parseColor(tintHexColor);
        }

        if (cancelIndex != null) {
            cancelOption = options.remove(cancelIndex.intValue());
        }

        this.adapter = new RecyclerView.Adapter<TextVH>() {
            @Override
            public TextVH onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.react_native_action_sheet_option, parent, false);
                return new TextVH(view);
            }

            @Override
            public void onBindViewHolder(TextVH holder, final int position) {
                Object option = options.get(position);

                final int actualPosition = cancelIndex != null && cancelIndex <= position ? position + 1 : position;

                final boolean isDestructive = destructiveIndex != null && actualPosition == destructiveIndex;
                final boolean isLast = position == options.size() - 1;

                holder.bind((String)option, isDestructive, isLast);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();

                        if (callback != null) {
                            callback.invoke(actualPosition);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return options.size();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.react_native_action_sheet, null);

        setContentView(view);
        setupViews(view);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void show() {
        try {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {}

        super.show();
    }

    class Manager extends LinearLayoutManager {
        public Manager(Context context) {
            super(context, LinearLayoutManager.VERTICAL, false);
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    private void setupViews(View rootView) {
        TextView titleTextView = findViewById(R.id.title);
        TextView messageTextView = findViewById(R.id.message);

        if (title == null && message == null) {
            findViewById(R.id.header).setVisibility(View.GONE);
            findViewById(R.id.header_divider).setVisibility(View.GONE);
        }

        if (title != null) {
            titleTextView.setText(title);
        } else {
            titleTextView.setVisibility(View.GONE);
        }

        if (message != null) {
            messageTextView.setText(message);
        } else {
            messageTextView.setVisibility(View.GONE);
        }

        if (headerColor != null) {
            findViewById(R.id.header)
                    .setBackgroundColor(headerColor);

            double luminance = ColorUtils.calculateLuminance(headerColor);

            if (luminance < 0.7 && headerTextColor == null) {
                titleTextView.setTextColor(Color.WHITE);
                messageTextView.setTextColor(Color.WHITE);
            }
        }

        if (headerTextColor != null) {
            titleTextView.setTextColor(headerTextColor);
            messageTextView.setTextColor(headerTextColor);
        }
        
        RecyclerView list = rootView.findViewById(R.id.options_list);
        list.setLayoutManager(new Manager(getContext()));
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (cancelOption != null) {
            View separator = rootView.findViewById(R.id.divider);

            if (headerColor != null) {
                separator.setBackgroundColor(ColorUtil.multiplyColorAlpha(headerColor, 76));
            }

            TextView cancelButton = rootView.findViewById(R.id.cancel_button);
            cancelButton.setText((String)cancelOption);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    callback.invoke(cancelIndex);
                }
            });

            separator.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        }
    }


    private class TextVH extends RecyclerView.ViewHolder {
        public TextVH(View itemView) {
            super(itemView);
        }

        public void bind(String text, boolean isDesctructive, boolean isLast) {
            TextView textView = (TextView)itemView;

            textView.setText(text);

            if (isDesctructive) {
                textView.setTextColor(Color.RED);
            } else if (tintColor != null) {
                textView.setTextColor(tintColor);
            }

            if (isLast) {
                textView.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
