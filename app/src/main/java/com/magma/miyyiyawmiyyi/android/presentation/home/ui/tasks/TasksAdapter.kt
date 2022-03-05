package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.databinding.ItemTaskBinding
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener

class TasksAdapter :
    ListAdapter<TaskObj, TasksAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemListener<TaskObj>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    fun setListener(listener: RecyclerItemListener<TaskObj>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: TaskObj) {
            binding.item = item
            binding.executePendingBindings()
        }

        init {
            binding.btnAction.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<TaskObj>() {
        override fun areItemsTheSame(
            oldItem: TaskObj,
            newItem: TaskObj
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: TaskObj,
            newItem: TaskObj
        ): Boolean {
            return newItem._id == oldItem._id
        }
    }


    override fun getItemCount() = currentList.size

}

