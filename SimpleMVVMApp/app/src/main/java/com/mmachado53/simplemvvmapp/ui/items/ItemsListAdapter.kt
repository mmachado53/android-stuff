package com.mmachado53.simplemvvmapp.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mmachado53.simplemvvmapp.R
import com.mmachado53.simplemvvmapp.commons.OnSelectWithAction
import com.mmachado53.simplemvvmapp.data.model.Item
import com.mmachado53.simplemvvmapp.databinding.RowItemListBinding

class ItemsListAdapter(private val onSelect: OnSelectWithAction<Item, SelectAction>) : ListAdapter<Item, ItemsListAdapter.ItemViewHolder>(ItemDiffCallback) {

    enum class SelectAction { Show, Delete }

    class ItemViewHolder(private val binding: RowItemListBinding, private val onSelect: OnSelectWithAction<Item, SelectAction>) : RecyclerView.ViewHolder(binding.root) {
        private var item: Item? = null
        init {
            binding.root.setOnClickListener {
                item?.let {
                    onSelect.onSelect(it, SelectAction.Show)
                }
            }
            binding.deleteButton.setOnClickListener {
                item?.let {
                    onSelect.onSelect(it, SelectAction.Delete)
                }
            }
        }
        fun bind(item: Item) {
            this.item = item
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RowItemListBinding = DataBindingUtil.inflate(inflater, R.layout.row_item_list, parent, false)
        return ItemViewHolder(binding, onSelect)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(getItem(position))
}

object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.content == newItem.content
    }
}
