package com.mabo.actionsheet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.views.view.ColorUtil
import com.mabo.actionsheet.utils.get

import java.util.ArrayList

/**
 * Created by maksim on 22/03/2018.
 */

internal class Sheet(context: Context, parameters: ReadableMap, private val callback: Callback?) : BottomSheetDialog(context, R.style.ActionSheet) {
    private val title: String? = parameters.get<String>("title")
    private val message: String? = parameters.get<String>("message")
    private val cancelIndex: Int? = parameters.get<Int>("cancelButtonIndex")
    private val destructiveIndex: Int? = parameters.get<Int>("destructiveButtonIndex")
    private val options: ArrayList<Any> = parameters.get("options", ArrayList())

    private var tintColor: Int? = null
    private var headerColor: Int? = null
    private var headerTextColor: Int? = null
    private var cancelOption: Any? = null
    private val adapter: RecyclerView.Adapter<*>

    init {
        val headerHexColor = parameters.get<String>("android.header.color")
        val headerTextHexColor = parameters.get<String>("android.header.textColor")
        val tintHexColor = parameters.get<String>("tintColor")

        if (headerHexColor != null) {
            headerColor = Color.parseColor(headerHexColor)
        }

        if (headerTextHexColor != null) {
            headerTextColor = Color.parseColor(headerTextHexColor)
        }

        if (tintHexColor != null) {
            tintColor = Color.parseColor(tintHexColor)
        }

        if (cancelIndex != null) {
            cancelOption = options.removeAt(cancelIndex)
        }

        this.adapter = object : RecyclerView.Adapter<TextVH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextVH {
                val view = layoutInflater.inflate(R.layout.react_native_action_sheet_option, parent, false)
                return TextVH(view)
            }

            override fun onBindViewHolder(holder: TextVH, position: Int) {
                val option = options[position]

                val actualPosition = if (cancelIndex != null && cancelIndex <= position) position + 1 else position

                val isDestructive = destructiveIndex != null && actualPosition == destructiveIndex

                holder.bind(option as String, isDestructive)
                holder.itemView.setOnClickListener {
                    dismiss()

                    callback?.invoke(actualPosition)
                }
            }

            override fun getItemCount(): Int {
                return options.size
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.react_native_action_sheet, null)

        setContentView(view)
        setupViews(view)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun show() {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
        } catch (e: Exception) {
        }

        super.show()
    }

    internal inner class Manager(context: Context) : LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {

        override fun canScrollHorizontally(): Boolean {
            return false
        }

        override fun canScrollVertically(): Boolean {
            return false
        }
    }

    private fun setupViews(rootView: View) {
        val titleTextView = findViewById<TextView>(R.id.title)
        val messageTextView = findViewById<TextView>(R.id.message)

        if (title != null || message != null)
            findViewById<View>(R.id.header)?.visibility = View.VISIBLE

        if (title != null)
            titleTextView!!.text = title
        else
            titleTextView!!.visibility = View.GONE

        if (message != null) {
            messageTextView!!.text = message
        } else {
            messageTextView!!.visibility = View.GONE
        }

        val list = rootView.findViewById<RecyclerView>(R.id.options_list)
        list.layoutManager = Manager(context)
        list.adapter = adapter
        adapter.notifyDataSetChanged()

        headerColor?.let { color ->
            rootView.findViewById<View>(R.id.header_divider)?.setBackgroundColor(ColorUtil.multiplyColorAlpha(color, 76))

            findViewById<View>(R.id.header)?.setBackgroundColor(color)

            val luminance = ColorUtils.calculateLuminance(color)

            if (luminance < 0.7 && headerTextColor == null) {
                titleTextView.setTextColor(Color.WHITE)
                messageTextView.setTextColor(Color.WHITE)
            }
        }

        headerTextColor?.let { color ->
            titleTextView.setTextColor(color)
            messageTextView.setTextColor(color)
        }

        cancelOption?.let { option ->
            val cancelButton = rootView.findViewById<TextView>(R.id.cancel_button)
            cancelButton.text = option as String
            cancelButton.setOnClickListener {
                dismiss()
                callback?.invoke(cancelIndex)
            }

            cancelButton.visibility = View.VISIBLE
        }
    }


    private inner class TextVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(text: String, isDestructive: Boolean) {
            val textView = itemView as TextView

            textView.text = text

            if (isDestructive) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.react_native_action_sheet_destructive_button))
            } else {
                tintColor?.let { color ->
                    textView.setTextColor(color)
                }
            }
        }
    }
}
