package com.asm.bluetoothchat.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asm.bluetoothchat.bluetooth.Message
import com.asm.bluetoothchat.databinding.CardMsgItemBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private var messages: ArrayList<Message> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = CardMsgItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    fun updateData(newMsg: Message) {
        messages.add(newMsg)
        notifyItemInserted(messages.size - 1)
    }

    inner class MessageViewHolder(
        private val binding: CardMsgItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvChatMsg.text = message.data
            if (message.dir == "incoming") {
                binding.llMsg.gravity = Gravity.END
            } else
                binding.llMsg.gravity = Gravity.START
        }
    }
}