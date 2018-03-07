package com.mabo.actionsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

/**
 * Created by maksim on 2/19/18.
 */

public class ActionSheetModule extends ReactContextBaseJavaModule {
    protected boolean isShown;

    @Override
    public String getName() {
        return "ActionSheet";
    }

    public ActionSheetModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @SuppressWarnings("unused")
    @ReactMethod
    public void showActionSheetWithOptions(ReadableMap parameters, final Callback callback) {
        if (isShown) { return; }

        isShown = true;

        ArrayList<Object> options = new ArrayList<>();
        String title = null;
        Integer cancelIndex = null;

        if (parameters.hasKey("options")) {
            options = parameters.getArray("options").toArrayList();
        }

        if (parameters.hasKey("title")) {
            title = parameters.getString("title");
        }

        if (parameters.hasKey("cancelButtonIndex")) {
            cancelIndex = parameters.getInt("cancelButtonIndex");
        }

        Sheet sheet = new Sheet(getReactApplicationContext().getCurrentActivity())
                .withParameters(options, title, cancelIndex, callback);


        sheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShown = false;
            }
        });

        sheet.show();
    }

    private class Sheet extends BottomSheetDialog {
        private String title;
        private Integer cancelIndex;
        private Object cancelOption;
        private ArrayList<Object> options;
        private Callback callback;

        private RecyclerView.Adapter adapter;

        public Sheet(@NonNull Context context) {
            super(context, R.style.ActionSheet);
        }

        public Sheet(@NonNull Context context, int theme) {
            super(context, theme);
        }

        public Sheet(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        public Sheet withParameters(final ArrayList<Object> options, final String title, final Integer cancelIndex, final Callback callback) {
            this.title = title;
            this.options = options;
            this.callback = callback;
            this.cancelIndex = cancelIndex;

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

                    holder.bind((String)option);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               int actualPosition = position;

                               if (cancelIndex != null && cancelIndex <= position) {
                                   actualPosition = position + 1;
                               }

                               dismiss();
                               callback.invoke(actualPosition);
                           }
                       });
                }

                @Override
                public int getItemCount() {
                    return options.size();
                }
            };

            return this;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View view  = getLayoutInflater().inflate(R.layout.react_native_action_sheet, null);

            setContentView(view);
            setupViews(view);
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
            RecyclerView list = rootView.findViewById(R.id.options_list);
            list.setLayoutManager(new Manager(getContext()));
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (cancelOption != null) {
                TextView cancelButton = rootView.findViewById(R.id.cancel_button);
                cancelButton.setText((String)cancelOption);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        callback.invoke(cancelIndex);
                    }
                });
                cancelButton.setVisibility(View.VISIBLE);
            }
        }









        private class TextVH extends RecyclerView.ViewHolder {
            public TextVH(View itemView) {
                super(itemView);
            }

            public void bind(String text) {
                ((TextView)itemView).setText(text);
            }
        }
    }
}
