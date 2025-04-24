package com.mabo.actionsheet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReadableMap
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mabo.actionsheet.databinding.ReactNativeActionSheetBinding
import com.mabo.actionsheet.utils.get

/**
 * Created by maksim on 22/03/2018.
 */

internal class Sheet(context: Context, parameters: ReadableMap, private val callback: Callback?) :
  BottomSheetDialog(context, R.style.RN_ActionSheet) {
  private val title: String? = parameters.get<String>("title")
  private val message: String? = parameters.get<String>("message")
  private val cancelIndex: Int? = parameters.get<Int>("cancelButtonIndex")
  private val destructiveIndex: Int? = parameters.get<Int>("destructiveButtonIndex")
  private val disabledButtonIndices = parameters.get<ArrayList<Int>>("disabledButtonIndices", ArrayList()).map { it.toInt() }
  private val disabledButtonTintHexColor = parameters.get<String>("disabledButtonTintColor")
  private val options: ArrayList<String> = parameters.get("options", ArrayList<String>())


  private var tintColor: Int? = null
  private var cancelButtonTintColor: Int? = null
  private var cancelOption: Any? = null
  private val adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>

  init {
    val tintHexColor = parameters.get<String>("tintColor")
    val cancelButtonTintHexColor = parameters.get<String>("cancelButtonTintColor")
    val cancelable = parameters.get<Boolean>("android.cancelable", true)

    this.setCancelable(cancelable)

    if (tintHexColor != null) {
      tintColor = Color.parseColor(tintHexColor)
    }

    if (cancelButtonTintHexColor != null) {
      cancelButtonTintColor = Color.parseColor(cancelButtonTintHexColor)
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

        val actualPosition =
          if (cancelIndex != null && cancelIndex <= position) position + 1 else position

        val isDestructive = destructiveIndex != null && actualPosition == destructiveIndex
        val isDisabled = disabledButtonIndices.contains(actualPosition)

        holder.bind(option, isDestructive, isDisabled)

        if (!isDisabled) {
          holder.itemView.setOnClickListener {
            dismiss()

            callback?.invoke(actualPosition)
          }
        }
      }

      override fun getItemCount(): Int {
        return options.size
      }
    }
  }

  private lateinit var sheetViewBindings: ReactNativeActionSheetBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sheetViewBindings = ReactNativeActionSheetBinding.inflate(layoutInflater)

    setContentView(sheetViewBindings.root)
    setupViews()

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

  internal inner class Manager(context: Context) : androidx.recyclerview.widget.LinearLayoutManager(
    context,
    androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
    false
  ) {

    override fun canScrollHorizontally(): Boolean {
      return false
    }

    override fun canScrollVertically(): Boolean {
      return false
    }
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun setupViews() {
    val titleTextView = sheetViewBindings.title
    val messageTextView = sheetViewBindings.message
    val headerView = sheetViewBindings.header
    val optionsList = sheetViewBindings.optionsList
    val headerDivider = sheetViewBindings.headerDivider
    val cancelButton = sheetViewBindings.cancelButton

    titleTextView.setTypeface(null, Typeface.BOLD)

    if (title != null || message != null)
      headerView.visibility = View.VISIBLE

    if (title != null)
      titleTextView.text = title
    else
      titleTextView.visibility = View.GONE

    if (message != null) {
      messageTextView.text = message
    } else {
      messageTextView.visibility = View.GONE
    }

    optionsList.layoutManager = Manager(context)
    optionsList.adapter = adapter

    adapter.notifyDataSetChanged()

    if (title == null && message == null) {
      headerDivider.visibility = View.GONE
    }

    cancelOption?.let { option ->
      cancelButton.text = option as String
      cancelButton.setOnClickListener {
        dismiss()
        callback?.invoke(cancelIndex)
      }
      if (cancelButtonTintColor != null) {
        cancelButton.setTextColor(cancelButtonTintColor!!)
      }

      cancelButton.visibility = View.VISIBLE
    }
  }


  private inner class TextVH(itemView: View) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    fun bind(text: String, isDestructive: Boolean, isDisabled: Boolean) {
      val textView = itemView as TextView

      textView.text = text

      if (isDestructive) {
        textView.setTextColor(
          ContextCompat.getColor(
            context,
            R.color.react_native_action_sheet_destructive_button
          )
        )
      } else if (isDisabled) {
        textView.setTextColor(
          disabledButtonTintHexColor?.let { Color.parseColor(it) } ?:
          ContextCompat.getColor(
            context,
            R.color.react_native_action_sheet_gray
          )
        )
      } else {
        tintColor?.let { color ->
          textView.setTextColor(color)
        }
      }
    }
  }
}
